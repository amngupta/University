#include <iostream>
using namespace std;

double priceof(int x)
{
double price = 0;
switch(x){
case 1:
	price=20.00;
	break;
case 2:
	price= 6.50;	
	break;
case 3:
	price= 10.00;
	break;
}
return price;
}

int main(){
	int id=0;
	cin >> id;
	cout<<endl;
	cout<<priceof(id)<<endl;
}