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
			case 4:
				price=15.00;
				break;
			case 5:
				price=8.50;
				break;
			case 6:
				price=9.99;
				break;
				}
	return price;
}

int productSelection()
{
	cout << "1: Chicken ($" <<priceof(1)<<")" << endl;	
	cout << "2: Milk ($" <<priceof(2)<<")" << endl;	
	cout << "3: Chocolate ($" <<priceof(3)<<")" << endl;
	cout << "4: Candy ($" <<priceof(4)<<")" << endl;	
	cout << "5: Water ($" <<priceof(5)<<")" << endl;	
	cout << "6: Coffee ($" <<priceof(6)<<")" << endl;		
	cout << "-->";
	int x=0;
	cin >> x;
	if (x>0 && x<7)
		return x;
	else 
		productSelection();
}

double purchase ()
{
	int q=0;
	double fp =0;
	double prod=0;
	prod = priceof(productSelection());
	cout << "Select Quantity: ";
	cin >> q;
	fp = (q*prod);
	return fp;
}

int main(){
	char sel = 'm';
	double tot=0;
	while (sel =='m')
		{
			tot=purchase()+tot;
			cout << "Press 'm' to add more! --> ";
			cin >> sel;
			cout << endl;			
		}
	cout << tot;
}