#include <iostream>
using namespace std;
int main(){
	bool b = true;
	bool b2 = false;
	bool b3 = true;
	b=b2;
	b2=b3;
	b3=false;
	cout<<b<<endl;
	cout<<b2<<endl;
	cout<<b3<<endl;
}