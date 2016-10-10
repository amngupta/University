#include <iostream>
using namespace std;

int main()
{
	int n;
	int temp;
	cin >> n;
	int num[n-2];
	for (int i =0; i < n-2; i++)
	{
		num[i]=i+2;
	} 
	for (int k =0; k<n-2; k++)
	{
		for (int j =2; j<n/2;j++)
		{
			for (int f=2; f<n/2; f++){
			if (num[k]==j*f)
				num[k]=0;
		}}
		if (num[k]==0)
		{
			temp = num[k+1];
			num[k+1]=num[k];
			num[k]=temp;
		}
	
	}
	for (int i =0; i < n-2; i++)
	{
		cout << num[i];
	} 
}