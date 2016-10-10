void loadStudents(Database &d, string fileName)
{
	ifstream fin;
	fin.open(fileName.c_str());
	if (fin.fail()){
		cout << "Error in opening the file!";
		exit(1); 
	}
	else
	{
		fin >> d.numberOfStudents;
		d.students = new Student[d.numberOfStudents];
		for (int i = 0 ; i<d.numberOfStudents ; i++)
		{
			fin >> d.students[i].uid;
			fin >> d.students[i].programme;
			fin.ignore();
			getline(fin,d.students[i].name);
		};
	}
	fin.close();
};
void queryA(Database d)
{
	for (int i=0;i<d.numberOfStudents;i++)
	{
		cout << d.students[i].uid <<" ";
		cout << d.students[i].programme << " ";
		cout << d.students[i].name <<endl;
	}
}