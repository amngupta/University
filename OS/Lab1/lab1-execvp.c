/* Lab 1 - Process
*  lab1-execvp.c
*/

#include <errno.h>
#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int main() {
	char *a[3] = {(char *)"ls", (char *)"-1", (char *)NULL};
	
	printf("execvp: Press <enter> to execute 'ls -l'\n");
	
	getchar();
	
	if (execvp(a[0], a) == -1) {
		printf("execvp: error no = %s\n", strerror(errno));
		exit(-1);
	}
	
	printf("These lines should never be printed\n");
	return 0;
}
