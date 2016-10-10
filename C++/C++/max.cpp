#include <iostream>
using namespace std;
int main () {
	int a;
	int b;
	int c;
	cin>>a;
	cin>>b;
	cin>>c;
	if (a>b)
		{
			if (a>c)
				cout<<a;
			else 
				cout<<c;
		}
	else 
		{
			if (b>c)
				cout<<b;
			else 
				cout<<c;
		}

}