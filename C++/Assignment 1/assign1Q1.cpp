#include <iostream>
using namespace std;
int main ()
{
	int snumber = 0; 		//variable to ask for square size
	cout << "Square size: ";
	cin >> snumber;
	if (snumber>=2 && snumber<21)	//CHANGED >2 TO >= 2
	{
		for (int i=0; i<snumber; i++)
	{
		for (int j=0; j<snumber; j++)
		{
			cout<<"&";
		}
		cout<<endl;
	}
	}
	else cout<<"Square size between 2 and 20 only"<<endl;
}