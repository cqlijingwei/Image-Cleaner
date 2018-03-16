/**
 *  File: Shader.java 
 *  Author: Li Jingwei
 *  Date: September 22, 2015
 *  
 *  Description: This program ...
 */

import java.util.Scanner;

/**
 *  This is the main (only) class of the program and contains all program code.
 *  The program starts running in the main() of this class.
 */
public class Shader {
	/**
	 * This method is called when the program starts running.
	 * It reads in a color image file, transforms the file to a 
	 * gray-scale image and writes the file to a new image file.
	 */
	public static void main(String[] args) 
	{
		// Instantiate an ImageRW.
		ImageRW image = new ImageRW();
		
		// Instantiate new scanner to read from the console.
		Scanner keyboard = new Scanner(System.in);
		
		// Read in a string from the console denoting the name of the image file.
		String filename1 = keyboard.next();
		
		// Load the image.
		image.load( filename1 );
		
		// Get the height and the width of the image.
		int height = image.getHeight();
		int width = image.getWidth();
		
		// Initialize two loop numbers.
		int ln1 = 0, ln2 = 0;
		
		// Loop height times, reading in a number of the coordinate at a time.
		for ( ln1 = 0; ln1 < height; ln1++)
			// Loop width times, reading in another number of the coordinate at a time.
			for ( ln2 = 0; ln2 < width; ln2++)
			{
				// Define the pixel array.
				int [] rgb = new int[3];
				
				// Get the data of one individual pixel.
				image.getPixel( ln1, ln2, rgb);
				
				// Calculate the number that should be stored in the array.
				int num = ( rgb[0] + rgb[1] + rgb[2] ) / 3;
				
				// Store the number in the array.
				rgb[0] = num;
				rgb[1] = num;
				rgb[2] = num;
				
				// Set the data into the corresponding pixel.
				image.setPixel( ln1, ln2, rgb);				
			}
		
		// Read in a string from the console denoting the name of the new image file.
		String filename2 = keyboard.next();
		// Save the image in the new file.
		image.save( filename2 );
	}
}
