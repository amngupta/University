/*
    part1a-mandelbrot.c 
    GUPTA Aman
    3035206885    
    30/10/2017 v1.1
    Ubuntu 16.04 | GCC 5.4 compiler
    gcc  part1a-mandelbrot.c  -o part1a-mandelbrot  -l SDL2  -l m    
*/

//Using SDL2 and standard IO
#include <stdio.h>
#include <stdlib.h>
#include <sys/time.h>
#include <time.h>
#include <sys/resource.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <unistd.h>
#include "Mandel.h"
#include "draw.h"

int CHILD_PROCESSES = 10;
#define false 0
#define true 1
typedef int bool; 
#define READ 0   // Read end of pipe
#define WRITE 1  // Write end of pipe
#define STATUS 0

struct timespec timespec;

typedef struct ProcessObj{
    bool isMain;
    struct timespec start_compute;
    struct timespec end_compute;
    float difftime;
    pid_t process_id;    
} ProcessObj;

typedef struct message {
    int row_index;
    float rowdata[IMAGE_WIDTH];
} MSG;

void newProcessObject(ProcessObj * obj, pid_t id){
    obj->process_id = id;
}

void setStartTime(ProcessObj *process){
    struct timespec start;
    process->isMain ? printf("Start the computation ...\n") : 
        printf("Child(%d) : Start the computation ...\n", process->process_id);    
    clock_gettime(CLOCK_MONOTONIC, &start);
    process->start_compute = start;
}

void setEndTime(ProcessObj *process){
    struct timespec end;
    clock_gettime(CLOCK_MONOTONIC, &end);
    process->end_compute = end;
}

float computeDiffTime(struct timespec startTime, struct timespec endTime){
	return (endTime.tv_nsec - startTime.tv_nsec)/1000000.0 + (endTime.tv_sec - startTime.tv_sec)*1000.0;
}

float timeValToFloat(struct timeval val){
    return val.tv_sec*1000 + val.tv_usec/1000;
}

void printElapsedTime(ProcessObj *process){
    process->isMain ? printf(" ... completed. Elapse time = %.3f ms\n", process->difftime) : 
        printf("Child(%d) : ... completed. Elapse time = %.3f ms ...\n", process->process_id,process->difftime);           
}

void getUsageInfo(){
    struct rusage self, children;
    getrusage(RUSAGE_SELF, &self);
    getrusage(RUSAGE_CHILDREN, &children);
    printf("Total time spent by all child processes in user mode = %.3f ms\n", timeValToFloat(children.ru_utime));
    printf("Total time spent by all child processes in system mode =%.3f ms\n", timeValToFloat(children.ru_stime));
    printf("Total time spent by parent process in user mode = %.3f ms\n", timeValToFloat(self.ru_utime));
    printf("Total time spent by parent process in system mode = %.3f ms\n", timeValToFloat(self.ru_stime));
}


int main( int argc, char* args[] ){
    
    if( argc == 2 ) {
        CHILD_PROCESSES = atoi(args[1]);
    }

    struct timespec start_time, end_time;
    clock_gettime(CLOCK_MONOTONIC, &start_time);
    int pid[2];
    ProcessObj main;
    newProcessObject(&main, getpid());
    main.isMain = true;
    float * pixels;

    pixels = (float *) malloc(sizeof(float) * IMAGE_WIDTH * IMAGE_HEIGHT);
	if (pixels == NULL) {
		printf("Out of memory!!\n");
		exit(1);
	}
    setStartTime(&main);
    pipe(pid);    
    int rowsPerChild = IMAGE_HEIGHT / CHILD_PROCESSES;
    for(int childCnt = 1; childCnt <= CHILD_PROCESSES; childCnt ++ )   {
        if( fork() == 0 )  {
            ProcessObj child;
            newProcessObject(&child, getpid());
            child.isMain = false;
            setStartTime(&child);
            close(pid[READ]); // Close unused end            
            MSG message;  
            float temp[IMAGE_WIDTH];          
            for (int y=((childCnt - 1) * rowsPerChild); y<childCnt * rowsPerChild; y++) {
                message.row_index = y;
                for (int x=0; x<IMAGE_WIDTH; x++)
                    temp[x] = Mandelbrot(x, y);
                memcpy(message.rowdata, temp, sizeof temp);                    
                int msgSize = sizeof message;                 
                if (write(pid[WRITE], &message, sizeof(message)) <= 0)
                    printf("Child: Encountered write error\n");
                memset(temp,0,sizeof(temp));            
            }
            setEndTime(&child);
            child.difftime = computeDiffTime(child.start_compute, child.end_compute);
            printElapsedTime(&child);
            close(pid[WRITE]);  
            exit(0);          
            // it's the parent's loop, children do not iterate here
            break;
        }
        continue;
    }
    if (main.process_id == getpid()) {
        pid_t wpid;
        close(pid[WRITE]); // Close unused end        
        MSG current;
        int bytes;
        while ((bytes = read(pid[READ], &current, sizeof(current)))> 0)   {
            // int bytes = read(pid[READ], &current, sizeof(&current));
            int currentRowIndex = current.row_index;
            for (int j = 0; j <IMAGE_WIDTH; j++)
                pixels[currentRowIndex*IMAGE_WIDTH+j] = current.rowdata[j];
        }
        while ((wpid = wait(STATUS)) > 0);        // perform after all child process end
        printf("All Child processes have completed \n");
        float difftime;

        close(pid[READ]);
        setEndTime(&main);
        main.difftime = computeDiffTime(main.start_compute, main.end_compute);

        //Report
        clock_gettime(CLOCK_MONOTONIC, &end_time);
        difftime = computeDiffTime(start_time, end_time);
        getUsageInfo();        
        printf("Total elapse time measured by the parent process = %.3f ms\n", difftime);
        printf("Draw the image\n");
        //Draw the image by using the SDL2 library
        DrawImage(pixels, IMAGE_WIDTH, IMAGE_HEIGHT, "Mandelbrot demo", 3000);       
    }
	return 0;
}