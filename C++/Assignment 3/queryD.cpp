void queryD(Database d, string searchName)
{
	for (int i=0; i<d.numberOfStudents; i++)
	{
		if (d.students[i].name.find(searchName) != std::string::npos) 
		{
  			int x = d.students[i].uid;
  			cout << x<< " ";
  			double average = 0.00;
  			int number =0;
  			for (int j = 0; j<d.numberOfEnrollRecords; j++)
  			{
  				if (d.enrollRecords[j].uid==x)
  				{
  					average = average + d.enrollRecords[j].gpa;
  					number++;
  				}
  			}
  			cout << (average/number) << endl;
		}	
	};
};