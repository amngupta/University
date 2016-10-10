public class NonJUPASStudent extends Student {
private double IELTSResult;
public static int NumNonJUPASStudent = 0;
public NonJUPASStudent(String N, double I) {
super.Name = N;
super.Result = I;
IELTSResult = I;
NumNonJUPASStudent ++;
}
public double getResult() {
return IELTSResult;
}
}