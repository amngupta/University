#include <iostream>
using namespace std;
int main()
{
	int i;
	int tsize;
	int space = 0;
	cout<<"Triangle Size:";
	cin>>tsize;
	int tester=tsize;
	for (i=0; i<=(tsize); i++)
		{
			for (space=(tester); space>=1; space--)
	 		{
	 			cout<<" ";
	 		}
			for (int j=0; j<i; j++)
			{
			cout<<"&";			
			}
			cout<<endl;
			tester--;
		}
	tester=tsize;
	for (int k=0; k<(tsize-1); k++)
		{
			for (int m=0; m<=space; m++){
			cout<<" ";				
			}
			for (int l=(tester-2); l>=0; l--)
			{
			cout<<"&";	
			}
			tester--;
			cout<<endl;	
			space++;
		}
}
