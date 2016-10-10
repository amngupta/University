/* Lab 1 - Process
*  lab1-01.c
*/

#include <stdio.h>
#include <unistd.h>
#include <sys/types.h>

int main() {

	printf("Process (%d) starts up\n", (int)getpid());
	
	fork();
	
	printf("My ID is %d\n", (int)getpid());
	
	return 0;
}
