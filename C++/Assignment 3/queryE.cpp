void sorter(double list[], int length)
{
	double temp =0;
	for (int i=0; i<length; i++)
	{
		for (int j=i+1; j<length; j++)
		{
			if (list[i]<list[j])
			{
				temp = list[i];
				list[i]=list[j];
				list[j]=temp;
			}
		}
	}
};

void queryE(Database d, string courseCode)
{
	int f=0;
	for (int i=0; i<d.numberOfEnrollRecords; i++)
	{
		if (d.enrollRecords[i].code==courseCode)
		{
			f++;
		}
	}
	double list[f];
	int n=0;
	for (int l=0; l<d.numberOfEnrollRecords; l++)
	{
		if (d.enrollRecords[l].code==courseCode)
		{
			list[n]=d.enrollRecords[l].exam;
			n++;
		}
	}
	
	sorter(list, f);

	for (int r=0; r<f;r++)
	{
		for (int s=0;s<d.numberOfEnrollRecords;s++)
		{
			if ((d.enrollRecords[s].code==courseCode) && (d.enrollRecords[s].exam == list[r]))
				cout << d.enrollRecords[s].uid << " ";
		}
		cout << list[r] << endl;
	}
};