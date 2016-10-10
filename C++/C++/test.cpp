#include <iostream>
using namespace std;

int main (){
int count = 0;
for (int i=0; i<5;i++)
{
	if (i==1)
		continue;
	if (i==4)
		break;

	cout << i << "   " << count <<endl;
	count++;
}
	// char target[5] = {'r','b','p','p','p'};
	// char guess;
	// int i = 0;
	// int a = 10; int b =20; int c = 30;
	// c *= ++a +b--;
	// cout << a << "\n\n" <<b<<endl<<c;
	// if (a>0 && b<100)
	// 	a++;
	// else 
	// {	a--;
	// 	b--;
	// }
	// cout << a <<endl<<b;
	// for (int i=0; i<5; i++)
	// {	while (guess != target[i])
	// 	{
	// 		cout<<"Guesss the code in target[" << i <<"]";
	// 		cin >> guess;
	// 	}
	// 	guess = 0;
	// }
}