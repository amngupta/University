/**
 * There are a total of five tasks in this file. You may compile and run this code by typing 
 * javac Assignment2.java and java Assignment2, respectively. These commands will only work 
 * if you are in the same folder as the Assignment2.java and ImageProcessing.java. 
 * Make sure you have the JDK installed: http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html
 * Please only submit this file.
 */
public class ImageProcessing {
	/**
	 * Return the intensity of a pixel in image f at location x, y.
	 * This functions has already been implemented for you :-).
	 * Please refer to slide 23 of chapter 6.
	 * You don't need to edit this function.
	 */
	public int getIntensity(int[][] f, int x, int y) {
		System.out.println("DEBUG: getIntensity at ("+x+", "+y+")");
		return f[x][y];
	}
	
	/**
	 * Sets the set of pixels N_8(x, y) to 255 (white intensity).
	 * This functions has already been implemented for you :-).
	 * Please refer to slide 28 of chapter 6.
	 * You don't need to edit this function.
	 */
	public void setEightNeighborsToWhite(int[][] f, int x, int y) {
		System.out.println("DEBUG: setEightNeighborsToWhite at ("+x+", "+y+")");
		f[x-1][y-1] = 255;
		f[x-1][y+1] = 255;
		f[x+1][y+1] = 255;
		f[x+1][y-1] = 255;
		f[x-1][y] = 255;
		f[x][y-1] = 255;
		f[x+1][y] = 255;
		f[x][y+1] = 255;
	}

	/**
	 * Changes ever pixel in the image to 0 (black) or 255 (white). If a pixel in the 
	 * image f has intensity less then or equal to k it well be set to 0 otherwise to 255. 
	 * The threshold k will automatically be selected based on the intensity value of the pixel 
	 * below the mouse pointer. 
	 * Please refer to slides 28 and 37 of chapter 6.
	 */
	public void threshold(int[][] f, int k) {
		System.out.println("DEBUG: threshold with k="+k);
		int M = f.length;
		int N = f[0].length;
		//Task 1: Your code here :)
		for (int i =0; i<M; i++)
		{
			for (int il =0; il<N; il++)
			{
				if (f[i][il] <= k)
				{
					f[i][il] = 0;
				}
				else 
				{
					f[i][il] = 255;
				}
			}
		}
	}

	/**
	 * Performs the image negative transformation.
	 * Please refer to slides 28 and 39 of chapter 6.
	 */
	public void negative(int[][] f) {
		System.out.println("DEBUG: negative");
		int M = f.length;
		int N = f[0].length;
		int L = 256;
		//Task 2: Your code here :)
		for (int i =0; i<M; i++)
		{
			for (int il =0; il<N; il++)
			{
				f[i][il]=L-1-f[i][il];
			}
		}

	}

	/**
	 * Returns the histogram of the image f, i.e., an array of size L=256. The array at element i
	 * will equal to the number of occurrence of gray level intensity i, 0 <= i <= 255.
	 * Please refer to slide 50 of chapter 6.
	 */
	public int[] histogram (int[][] f) {
		System.out.println("DEBUG: histogram");
		int M = f.length;
		int N = f[0].length;
		int[] histogram = new int[256];
		//Task 3: Edit the following code :)
		for (int i =0; i<M; i++)
		{
			for (int il =0; il<N; il++)
			{
				int hist = f[i][il];
				histogram[hist]++;
			}
		}
		return histogram;
	}

	/** 
	 * Apply the box smoothing filter on the image f.
	 * Please refer to slide 82 of chapter 6.
	 * You don't need to edit this function.
	 */
	public void boxSmoothFilter(int[][] f, int filterSize) {
		System.out.println("DEBUG: boxSmoothFilter with filterSize="+filterSize);
		double filter[][] = new double[filterSize][filterSize];
		int M = f.length;
		int N = f[0].length;
		for (int i=0;i<filterSize;i++)
			for (int j=0;j<filterSize;j++)
				filter[i][j] = 1.0/(filterSize*filterSize);
		convolve(f, filter);
	}

