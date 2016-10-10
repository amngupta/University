
public class Question2 {
	public static void checkdigit(String x)
	{
		int f = 0;
		char test = x.charAt(0);
		int a = (int)test;
		a-=64;
		int b = Integer.parseInt(x.substring(1, 7));
		f=a*8;
		int i=2;
		for (int j=0; j<6;j++ )
		{
			f+=(b%10)*i;
			b=b/10;
			i++;
		}
		f=11-(f%11);
		if(f==10)
			System.out.println('A');
		if(f==0)
			System.out.println(0);
		else
			System.out.println(f);
	}
	public static void main(String args[])
	{
		String s = (args[0]);
;
		checkdigit(s);
	}
}
