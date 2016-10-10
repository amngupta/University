
public class Test6 {

	public static void main(String[] args) {
		
		Teacher teacherA = new Teacher("TeacherA");
		Teacher teacherB = new Teacher("TeacherB");
		Student studentA = new Student("StudentA");
		System.out.println("Test6:");
		studentA.takeTeacher(teacherA);
		studentA.dropTeacher(teacherA);
		studentA.takeTeacher(teacherB);
		System.out.println();
		
	}
	
}
