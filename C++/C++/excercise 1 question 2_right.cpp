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
	for (i=0; i<=(tsize); i++)
		{
			for (int j=0; j<i; j++)
			{
			cout<<"&";			
			}
			cout<<endl;
		}
	for (int k=0; k<(tsize-1); k++)
		{
			for (int l=(tester-2); l>=0; l--)
			{
			cout<<"&";	
			}
			tester--;
			cout<<endl;	
		}
}
