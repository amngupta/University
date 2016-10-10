#include <iostream>
using namespace std;
int main()
{
	int i;
	int tsize;
	int space = 1;
	cout<<"Triangle Size:";
	cin>>tsize;
	int tester=tsize;
	for (i=0; i<tsize; i++)
		{
			for(int k=0; k<space ; k++)
			{
				cout<<" ";
			}
			for (int j=((tester*2)-1); j>0; j--)
			{
				cout<<"&";		
			}
		cout<<endl;
		tester--;
		space++;
		}
}
