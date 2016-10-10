#include <iostream>
using namespace std;
int main()
{
	int i;
	int tsize;
	int space;
	cout<<"Triangle Size:";
	cin>>tsize;
	int tester=tsize;
	for (i=0; i<tsize; i++)
		{
			for (space=(tester); space>=0; space--)
	 		{
	 			cout<<" ";
	 		}
			for (int j=0; j<=i*2; j++)
			{
			cout<<"&";			
			}
			cout<<endl;
			tester--;
		}
}
