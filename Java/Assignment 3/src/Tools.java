import java.io.*;

public class Tools extends Student{
	
	public static JUPASStudent[] ReadJUPASFile(String F) throws Exception
	{
		int i = 0;
		int j = 0;
		BufferedReader readbuffer = null;
		String strRead;
		readbuffer = new BufferedReader(new FileReader(F));
		while (readbuffer.readLine() != null) {
			i++;
		}
		readbuffer.close();
		BufferedReader readbuffer1 = null;
		JUPASStudent[] JUPASArray = new JUPASStudent[i];
		readbuffer1 = new BufferedReader(new FileReader(F));
		while ((strRead = readbuffer1.readLine()) != null) {
			String splitarray[] = strRead.split("\t");
			String firstentry = splitarray[0];
			String secondentry = splitarray[1];
			JUPASStudent jstudent = new JUPASStudent(firstentry, Double.parseDouble(secondentry));
			JUPASArray[j] = jstudent;
			j++;
		}
		readbuffer1.close();
		return JUPASArray;
	}
	
	public static NonJUPASStudent[] ReadNonJUPASFile(String F) throws Exception
	{
		int i = 0;
		int j = 0;
		BufferedReader readbuffer = null;
		String strRead;
		readbuffer = new BufferedReader(new FileReader(F));
		while (readbuffer.readLine() != null) {
			i++;
		}
		readbuffer.close();
		BufferedReader readbuffer1 = null;
		NonJUPASStudent[] NonJUPASArray = new NonJUPASStudent[i];
		readbuffer1 = new BufferedReader(new FileReader(F));
		while ((strRead = readbuffer1.readLine()) != null) {
			String splitarray[] = strRead.split("\t");
			String firstentry = splitarray[0];
			String secondentry = splitarray[1];
			NonJUPASStudent nstudent = new NonJUPASStudent(firstentry, Double.parseDouble(secondentry));
			NonJUPASArray[j] = nstudent;
			j++;
		}
		readbuffer1.close();
		return NonJUPASArray;
	}
	
	public static Student[] CombineArray(JUPASStudent[] S1, NonJUPASStudent[] S2)
	{
		Student[] student = new Student[S1.length+S2.length];
		for(int i = 0; i < S1.length; i++)
		{
			student[i] = S1[i];
		}
		for(int i = 0; i <S2.length; i++)
		{
			student[i+S1.length] = S2[i];

		}
		return student;
	}
	
	public static void PrintArray(Student[] S)
	{
		String Name;
		Double Score;
		for(int i = 0; i<S.length; i++)
		{
			Name = S[i].getName();
			Score = S[i].getResult();
			System.out.println(Name + " " + Score);
		}
	}

	public static Student[] Sort(Student[] S) {
		int i, j;
		Student t;
		for (i = 0; i < S.length - 1; i++){
			for (j = i + 1; j < S.length; j++) {
				if (S[i].getResult() > S[j].getResult() && (S[j] != null && S[i] != null))
				{
					t = S[i];
					S[i] = S[j];
					S[j] = t;
				}
			}
		}
		return S;
	}

}
