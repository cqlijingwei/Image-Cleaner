/**
 *  File: Detector.java 
 *  Author: Li Jingwei
 *  Date: September 23, 2015
 *  
 *  Description: This program identifies edges in an image, 
 *  which is how we (humans) delineate objects that we see.
 */

import java.util.Scanner;

/**
 *  This is the main (only) class of the program and contains all program code.
 *  The program starts running in the main() of this class.
 */
public class Detector {
	/**
	 * This method is called when the program starts running.
	 * It reads in gray-scale image and generates a
	 * black-and-white image representing the edges in the original image.
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
		
		/** 
		 *  Read in a number from the console denoting the integer 
		 *  representing the threshold to be used for edge detection.
		 */
		int t = keyboard.nextInt();
		
		// Define the difference number and specific color numbers.
		double d = 0;
		double x1, x2, x3;
		
		// Load the image.
		image.load( filename1 );
				
		// Get the height and the width of the image.
		int height = image.getHeight();
		int width = image.getWidth();
		
		// Loop height times, reading in a number of the coordinate at a time.
		for ( int ln1 = 0; ln1 < height; ln1++)
			// Loop width times, reading in another number of the coordinate at a time.
			for ( int ln2 = 0; ln2 < width; ln2++)
			{
				// Define the pixel array.
				int [] rgb = new int[3];
				
				// Judge if parameter plus one is over the range.
				if ( ln1 != height - 1 && ln2 != width - 1 )
				{
					// Get the data of the next x individual pixel.
					image.getPixel( ln1 + 1, ln2, rgb);
				
					// Assign the number into color number parameter.
					x1 = rgb[0];
				
					// Get the data of the next y individual pixel.
					image.getPixel( ln1, ln2 + 1, rgb);
				
					// Assign the number into color number parameter.
					x2 = rgb[0];
				
					// Get the data of the individual pixel.
					image.getPixel( ln1, ln2, rgb);
				
					// Assign the number into color number parameter.
					x3 = rgb[0];
				
					// Calculate the difference number.
					d = Math.pow( Math.pow( x3 - x1, 2) + Math.pow( x3 - x2, 2), 0.5);
				
					// Convert the image data into black-and-white image data.
					if ( d > t )
					{
						rgb[0] = 255;
						rgb[1] = 255;
						rgb[2] = 255;
					}
					else
					{
						rgb[0] = 0;
						rgb[1] = 0;
						rgb[2] = 0;
					}
				}
				else
				{
					// Get the data of the individual pixel.
					image.getPixel( ln1, ln2, rgb);
					
					// Convert the image data into black-and-white image data.
					rgb[0] = 0;
					rgb[1] = 0;
					rgb[2] = 0;
				}
					
				// Set the data into the corresponding pixel.
				image.setPixel( ln1, ln2, rgb);				
			}
		
		// Save the image in the new file.
		image.save( filename2 );
	}
}
