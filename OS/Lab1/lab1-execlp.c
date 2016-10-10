/* Lab 1 - Process
*  lab1-execlp.c
*/

#include <errno.h>
#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int main() {
	char *a[3] = {(char *)"ls", (char *)"-1", (char *)NULL};
	
	printf("execlp: Press <enter> to execute 'ls -l'\n");
	
	getchar();
	
	if (execlp(a[0], a[0], a[1], NULL) == -1) {
		printf("execlp: error no = %s\n", strerror(errno));
		exit(-1);
	}
	
	printf("These lines should never be printed\n");
	return 0;
}
