void loadEnrollRecords(Database &d, string fileName)
{
	ifstream fin;
	fin.open(fileName.c_str());
	if (fin.fail()){
		cout << "Error in opening the file!";
		exit(1); 
	}
	else
	{
		fin >> d.numberOfEnrollRecords;
		d.enrollRecords = new EnrollRecord[d.numberOfEnrollRecords];
		for (int i = 0 ; i<d.numberOfEnrollRecords ; i++){
			fin >> d.enrollRecords[i].code;
			fin >> d.enrollRecords[i].uid;
			fin >> d.enrollRecords[i].exam;
			fin >> d.enrollRecords[i].gpa;
		};
		fin.close();
	};
};
void queryC(Database d)
{
	for (int i=0;i<d.numberOfEnrollRecords;i++)
	{
		cout << d.enrollRecords[i].code <<" ";
		cout << d.enrollRecords[i].uid <<" ";
		cout << d.enrollRecords[i].exam << " ";
		cout << d.enrollRecords[i].gpa <<endl;
	};
};