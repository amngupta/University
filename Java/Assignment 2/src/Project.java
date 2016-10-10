/**
 * Sample program testing the system.
 * @author Haoyuan
 */
public class Project {

    /**
     * The main method
     * @param args Arguments
     */
    public static void main(String[] args) {
        Teacher Chim = new Teacher("Chim");
        Teacher Zhang = new Teacher("Zhang");
        Student Chan = new Student("Chan");
        Student Wong = new Student("Wong");
        Student Lee = new Student("Lee");
        Student Cheung = new Student("Cheung");
        Student Woo = new Student("Woo");
        Chan.takeTeacher(Zhang);
        Wong.takeTeacher(Zhang);
        Lee.takeTeacher(Zhang);
        Lee.takeTeacher(Chim);
        Cheung.takeTeacher(Zhang);
        Cheung.takeTeacher(Chim);
        Lee.dropTeacher(Zhang);
        Woo.takeTeacher(Zhang);
        System.out.println("Number of Teachers: " + Teacher.getNumTeachers());
        System.out.println("Number of Students: " + Student.getNumStudents());
        System.out.println("Number of Persons: " + Person.getNumPersons());
    }

}
