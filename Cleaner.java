/**
 *  File: Cleaner.java 
 *  Author: Li Jingwei
 *  Date: September 24, 2015
 *  
 *  Description: This program identifies edges in an image, 
 *  which is how we (humans) delineate objects that we see.
 */

import java.util.Scanner;

/**
 *  This is the main (only) class of the program and contains all program code.
 *  The program starts running in the main() of this class.
 */
public class Cleaner 
{
	// Define the number for counting the times artifact removed.
	static int arn = 0;
	
	// Define the artifact coordinates' extreme value.
	static int xmin,xmax,ymin,ymax;
	
	/**
	 * This method is called when the program starts running.
	 * It reads in a black-and-white image that was generated 
	 * by the Detector and removes all artifacts that are no larger than at most 2 × 2 pixels.
	 * The program should also print out the number of artifacts removed.
	 */
	public static void main(String[] args) 
	{
		// Instantiate an ImageRW.
		ImageRW image = new ImageRW();
						
		// Instantiate new scanner to read from the console.
		Scanner keyboard = new Scanner(System.in);
						
		// Read in a string from the console denoting the name of the image file.
		String filename1 = keyboard.next();
				
		// Read in a string from the console denoting the name of the output image file.
		String filename2 = keyboard.next();

		// Load the image.
		image.load( filename1 );
				
		// Get the height and the width of the image.
		int height = image.getHeight();
		int width = image.getWidth();
		
		// Define a 2D array to store the pixel data.
		int[][] tda = new int[height][width];
		
		// Loop height times, reading in a number of the coordinate at a time.
		for ( int ln1 = 0; ln1 < height; ln1++)
			// Loop width times, reading in another number of the coordinate at a time.
			for ( int ln2 = 0; ln2 < width; ln2++)
			{
				// Define the pixel array.
				int [] rgb = new int[3];
						
				// Get the data of one individual pixel.
				image.getPixel( ln1, ln2, rgb);
						
				// Assign the data into 2D array.
				tda[ln1][ln2] = rgb[0];			
			}
		
		// Loop height times, reading in a number of the coordinate at a time.
		for ( int ln1 = 0; ln1 < height; ln1++)
			// Loop width times, reading in another number of the coordinate at a time.
			for ( int ln2 = 0; ln2 < width; ln2++)				
			{
				// Initialize the extreme values of height and width.
				xmin = ln1;
				xmax = ln1;
				ymin = ln2;
				ymax = ln2;
				
				// Get the data for judging artifacts in the function.
				deter( ln1, ln2, tda, height, width);
				
				// Check the artifact.
				if ( xmax - xmin < 2 && ymax - ymin < 2 && tda[ln1][ln2] == 255 )
				{
					// Remove the artifacts.
					for ( int ln3 = xmin; ln3 <= xmax; ln3++)
						for ( int ln4= ymin; ln4 <= ymax; ln4++)
							tda[ln3][ln4] = 0;
					
					// Calculate the number of artifact removed.
					arn++;
				}	
			}

		// Loop height times, reading in a number of the coordinate at a time.
		for ( int ln1 = 0; ln1 < height; ln1++)
			// Loop width times, reading in another number of the coordinate at a time.
			for ( int ln2 = 0; ln2 < width; ln2++)
			{
				// Define the pixel array.
				int [] rgb = new int[3];
				
				// Store the number in the array.
				rgb[0] = tda[ln1][ln2];
				rgb[1] = tda[ln1][ln2];
				rgb[2] = tda[ln1][ln2];
			
				// Set the data into the corresponding pixel.
				image.setPixel( ln1, ln2, rgb);
			}
		
		// Save the image in the new file.
		image.save( filename2 );
		
		// 	Print the number of artifact moved.
		System.out.println( arn );
	}

	// The function for determining the extreme values of height and width.
	public static void deter( int x, int y, int[][] tda, int h, int w)
	{		
		// Check if the pixel is white.
		if ( tda[x][y] == 255 )
		{
			// Change the number to determine this pixel has been checked.
			tda[x][y] = 254;
			
			// Compare the pixel with the extreme value and get the extreme value.
			if ( xmin > x )
				xmin = x;
			else if ( xmax < x )
				xmax = x;
			
			if ( ymin > y )
				ymin = y;
			else if ( ymax < y )
				ymax = y;
			
			// Check if the white pixels still meet the artifact requirement.
			if ( xmax - xmin < 2 && ymax - ymin < 2 )
			{
				// Check the surrounding pixel.
				for ( int ln1 = x - 1; ln1 <= x + 1; ln1++)
					for ( int ln2 = y - 1; ln2 <= y + 1; ln2++)
					{
						if ( ln1 >= 0 && ln1 < h && ln2 >= 0 && ln2 < w )
							deter( ln1, ln2, tda, h, w);
					}
			}	
			
			// Restore the color of the white pixel.
			tda[x][y] = 255;
		}
	}
}