	/** 
	 * Applt the Laplacian filter (isotropic mask for rotations in increments of 45 deg) on image f.
	 * Please refer to slide 90 of chapter 6.
	 * You don't need to edit this function.
	 */
	public void laplacianFilter(int[][] f) 
	{
		System.out.println("DEBUG: laplacianFilter");
		double [][] filter = new double [][]{{-1,-1,-1},{-1,8,-1},{-1,-1,-1}};
		convolve(f, filter);
	}


	// private void laplicancon(int[][] f, double[][] filter)
	// {
	// 	int M = f.length;
	// 	int N = f[0].length;
	// 	int[][] copy = new int[f.length][f[0].length];
	// 	for (int i = 0; i < f.length; i++)
 //  		   System.arraycopy(f[i], 0, copy[i], 0, f[0].length);
	// 	for (int x = 1; x<M-1; x++)
	// 	{
	// 		for (int y = 1; y<N-1; y++)
	// 		{
	// 			f[x][y] = copy[x-1][y-1]*-1 + copy[x-1][y]*-1 + copy[x-1][y+1]*-1 + copy[x][y-1]*-1 +
	// 			copy[x][y]*8 + copy[x][y+1]*-1 + copy[x+1][y-1]*-1 + copy[x+1][y]*-1 + copy[x+1][y+1]*-1;
	// 		}
	// 	}
	// }

	/** 
	 * Performs convolution on the image f with the mask filter. Please make sure
	 * that all values in the image array f are within the bound [0, 255]. You may ignore the boundary, 
	 * i.e. no need to change the pixel intensities at the boundary. You may assume that the filter
	 * is symmetric, i.e., convolution and correlation give the same result.
	 */
	private void convolve(int[][] f, double[][] filter) {
		int filterSize = filter.length;
		int M = f.length; //height
		int N = f[0].length; //width
		int[][] copy = new int[f.length][f[0].length];
		for (int i = 0; i < f.length; i++)
  		   System.arraycopy(f[i], 0, copy[i], 0, f[0].length);
    	//Task 4: Your code here :)
		for (int i = 0; i<(M-filterSize); i++)
		{
			for (int il =0; il<(N-filterSize); il++)
			{
				double sum = 0;
				for (int j=0; j<filterSize; j++)
				{
					for(int jl=0; jl<filterSize; jl++)
					{
						int y = i+j;
						int x = il+jl;
						sum += copy[y][x]*filter[j][jl];
					}
				}
			
			if (sum>255)
				sum = 255;
			if (sum<0)
				sum = 0;
			
			f[i+filterSize/2][il+filterSize/2] = (int)Math.round(sum);
			}
		};
		// int half = filterSize/2;
		// int negative = -half;
		// for (int i = half; i<M-half; i++)
		// {
		// 	for (int il = half; il<N-half; il++)
		// 	{
		// 		int x = i;
		// 		int y = il;
		// 		int sum = 0;
		// 		for (int a = 0; a<filterSize; a++)
		// 		{
		// 			for (int b = 0; b<filterSize; b++)
		// 			{
		// 				sum += filter[a][b]*copy[i+negative+a][il+negative+b];
		// 			}
		// 		}
		// 	f[x][y]= sum;
		// 	}
		// }
	}
	
	/**
	 * Apply the median filter to the image f.
	 * The java function java.util.Arrays.sort(array) can be used to sort an array.
	 * Please refer to slide 84 of chapter 6.
	 */
	public void medianFilter(int[][] f, int filterSize) {
		System.out.println("DEBUG: medianFilter with filterSize="+filterSize);
		int M = f.length; //height
		int N = f[0].length; //width
		int[][] copy = new int[f.length][f[0].length];
		for (int i = 0; i < f.length; i++)
    		System.arraycopy(f[i], 0, copy[i], 0, f[0].length);
		int[] filter = new int[filterSize*filterSize];
		int middle = (filterSize*filterSize)/2;
		//Task 5: Your code here :)
		for (int i = 0; i<(M-filterSize); i++)
		{
			for (int il =0; il<(N-filterSize); il++)
			{
				int test = 0;
				for (int j=0; j<filterSize; j++)
				{
					for(int jl=0; jl<filterSize; jl++)
					{
						filter[test] = copy[i+j][il+jl];
						test++;
					}
				}
				java.util.Arrays.sort(filter);
				f[i+filterSize/2][il+filterSize/2] = filter[middle];
			}
		};
	}
}