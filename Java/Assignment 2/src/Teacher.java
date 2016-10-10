
public class Teacher extends Person{
	private String[] students = new String[3];
	private int numStudents;
	private static int NumTeachers = 0;
	public Teacher(String name)
	{
		super(name);
		NumTeachers++;
	}
	public boolean addStudent(String studentName)
	{
		if(this.numStudents < 3)
		{
			for(int i =0;i<3;i++)
			{
				if(this.students[i] == null)
				{
					this.students[i] = studentName;
					this.numStudents++;
					return true;
				}
			}
		}
		return false;
	}
	
	public void removeStudent(String studentName)
	{
		for(int i =0;i<3;i++)
		{
			if(this.students[i].equals(studentName))
			{
				this.students[i] = null;
				this.numStudents--;
			}
		}
	}
	
	public static int getNumTeachers(){
		return NumTeachers;
	}



}
