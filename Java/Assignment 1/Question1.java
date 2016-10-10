public class Question1 {
	
	public static int generator(int x)
	{
		int f = 0;
		f = x;
		while (x>0){
			f+=x%10;	
			x=x/10;
		}
		return f;
	}
	public static void selfnumber(int x)
	{
		int [] array1 = new int[x];
		int [] array2 = new int[x];
		int [] array3 = new int[x];
		int l = 0;
		boolean tester; 
		for (int i=1; i<=x; i++)
		{
			array1[i-1]=i;
			array2[i-1]=generator(i);
		}
		for (int number1 : array1)
		{
			tester = false;
			for (int number2 : array2)
			{
				if (number1==number2)
				{
					tester = true;

				}
			//System.out.println(tester + "  " + number1 + "   " + number2);

			}
			if(tester==false)
				System.out.println(number1);
		}
	}

	public static void main(String args[])
	{
		int x = Integer.parseInt(args[0]);
		selfnumber(x);
	}
}
