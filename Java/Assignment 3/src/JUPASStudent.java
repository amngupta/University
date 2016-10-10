public class JUPASStudent extends Student {
private double DSEResult;
public static int NumJUPASStudent = 0;
public JUPASStudent(String N, double D) {
super.Name = N;
super.Result = D;
DSEResult = D;
NumJUPASStudent ++;
}
public double getResult() {
return DSEResult;
}
}
