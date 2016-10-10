
public class Person {

	private String name;
	private static int NumPersons = 0;
	public Person(String name)
	{
		this.name = name;
		NumPersons++;
	}
	public String getName(){
		return this.name;
	}
	public static int getNumPersons(){
		 return NumPersons;
	}


}
