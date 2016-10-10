
public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JUPASStudent[] J = null;
		NonJUPASStudent[] NJ = null;
		try {
			J = Tools.ReadJUPASFile("JUPAS.txt");
			}
		catch (Exception e) {
			System.out.println("File not found");
			}
		try {
			NJ = Tools.ReadNonJUPASFile("NonJUPAS.txt");
			}
		catch (Exception e) {
			System.out.println("File not found");
			}
//		try {
			System.out.println("Original JUPAS Student Array:");
			Tools.PrintArray(J);
			System.out.println("\nOriginal Non-JUPAS Student Array:");
			Tools.PrintArray(NJ);
			Student[] C;
			C = Tools.CombineArray(J, NJ);
			System.out.println("\nCombined Array: ");
			Tools.PrintArray(C);
			System.out.println("\nSorted Array: ");
			Student[] S;
			S = Tools.Sort(C);
			Tools.PrintArray(S);
//		}
//		catch (Exception e)
//		{
//			System.out.println(e);
//		}

	}

}
