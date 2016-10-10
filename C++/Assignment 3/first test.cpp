#include <iostream>
#include <fstream>
#include <cstdlib>
using namespace std;

struct Student
{
	int uid;
	string programme;
	string name;
};
struct Course
{
	string code;
	string name;
};
struct EnrollRecord
{
	string code;
	int uid;
	double exam;
	double gpa;
};
struct Database
{
	int numberOfStudents;
	int numberOfCourses;
	int numberOfEnrollRecords;
	Student *students;
	Course *courses;
	EnrollRecord *enrollRecords;
};

void loadStudents(Database &d, string fileName);
void loadCourses(Database &d, string fileName);
void loadEnrollRecords(Database &d, string fileName);
void queryB(Database d);
void queryA(Database d);
void queryC(Database d);
void queryD(Database d, string searchName);
void queryE(Database d, string courseCode);

#include "queryD.cpp"
#include "queryE.cpp"
#include "loadStudents.cpp"
#include "loadCourses.cpp"
#include "loadEnrollRecords.cpp"

int main()
{

Database d;
loadStudents(d,"Students.txt");
// queryA(d);
loadCourses(d,"Courses.txt");
// queryB(d);
loadEnrollRecords(d,"EnrollRecords.txt");
// queryC(d);
queryD(d, "Will");
// queryE(d,"ENGG1111");
};