#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>

int main() {
	pid_t myid;
	char str[50];
	FILE * file;
	int z;
	unsigned long long i, x;
	unsigned long h, ut, st;

	/* just run for some duration */

	for (i=0; i<1073741824; i += 2) {
		x *= i;
		if (x > 8589934589)
			x = 1;
	}

	myid = getpid();

	/* get my own procss statistics */
	sprintf(str, "/proc/%d/stat", (int)myid);
	file = fopen(str, "r");
	if (file == NULL) {
		printf("Error in open my proc file\n");
		exit(0);
	}

	fscanf(file, "%d %s %c %d %d %d %d %d %u %lu %lu %lu %lu %lu %lu", &z, str, &str[0], &z, &z, &z, &z, &z,
		(unsigned *)&z, &h, &h, &h, &h, &ut, &st);
	fclose(file);

	printf("The number of clock ticks per second is: %ld\n", sysconf(_SC_CLK_TCK));
	
	printf("User time: %lf s\n", ut*1.0f/sysconf(_SC_CLK_TCK));
	printf("In system time: %lf s\n", st*1.0f/sysconf(_SC_CLK_TCK));

	return 0;
}
