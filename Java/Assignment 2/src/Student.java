public class Student extends Person{
	private String teacher;
    boolean taken = false;
    private static int NumStudents = 0;
    Student(String name){
        super(name);
        NumStudents++;
    }
    public void takeTeacher(Teacher teacher){
        if(this.taken) {
            System.out.println("Student " + this.getName() + " cannot take teacher " + teacher.getName() + " because student " +this.getName()+ " has already taken teacher " + this.teacher + ".");
        }
        else if(teacher.addStudent(this.getName()))
        {
            this.teacher=teacher.getName();
            this.taken = true;
            System.out.println("Student " + this.getName() + " has taken teacher " + teacher.getName() + " successfully.");
        }
        else {
            System.out.println("Student " + this.getName() + " cannot take teacher " +teacher.getName() + " because teacher " + teacher.getName() + " has no more quota.");
        }


    }

    public void dropTeacher(Teacher teacher){
        teacher.removeStudent(this.getName());
        this.teacher=null;
        this.taken = false;
        System.out.println("Student " + this.getName() + " has dropped teacher " + teacher.getName() + ".");
    }

    public static int getNumStudents(){
        return NumStudents;
    }



}
