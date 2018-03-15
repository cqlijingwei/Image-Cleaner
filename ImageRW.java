/**
   File: ImageRW.java
   Author: Alex Brodsky
   Date: August 31, 2015

   Description: This class provides simple funcationality to 
                load, save, and manipulate the pixels of an image.
*/

import java.util.*;
import javax.imageio.ImageIO;
import java.io.*;
import java.awt.image.*;

/**
   This is the publlic (only) class that contains all code of ImageRW.
   The class has an implicity default constructor.
*/
public class ImageRW {
  BufferedImage image;                  // The curremt loaded image.

  /**
     This method loads an image from a file specified by the filename.
     If the file has a .txt extension, it has the following human 
     readable format:
       A sequence of integers separated by white space.
       The first two integers, W and H, denote the image width and height 
       The remaining W * H * 3 integers are in the range 0 - 255
       and represent H rows of W pixels each where each pixel is 
       represented by 3 integers: the red, green, and
       blue components of the pixel.  The pixels are stored in row major 
       order, i.e., the firts pixel is at row 1, column 1, the second at
       row 1, column 2, etc ...
     THe method first reads in W and wnd H, creates a blank image, and 
     then reads each pixel and sets it.
      
     If the ending is not .txt, the nethod uses Java's ImageIO API to load
     the image.

     Parameters: filename : name of the file from which to load the image 
     Returns:    true on success and false otherwise
  */ 
  public boolean load( String filename ) {
    try {
      // Initialize image variable to null, meaning no image loaded.
      image = null;

      // Test if file ends in .txt
      if( filename.endsWith( ".txt" ) ) {
        // If so, create a Scanner to read the file
        // Load image's dimentions.
        Scanner scan = new Scanner( new File( filename ) );
        int width = scan.nextInt();
        int height = scan.nextInt();

        // Create a new blank image.
        // Load each pixel, in row major order.
        image = new BufferedImage( width, height, BufferedImage.TYPE_INT_ARGB );
        for( int i = 0; i < height; i++ ) {
          for( int j = 0; j < width; j++ ) {
            // Read in 3 values per pixel and combine into Pack Integer Format
            int c = 0xff; // Set alpha to maximum.
            for( int k = 0; k < 3; k++ ) {
              c <<= 8;
              c |= scan.nextInt() & 0xff;
            }

            // Set the resulting pixel in the image.
            image.setRGB( j, i, c );
          }
        }

        // Close the file when done.
        scan.close();
      } else {
        // If file does not end in .txt use the ImageIO API to load the image
        image = ImageIO.read( new File( filename ) );
      }
    } catch ( IOException e ) {
      // If an error occurred during loading display the error.
      System.out.println( e );
      image = null;
    }

    //Retirn true if image was successfully loaded.
    return image != null;
  }


  /**
     This method saves an image to a file specified by the filename.
     If the file has a .txt extension, it has the human readable format
     described in the load() method.  In this case, the method first 
     opens the file, writes the width and height, and then writes each 
     pixel.
      
     If the ending is not .txt, the nethod uses Java's ImageIO API to save
     the image.

     Parameters: filename : name of the file to which to save the image 
     Returns:    true on success and false otherwise
  */ 
  public boolean save( String filename ) {
    try {
      // Check if image is present, and if it is, check it's ending
      if( image == null ) {
        // If image is not present, print out an error message and return.
        System.out.println( "No image loaded" );
        return false;
      } else if( filename.endsWith( ".txt" ) ) {
        // If file is a .txt file, open the file and write the width and height
        // of the image to it.
        PrintStream out = new PrintStream( new File( filename ) );
        int width = image.getWidth();
        int height = image.getHeight();
        out.println( width + " " + height );

        // Iterate over the pixels in row major order and writ eeach one to
        // the file.
        for( int i = 0; i < height; i++ ) {
          for( int j = 0; j < width; j++ ) {
            // Get the pixel from the image, decode it, and write the components
            // to the files: red, green, blue, all components alpha adjusted
            int c = image.getRGB( j, i );
            int alpha = ( c >> 24 ) & 0xff;
            for( int k = 2; k >= 0; k-- ) {
              out.print( alpha * ( ( c >> ( k * 8 ) ) & 0xff ) / 255 + " " );
            }
          }
          // Terminate each row with a new-line for readability purposes.
          out.println( "" );
        }

        // Close the file and return true.
        out.close();
        return true;
      } else {
        // If file does not end in .txt, determine ending of file to ascertain 
        // desired formant, and write file out using the ImageIO API.
        String fmt = filename.substring( filename.lastIndexOf( "." ) + 1 );
        return ImageIO.write( image, fmt, new File(filename) );
      }
    } catch ( IOException e ) {
      // If an error occurs, print it out and return false.
      System.out.println( e );
      return false;
    }
  }


  /**
     This method gets the pixel at location (x,y) and stores its three
     colour components in a three element array (rgb) that is passed as
     one of the parameters.

     Parameters: x   : x coordinate of the pixel
                 y   : y coordinate of the pixel
                 rgb : a 3 element integer array for storing the components
     Returns:    true on success and false otherwise
  */ 
  public boolean getPixel( int x, int y, int [] rgb ) {
    // Check if image is loaded
    if( image != null ) {
      // If it is, get pixel (into c), unpack the rgb components, alpha adjust, 
      // and store each component into rgb[0..2]
      int c = image.getRGB( x, y );
      int alpha = ( c >> 24 ) & 0xff;
      rgb[0] = alpha * ( ( c >> 16 ) & 0xff ) / 255;
      rgb[1] = alpha * ( ( c >> 8 ) & 0xff ) / 255; 
      rgb[2] = alpha * ( c & 0xff ) / 255; 
    } else {
      // Else print out an error message.
      System.out.println( "Error: Image not loaded" );
    }
    // An error occurs only if the image was not loaded.
    return image != null;
  }


  /**
     This method sets the pixel at location (x,y) from thre three
     colour components in a three element array (rgb) that is passed as
     one of the parameters.

     Parameters: x   : x coordinate of the pixel
                 y   : y coordinate of the pixel
                 rgb : a 3 element integer array storing the components
     Returns:    true on success and false otherwise
  */ 
  public boolean setPixel( int x, int y, int [] rgb ) {
    // Check if image is loaded
    if( image != null ) {
      // If it is, pack the rgb components, using alpha = 255. 
      // into c, preserving only the alpha, and then set the pixel.
      int c = 0xff000000 | ( ( rgb[0] & 0xff ) << 16 ) | 
          ( ( rgb[1] & 0xff ) << 8 ) | ( rgb[2] & 0xff );
      image.setRGB( x, y, c );
    } else {
      // Else print out an error message.
      System.out.println( "Error: Image not loaded" );
    }
    // An error occurs only if the image was not loaded.
    return image != null;
  }


  /**
     This method returns the height of the current image.

     Returns:    the height of the image or 0 if no image is loaded.
  */ 
  public int getHeight() {
    // Check if image is loaded
    if( image != null ) {
      // If it is, get the image's height and return it.
      return image.getHeight();
    } else {
      // Else print out an error message.
      System.out.println( "Error: Image not loaded" );
    }
    return 0;
  }


  /**
     This method returns the width of the current image.

     Returns:    the width of the image or 0 if no image is loaded.
  */ 
  public int getWidth() {
    // Check if image is loaded
    if( image != null ) {
      // If it is, get the image's width and return it.
      return image.getWidth();
    } else {
      // Else print out an error message.
      System.out.println( "Error: Image not loaded" );
    }
    return 0;
  }
}
