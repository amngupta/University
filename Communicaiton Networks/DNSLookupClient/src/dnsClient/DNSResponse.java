package dnsClient;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.util.ArrayList;

public class DNSResponse {
    private int queryID;                  // this is for the response it must match the one in the request 
    protected int answerCount = 0;        // number of answers
    protected int nsCount = 0;              // number of nscount response records
    protected int additionalCount = 0;      // number of additional (alternate) response records
    protected boolean authoritative = false;// Is this an authoritative record

    /**
     * This is the rData class that is used to store information about each RData in response.
     */
    class rData{
        String ipAddress;
        String name;
        String ns;
        int type;
        int classInt;
        long ttl;
        int rdLength;
        int totalOffset;
        rData(){
            return;
        }
    }

    DNSResponse(){
        return;
    }

    /**
     * Used to parse the information in the response that is contained as  2 byte integer
     * @param arr array that contains the integers
     * @param offset position in the array where to read from
     * @return
     */
    private int convertBytesToInt (byte [] arr, int offset)      // unsigned
    {
        return (arr[offset] & 0xFF) << 8 | (arr[offset+1] & 0xFF);
    }

    /**
     * Used to parse the TTL, basically converts 4 bytes into an integer
     * @param bytes array that contains the integers
     * @param offset position in the array where to read from
     * @return
     */
    private int convert4BytesToInt(byte[] bytes, int offset) {
        return bytes[offset] << 24 | (bytes[offset+1] & 0xFF) << 16 | (bytes[offset+2] & 0xFF) << 8 | (bytes[offset+3] & 0xFF);
    }

    /**
     * Returns the human-readable TYPE of the RData Type
     * @param i the integer value of the RData Type
     * @return string 'A', 'NS', 'AAAA', 'CN'
     */
    protected static String getActualType(int i){
        String actualType = "";
        switch (i){
            case 1:{
                actualType = "A";
                break;
            }
            case 2:{
                actualType = "NS";
                break;
            }
            case 28:{
                actualType = "AAAA";
                break;
            }
            case 5:{
                actualType = "CN";
                break;
            }
        }
        return actualType;
    }

    /**
     * Encodes the query into a byte array before sending to the DNS
     * @param query: The string containing the website being looked up
     * @return byte array containing all the information for a query
     */
    protected byte[] encodeQuery(String query, DNSlookup looker) {
        byte[] encodedQuery = new byte[64];
        try {
            query = query.replace(new String(Character.toChars(0)), "");
            String[] split = query.split("\\.");
            int counter = 12;
            int queryId = (int) (Math.random() * 255);
            encodedQuery[1] = (byte) queryId;
            encodedQuery[2] = 0;
            encodedQuery[3] = 0;
            encodedQuery[5] = 1;
            for (int i = 0; i < split.length; i++) {
                encodedQuery[counter] = (byte) split[i].length();
                counter++;
                for (int j = 0; j < split[i].length(); j++) {
                    String a = split[i];
                    char c = a.charAt(j);
                    try {
                        String hexForAscii = Integer.toHexString((int) c);
                        byte test = Byte.parseByte(hexForAscii, 16);
                        encodedQuery[counter] = test;
                        counter++;
                    }catch (Exception e){
                        e.printStackTrace();
                        break;
                    }
                }
            }
            if (looker.IPV6Query) {
                encodedQuery[counter + 2] = 28;
            } else {
                encodedQuery[counter + 2] = 1;
            }
            encodedQuery[counter + 4] = 1;
        }catch(Exception e){
            e.printStackTrace();
        }
        return encodedQuery;
    }

    /**
     * Parses the name or the NS of the RData
     * @param arr: The byte array containing the response from the DNS server
     * @param offset: The integer offset where the name starts from
     * @return name in the RData Object as byte array
     */
    protected byte[] getName(byte[] arr, int offset) {
        byte[] name = new byte[512];

        int i = 0;

        while(true) {

            if ((arr[offset] & 0xFF) == 0) {
                return name;
            }
            if ((arr[offset] & 0xFF) == 192) {
                offset = arr[offset+1];
                continue;
            }
            if (arr[offset] > 40) {
                name[i] = arr[offset];
                i++;
                offset++;
                continue;
            } else {
                if (i != 0) {
                    name[i] = 0x2e;
                    i++;
                    offset++;
                    continue;
                }
                offset++;
            }
        }


    }

