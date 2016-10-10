#include <iostream>
using namespace std;

int productSelection()
{
	cout << "1: Chicken ($20.00)" << endl;	
	cout << "2: Milk ($6.50)" << endl;	
	cout << "3: Chocolate ($10.00)" << endl;	
	cout << "-->";
	int x=0;
	cin >> x;
	if (x>0 && x<4)
		return x;
	else 
		productSelection();
}

int main(){
	cout << productSelection();
}