/* Lab 1 - Process
*  lab1-02.c
*/

#include <stdio.h>
#include <unistd.h>
#include <sys/types.h>

int main() {
	pid_t who;
	int var = 999;
	
	printf("Process (%d) starts up\n", (int)getpid());
	who = fork();
	
	if (who == 0) {
		sleep(1);  //force the child to sleep first
		printf("The variable has value: %d\n", var);
		var -= 100;
		printf("The variable becomes: %d\n", var);
	} else {
		printf("The variable has value: %d\n", var);
		var += 100;
		sleep(3);  //force parent to sleep
		printf("The variable becomes: %d\n", var);
	}
	printf("My ID is %d\n", (int)getpid());
	
	return 0;
}
