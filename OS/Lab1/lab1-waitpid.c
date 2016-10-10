/* Lab 1 - Process
*  lab1-waitpid.c
*/

#include <errno.h>
#include <unistd.h>
#include <stdio.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <stdlib.h>
#include <string.h>

int main() {
	int i, ret;
for (i=0; i<4; i++) {
fork();
ret = waitpid(-1, NULL, 0);
if (ret > 0)
execlp("ps", "ps", NULL);
printf("Process ID: %d\n", getpid());
}
printf("Process ID: %d\n", getpid());
	
	return 0;
}
