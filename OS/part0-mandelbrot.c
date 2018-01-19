/* 2017-18 Programming Project
	part0-mandelbrot.c */

//Using SDL2 and standard IO
#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <sys/types.h>
#include <unistd.h>
#include "Mandel.h"
#include "draw.h"

int main( int argc, char* args[] )
{
	//data structure to store the start and end times of the whole program
	struct timespec start_time, end_time;
	//get the start time
	clock_gettime(CLOCK_MONOTONIC, &start_time);
	
	//data structure to store the start and end times of the computation
	struct timespec start_compute, end_compute;
	
	//generate mandelbrot image and store each pixel for later display
	//each pixel is represented as a value in the range of [0,1]
	
	//store the 2D image as a linear array of pixels (in row-major format)
	float * pixels;
	
	//allocate memory to store the pixels
	pixels = (float *) malloc(sizeof(float) * IMAGE_WIDTH * IMAGE_HEIGHT);
	if (pixels == NULL) {
		printf("Out of memory!!\n");
		exit(1);
	}
	
	//compute the mandelbrot image
	//keep track of the execution time - we are going to parallelize this part
	printf("Start the computation ...\n");
	clock_gettime(CLOCK_MONOTONIC, &start_compute);
    int x, y;
	float difftime;
    for (y=0; y<IMAGE_HEIGHT; y++) {
    	for (x=0; x<IMAGE_WIDTH; x++) {
			//compute a value for each point c (x, y) in the complex plane
    		pixels[y*IMAGE_WIDTH+x] = Mandelbrot(x, y);
    	}
   	}
	clock_gettime(CLOCK_MONOTONIC, &end_compute);
	difftime = (end_compute.tv_nsec - start_compute.tv_nsec)/1000000.0 + (end_compute.tv_sec - start_compute.tv_sec)*1000.0;
	printf(" ... completed. Elapse time = %.3f ms\n", difftime);

	//Report timing
	clock_gettime(CLOCK_MONOTONIC, &end_time);
	difftime = (end_time.tv_nsec - start_time.tv_nsec)/1000000.0 + (end_time.tv_sec - start_time.tv_sec)*1000.0;
	printf("Total elapse time measured by the process = %.3f ms\n", difftime);
	
	printf("Draw the image\n");
	//Draw the image by using the SDL2 library
	DrawImage(pixels, IMAGE_WIDTH, IMAGE_HEIGHT, "Mandelbrot demo", 3000);
	
	return 0;
}