    /**
     * Reads the data from a certain offset in the array to parse all information about that Response Data Object
     * @param arr: The byte array containing the response from the DNS server
     * @param offset: The integer offset where the RData starts from
     * @return returns the rData Object
     */
    protected rData readRDATA (byte [] arr, int offset){
        rData rData = new rData();

        try {
            String nameString  =  new String(getName(arr,offset), "UTF-8");
            rData.name = nameString;
            String nsString = new String(getName(arr,offset+11), "UTF-8");
            rData.ns = nsString;
        } catch (Exception e){
        }
        rData.type = this.convertBytesToInt(arr, offset+2);
        rData.classInt = this.convertBytesToInt(arr, offset+4);
        rData.ttl = this.convert4BytesToInt(arr, offset+6);

        rData.rdLength = this.convertBytesToInt(arr, offset+10);

        int innerOffset = offset+12;
        String ipAddress = "";
        if(rData.type == 1 && rData.rdLength == 4){
            byte[] nextIp = new byte[4];
            nextIp[0] = arr[innerOffset];
            nextIp[1] = arr[innerOffset+1];
            nextIp[2] = arr[innerOffset+2];
            nextIp[3] = arr[innerOffset+3];
            try {
                rData.ipAddress = InetAddress.getByAddress(nextIp).toString().replaceAll("[/ ]","");
            }
            catch (Exception e) {
            }
        }
        if(rData.type == 28 && rData.rdLength ==16){
            byte[] tmp2 = new byte[16];
            System.arraycopy(arr, innerOffset, tmp2, 0, 16);
            try {
                rData.ipAddress= Inet6Address.getByAddress(tmp2).toString().replaceAll("[/ ]","");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else{
            for (int i = 0; i < rData.rdLength; i++)
            {
                int ipBits = arr[innerOffset+i];
                ipAddress += ipBits + ".";
            }
            try {
                rData.ipAddress = InetAddress.getByName(ipAddress.substring(0, ipAddress.length() - 1)).toString().replaceAll("[/ ]","");
            }catch(Exception e){
//                e.printStackTrace();
            }
        }
        rData.totalOffset = 12+rData.rdLength;
        return rData;
    }

    /**
     * Method to handle the printing for traceRoute
     * @param rDatas the arraylist of traceRoutes for one iteration of the query
     * @param looker the parent looker class required for some protected variables.
     */
    protected void printRDATA(ArrayList<rData> rDatas, DNSlookup looker){
        String queryType = "A";
        if(looker.IPV6Query){
            queryType = "AAAA";
        }
        System.out.println();
        System.out.println();
        System.out.println("Query ID     "+this.queryID+" "+" "+looker.queryString+"  "+queryType+" --> "+looker.dnsString.toString().replaceAll("[/]",""));
        System.out.println("Response ID "+this.queryID+" Authoritative = "+this.authoritative);
        System.out.println("  Answers ("+this.answerCount+")");
        int i = 0;
        for(rData r: rDatas){
            if(i == this.answerCount) {
                System.out.println("  Nameservers (" + this.nsCount + ")");
            }
            if(i == this.nsCount+this.answerCount){
                System.out.println("  Additional Information ("+this.additionalCount+")");
            }
            if(r.ipAddress == null){
                System.out.printf("       %-30s %-10d %-4s %s\n", r.name, r.ttl, getActualType(r.type), r.ns);
            }
            else{
                System.out.printf("       %-30s %-10d %-4s %s\n", r.name, r.ttl, getActualType(r.type), r.ipAddress);
            }
            i++;
        }
    }


    /**
     * This method decodes the queryResponse
     * @param response: The byte array of the response received from the DNS server
     * @param looker: The DNSloopup class object that is calling the method
     * @return rData object that best fits the query
     * @throws Exception
     */
    rData decodeQuery(byte[] response, DNSlookup looker) throws  Exception{
        byte[] defaultadd = new byte[4];
        InetAddress s = InetAddress.getByAddress(defaultadd);
        String qname = "";
        int counter = 0;
        this.queryID = this.convertBytesToInt(response, 0);
        int questionCount = this.convertBytesToInt(response, 4);
        this.answerCount = this.convertBytesToInt(response, 6);
        this.nsCount = this.convertBytesToInt(response,8);
        this.additionalCount = this.convertBytesToInt(response, 10);
        int offset = 12;
        while (response[offset] != 0){
            offset++;
        }

        int queryType = this.convertBytesToInt(response, offset+1);
        int queryClass = this.convertBytesToInt(response, offset+3);

        if(this.answerCount >= 1 || queryType == 6){
            this.authoritative = true;
        }else{
            this.authoritative = false;
        }
        offset +=5;
        int totalCount = this.nsCount + this.additionalCount +this.answerCount;
        ArrayList<rData> rDataList = new ArrayList<rData>();
        for (int i = 0; i < totalCount; i++){
            rData r = readRDATA(response, offset);
            if (r.type == 6) {
                authoritative = true;
            }
            offset += r.totalOffset;
            if(looker.tracingOn){

            }
            rDataList.add(r);
        }
        if(looker.tracingOn){
            this.printRDATA(rDataList, looker);
        }
        for(rData c: rDataList) {

            if(this.answerCount >= 1){
                return rDataList.get(0);
            }
//            if(looker.IPV6Query){
//                if(c.type == 28){
//                    return c;
//                }
//            }
            else {
                if (c.type == 1) {
                    return c;
                }
                if(c.type == 6){
                    authoritative = true;
                    return c;
                }
            }
        }
        return rDataList.get(0);
    }
}


