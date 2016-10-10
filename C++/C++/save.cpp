#include <iostream>
using namespace std;
int main(){
	int i;
	for (i=0; i<10; i++)
	{for (int j=0; j<=i; j++)
		{
			cout<<"*"<<i+j;
		}
		cout<<endl;
	}
}