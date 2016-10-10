
public class Test4 {

	public static void main(String[] args) {
		
		Teacher teacherA = new Teacher("TeacherA");
		Student studentA = new Student("StudentA");
		System.out.println("Test4:");
		studentA.takeTeacher(teacherA);
		studentA.dropTeacher(teacherA);
		System.out.println();
		
	}
	
}
