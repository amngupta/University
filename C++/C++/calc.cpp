#include <iostream>
using namespace std;
int main(){

	int a,b,c,d;
	cout<<"Enter Number 1: ";cin>>a;
	cout<<"Enter Number 2: ";cin>>b;
	cout<<"For addition press 1, subtraction 2, multiplication 3, division 4: ";
	cin>>d;
	if(d==1){
	cout<<"Addition: "<<a+b;
	}
	if(d==2){
	cout<<"Subtraction: "<<a-b;
	}
	if(d==3){
	cout<<"Multiplication: "<<a*b;
	}
	if(d==4){
	cout<<"Division: "<<a/b;
	}
}