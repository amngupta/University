/*
    part2-mandelbrot.c 
    GUPTA Aman
    3035206885    
    30/10/2017 v1.2
    Ubuntu 16.04 | GCC 5.4 compiler
    gcc  part2-mandelbrot.c -pthread -o part2-mandelbrot  -l SDL2  -l m    
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
#include <pthread.h>
#include <semaphore.h>


// Defining bool type
#define false 0
#define true 1
typedef int bool; 


//Default Values
int WORKERS_NUMBER = 10;
int ROWS_ALLOCATED = 10;
int BUFFER_SIZE = 10;

struct timespec timespec;

// Thread Sync and Mutex Variables 
pthread_mutex_t pool_lock;
sem_t full;     
sem_t empty;     

typedef struct task {  
    int start_row;  
    int num_of_rows;
    int taskNumber; 
} TASK;

TASK * buffer; // shared Task Buffer
int in = 0; 
int out = 0;
int num_tasks = 0;
float * pixels; // pixels float array
bool work_completed = false; 
bool * tasksList;  // boolean array to keep a list of tasks completed

float timeValToFloat(struct timeval val){
    return val.tv_sec*1000 + val.tv_usec/1000;
}

float computeDiffTime(struct timespec startTime, struct timespec endTime) {
	float difftime = (endTime.tv_nsec - startTime.tv_nsec)/1000000.0 + (endTime.tv_sec - startTime.tv_sec)*1000.0;
    return difftime;
}

void getUsageInfo() {
    struct rusage self;
    getrusage(RUSAGE_SELF, &self);
    printf("Total time spent by process and its threads in user mode = %.3f ms\n", timeValToFloat(self.ru_utime));
    printf("Total time spent by process and its threads in system mode = %.3f ms\n", timeValToFloat(self.ru_stime));
}

void *producerThread(void *args) 
{
    TASK tempTask;

    for (int i=0; i < num_tasks; i++)
    {
        tempTask.start_row = i*ROWS_ALLOCATED;
        tempTask.num_of_rows = ROWS_ALLOCATED;
        tempTask.taskNumber = i;
        // Wait for empty
        sem_wait(&empty);
        // acquire lock 
        pthread_mutex_lock(&pool_lock);
            // add task
            buffer[in] = tempTask;
            in = (in+1)%BUFFER_SIZE;
        pthread_mutex_unlock(&pool_lock);        
        // Post full
        sem_post(&full);                
    }
    work_completed = true;
    for(int i=0;i<WORKERS_NUMBER;i++) // Send signals to ensure all consumer threads run at least once more.
        sem_post(&full);                
	pthread_exit(NULL);
}

void *consumerThread(void *args) 
{
    TASK task; 
    int tasksCompleted;
    int id = *(int*)args;
    struct timespec start_time, end_time;
    float difftime;
    id--;
    printf("Worker(%d) : Start up. Wait for task!\n", id);
    while(true) {
        sem_wait(&full);         // wait for full 
        pthread_mutex_lock(&pool_lock);         // acquire lock - critical section
            task=buffer[out];
            int startRow = task.start_row;
            int endRow = startRow + task.num_of_rows;
            int taskNumber = task.taskNumber;
            if (!tasksList[taskNumber]) {
                printf("Worker(%d) : Start the computation ... \n", id);
                clock_gettime(CLOCK_MONOTONIC, &start_time);            
                for (int y=startRow; y< endRow; y++) {
                    if (y < IMAGE_HEIGHT)
                        for (int x=0; x<IMAGE_WIDTH; x++)
                            pixels[y*IMAGE_WIDTH+x]  = Mandelbrot(x, y);
                }
                out = (out+1)%BUFFER_SIZE;
                sem_post(&empty);   // Post to free a buffer
                clock_gettime(CLOCK_MONOTONIC, &end_time);
                difftime = computeDiffTime(start_time, end_time);        
                printf("Worker(%d) :  ... completed. Elapse time = %.3f ms \n", id, difftime);        
                tasksCompleted++;
                tasksList[taskNumber] = true;
            }
        pthread_mutex_unlock(&pool_lock);   // end of critical section 
        if (work_completed)
            pthread_exit(&tasksCompleted); // send taskInfo and terminate
    }
}

int main( int argc, char* args[] )
{
    struct timespec start_time, end_time;
    clock_gettime(CLOCK_MONOTONIC, &start_time);

    if( argc == 4 ) {
        WORKERS_NUMBER = atoi(args[1]);
        ROWS_ALLOCATED = atoi(args[2]);
        BUFFER_SIZE = atoi(args[3]);
    }

    num_tasks = IMAGE_HEIGHT / ROWS_ALLOCATED;
    if(IMAGE_HEIGHT % ROWS_ALLOCATED != 0) num_tasks++;
 
    pixels = (float *) malloc(sizeof(float) * IMAGE_WIDTH * IMAGE_HEIGHT);
    if (pixels == NULL) {
		printf("Out of memory!!\n");
		exit(1);
    }

	tasksList = (bool *) malloc(sizeof(bool) * num_tasks);
    if (tasksList == NULL) {
		printf("Out of memory!!\n");
		exit(1);
    }
    memset (tasksList, false, sizeof (bool) * num_tasks);

    buffer = (TASK *) malloc(sizeof(TASK) * BUFFER_SIZE);
	if (buffer == NULL) {
		printf("Out of memory!!\n");
		exit(1);
    }
    
	pthread_t workers[WORKERS_NUMBER];
    pthread_t producer; 
    sem_init(&full, 0, 0);
    sem_init(&empty, 0, BUFFER_SIZE);
    pthread_mutex_init(&pool_lock, NULL);

    pthread_create(&producer, NULL, producerThread, NULL);    // create producer thread

    // create worker threads
    for(int i=0;i<WORKERS_NUMBER;i++)
        pthread_create(&workers[i], NULL, consumerThread, (void *) &i);
        
    pthread_join(producer, NULL);    // wait for producer to terminate

    int *exit_status;    
    for(int i=0;i<WORKERS_NUMBER;i++) {
        pthread_join(workers[i], (void **)&exit_status); // wait for consumers to terminate
        printf("Worker thread %d has terminated and completed %d tasks.\n", i, *exit_status);
    }
    printf("All worker threads have terminated\n");
    float difftime;
    clock_gettime(CLOCK_MONOTONIC, &end_time);
    difftime = computeDiffTime(start_time, end_time);
    getUsageInfo();
    printf("Total elapse time measured by the process = %.3f ms\n", difftime);
    printf("Draw the image\n");
    //Draw the image by using the SDL2 library
    DrawImage(pixels, IMAGE_WIDTH, IMAGE_HEIGHT, "Mandelbrot demo", 3000);
    // freeing memory 
    free(pixels);
    free(tasksList);
    free(buffer);
}