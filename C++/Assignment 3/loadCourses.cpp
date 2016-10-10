void loadCourses(Database &d, string fileName)
{
	ifstream fin;
	fin.open(fileName.c_str());
	if (fin.fail()){
		cout << "Error in opening the file!";
		exit(1); 
	}
	else
	{
		fin >> d.numberOfCourses;
		d.courses = new Course[d.numberOfCourses];
		for (int i = 0 ; i<d.numberOfCourses ; i++)
		{
			fin >> d.courses[i].code;
			fin.ignore();
			getline(fin,d.courses[i].name);
		}
	}
	fin.close();
};
void queryB(Database d)
{
	for (int i=0;i<d.numberOfCourses;i++)
	{
		cout << d.courses[i].code <<" ";
		cout << d.courses[i].name <<endl;
	}
}