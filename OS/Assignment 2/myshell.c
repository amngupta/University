#include <errno.h>
#include <unistd.h>
#include <stdio.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <stdlib.h>
#include <string.h>
#include <signal.h>

#define MAX_BUFFER 1024

char lineInput[MAX_BUFFER];


// void sigChild() {
// 	printf("\n ## myshell $");
// }

void sigIgnore(){
	putchar('\n');
	printf("## myshell $ ");
}

void error(char msg[MAX_BUFFER]) {
	printf("%s.\n", msg);
} 	


int main() {
	printf("## myshell $ ");
	signal(SIGINT, SIG_IGN);
    while (fgets(lineInput, MAX_BUFFER, stdin) != NULL)
    {
        if (strcmp(lineInput, "exit\n") == 0){
        	printf("myshell: Terminated\n");
            exit(EXIT_SUCCESS);
        }
        else if (strcmp(lineInput, "\n") == 0){
			printf("## myshell $ ");
        }
		else{	
	        char *args[64];
	        char *amp = "&";
	        char **next = args;
	        char *temp = strtok(lineInput, " \n");
	        int count = 0;
	        while (temp != NULL)
	        {
	            *next++ = temp;
	            count++;
	            temp = strtok(NULL, " \n");
	        }
	        *next = NULL;
	    	pid_t pid = fork();
	    	if (pid == 0) {
				if (execvp(args[0], args) == -1) {
					error("execvp: error");
					exit(-1);
				}

			}
			else{
				pid_t child_pid;
				int status;
		        if (strcmp(amp,args[count-1]) != 0)
					child_pid = waitpid(pid, &status, 0);
				error("Child process exited");
				printf("## myshell $ ");
			}
		}
    }
    return EXIT_SUCCESS;
}
