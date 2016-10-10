#include <iostream>
#include <cmath>
using namespace std;


void area(int a, int b, int c, double &semi, double &ar)
	{
		semi=(a+b+c)/2;
		double square=(semi)*(semi-a)*(semi-b)*(semi-c);
		ar=sqrt(square);
	}

int main()
{
int a=0;
int b=0;
int c=0;
double semi =0;
double ar =0;
cout << "Enter the lengths of the side A:"<<endl;
cin >> a;
cout << "Enter the lengths of the side B:"<<endl;
cin >> b;
cout << "Enter the lengths of the side C:"<<endl;
cin >> c;
area(a,b,c,semi,ar);
cout << "Area of Triangle = " << ar <<endl;
}