/*
    part1b-mandelbrot.c 
    GUPTA Aman
    3035206885    
    30/10/2017 v1.2
    Ubuntu 16.04 | GCC 5.4 compiler
    gcc  part1b-mandelbrot.c  -o part1b-mandelbrot  -l SDL2  -l m    
*/

#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <sys/types.h>
#include <sys/resource.h>
#include <sys/wait.h>
#include <unistd.h>
#include "Mandel.h"
#include "draw.h"
#include <signal.h>

// Default Values
int CHILD_PROCESSES = 10;
int ROWS_ALLOCATED = 10;

// Defining bool type
#define false 0
#define true 1
typedef int bool; 

bool WAIT_SIGNAL = true;
bool LOOP_SIGNAL = true;
#define READ  0   // Read end of pipe
#define WRITE 1  // Write end of pipe
#define STATUS 0

struct timespec timespec;

// Created a ProcessObject Structure to handle Time Computes
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
    pid_t child_pid; 
} MSG;

typedef struct task {  
    int start_row;  
    int num_of_rows; 
} TASK;

void newProcessObject(ProcessObj * obj, pid_t id){
    obj->process_id = id;
}

void setStartTime(ProcessObj *process) {
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

float timeValToFloat(struct timeval val){
    return val.tv_sec*1000 + val.tv_usec/1000;
}

float computeDiffTime(struct timespec startTime, struct timespec endTime) {
	float difftime = (endTime.tv_nsec - startTime.tv_nsec)/1000000.0 + (endTime.tv_sec - startTime.tv_sec)*1000.0;
    return difftime;
}

void printElapsedTime(ProcessObj *process) {
    process->isMain ? printf(" ... completed. Elapse time = %.3f ms\n", process->difftime) : 
        printf("Child(%d) : ... completed. Elapse time = %.3f ms ...\n", process->process_id,process->difftime);           
}

void incrementChild(int childProcess[][3], int currentChild)
{
    for (int j = 0; j < CHILD_PROCESSES; j++) {
        if (currentChild == childProcess[j][0])
            childProcess[j][1]++;
        if (childProcess[j][1] == ROWS_ALLOCATED) { // Once task is complete
            kill(currentChild, SIGUSR1);
            childProcess[j][1] = 0;
            childProcess[j][2] ++;
        }
    }
}

void killChildren(int childProcess[][3])
{
    for (int i = 0; i < CHILD_PROCESSES; i++)  {
        kill(childProcess[i][0], SIGINT);
    }
}

void sigIntHandler(int sig) {
    printf("Process %d is interrupted by ^C. Bye Bye\n", getpid());                
    LOOP_SIGNAL = false;
}

void sigUsrHandler(int sig) {
    WAIT_SIGNAL = false;
}

void getUsageInfo() {
    struct rusage self, children;
    getrusage(RUSAGE_SELF, &self);
    getrusage(RUSAGE_CHILDREN, &children);
    printf("All Child processes have completed\n");
    printf("Total time spent by all child processes in user mode = %.3f ms\n", timeValToFloat(children.ru_utime));
    printf("Total time spent by all child processes in system mode =%.3f ms\n", timeValToFloat(children.ru_stime));
    printf("Total time spent by parent process in user mode = %.3f ms\n", timeValToFloat(self.ru_utime));
    printf("Total time spent by parent process in system mode = %.3f ms\n", timeValToFloat(self.ru_stime));
}

int main( int argc, char* args[] )
{
    if( argc == 3 ) {
        CHILD_PROCESSES = atoi(args[1]);
        ROWS_ALLOCATED = atoi(args[2]);
    }

    struct timespec start_time, end_time;
    clock_gettime(CLOCK_MONOTONIC, &start_time);
    int task_pipe[2];
    int data_pipe[2];
    ProcessObj main;
    newProcessObject(&main, getpid());
    main.isMain = true;
    float * pixels;

    setStartTime(&main);
    
    pixels = (float *) malloc(sizeof(float) * IMAGE_WIDTH * IMAGE_HEIGHT);
	if (pixels == NULL) {
		printf("Out of memory!!\n");
		exit(1);
	}
    // Register Signal Handlers
    signal(SIGUSR1,sigUsrHandler);
    signal(SIGINT, sigIntHandler);

    // Creating two pipes
    pipe(task_pipe);
    pipe(data_pipe);  
    // Defining MultiDimensional Array Store for keeping count of tasks               
    int childProcess[CHILD_PROCESSES][3];
    for(int childCnt = 1; childCnt <= CHILD_PROCESSES; childCnt ++ ){
        int pid;
        pid_t currentProcess;                    
        if( (pid = fork()) == 0 ) {
            ProcessObj child;
            newProcessObject(&child, getpid());
            child.isMain = false;
            close(data_pipe[READ]);    // close unused end                           
            close(task_pipe[WRITE]);   // close unused end
            TASK task;
            MSG message; 
            currentProcess = getpid();   
            printf("Child(%d) : Start up. Wait for task!\n", currentProcess);
            float temp[IMAGE_WIDTH];   
            int bytes;
            int taskCounter = 0; // initialize task counter for counting tasks completed by a child
            // Infinite Loop broken only by SIGINT from Parent
            while(LOOP_SIGNAL)  {
                if (WAIT_SIGNAL){  // Sleep until SIGUSR1                    
                    sleep(1);
                }
                else {
                    WAIT_SIGNAL = true;      
                    if ((bytes = read(task_pipe[READ], &task, sizeof(task)))> 0) { // Read TASK from pipe                    
                        setStartTime(&child);    
                        int startRow = task.start_row;
                        int endRow = startRow + task.num_of_rows;
                        for (int y=startRow; y< endRow; y++) {
                            message.row_index = y;
                            for (int x=0; x<IMAGE_WIDTH; x++)
                                temp[x] = Mandelbrot(x, y);
                            memcpy(message.rowdata, temp, sizeof temp);                    
                            // Copy PID stuff
                            memcpy(&message.child_pid, &currentProcess, sizeof currentProcess);
                            int msgSize = sizeof message;                 
                            memset(temp,0,sizeof(temp));     
                            // Write MESSAGE
                            if (write(data_pipe[WRITE], &message, sizeof(message)) <= 0)
                                printf("Child: Encountered write error\n");         
                        }
                        setEndTime(&child);
                        child.difftime = computeDiffTime(child.start_compute, child.end_compute);
                        printElapsedTime(&child);  
                        taskCounter++;
                    }
                }
            }
            // Close pipe ends
            close(task_pipe[READ]);  
            close(data_pipe[WRITE]);
            exit(taskCounter); // exit with status as the the taskCounter
            break;
        }
        else{
            childProcess[childCnt-1][0] = pid; // Process ID
            childProcess[childCnt-1][1] = 0; // Count of Rows 
            childProcess[childCnt-1][2] = 0; // Count of Tasks
            kill(pid, SIGUSR1); // Initial SIGUSR1 signal 
        }
    }
    if (main.process_id == getpid()){
        pid_t wpid;
        close(task_pipe[READ]); // Close unused end        
        close(data_pipe[WRITE]); // Close unused end        
        TASK tempTask;
        // writing tasks
        for (int i = 0; i < (IMAGE_HEIGHT / ROWS_ALLOCATED); i++)
        {
            tempTask.start_row = i*ROWS_ALLOCATED;
            tempTask.num_of_rows = ROWS_ALLOCATED;
            if (write(task_pipe[WRITE], &tempTask, sizeof(tempTask)) <= 0)
                printf("Parent Task Pipe: Encountered write error\n"); 
        }
        // writing last task
        if (IMAGE_HEIGHT % ROWS_ALLOCATED != 0)
        {
            tempTask.start_row = (IMAGE_HEIGHT / ROWS_ALLOCATED) * ROWS_ALLOCATED;
            tempTask.num_of_rows = IMAGE_HEIGHT % ROWS_ALLOCATED;
            if (write(task_pipe[WRITE], &tempTask, sizeof(tempTask)) <= 0)
                printf("Parent Task Pipe: Encountered write error\n");
        }  
        
        printf("Start collecting the image lines\n");                
        bool completed = false;
        MSG current;
        int counter = 0; 
        int bytes;   
        while(!completed)  {
            // read message to current
            if (read(data_pipe[READ], &current, sizeof(current)) > 0) {
                int currentRowIndex = current.row_index;
                for (int j = 0; j <IMAGE_WIDTH; j++)
                    pixels[currentRowIndex*IMAGE_WIDTH+j] = current.rowdata[j];
                counter++;    
                int currentChildId = current.child_pid;
                // Update values for Child Process in Parent Memory
                incrementChild(childProcess, currentChildId);
            }
            if (counter == IMAGE_HEIGHT)
                completed = true;
        }
        float difftime;
        // Killing all children
        killChildren(childProcess);
        close(task_pipe[WRITE]);
        close(data_pipe[READ]);
        int cstatus;
        while ((wpid = wait(&cstatus)) > 0)
        {
            printf("Child process %d terminated and completed %d tasks\n", wpid, WEXITSTATUS(cstatus));            
        };        
        // perform after all child process end        
        setEndTime(&main);
        main.difftime = computeDiffTime(main.start_compute, main.end_compute);
        //Report
        clock_gettime(CLOCK_MONOTONIC, &end_time);
        difftime = computeDiffTime(start_time, end_time);
        getUsageInfo();        
        printf("Total elapse time measured by the process = %.3f ms\n", difftime);
        printf("Draw the image\n");
        //Draw the image by using the SDL2 library
        DrawImage(pixels, IMAGE_WIDTH, IMAGE_HEIGHT, "Mandelbrot demo", 3000);       
    }
    return 0;
}