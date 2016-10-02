/**
 * Program 1: RLEncoder
 * This program takes files and compresses them using 
 * run length encoding, which "breaks down" runs of
 * repeated numbers (longer than 2) into a special
 * three byte pattern: [run indicator value] [length of run] [repeated number]
 * 
 * The run indicator value is 255.
 * 
 * All of the writing/encoding to the file is done inside main
 * because the OutputStream object relies on the user input
 * through the args array to know where to write the values to,
 * and we couldn't figure out how to pass that array value
 * to another method.
 * 
 * Authors: Yunyi Ding (yding13@ucsc.edu)
 *          Shayne Clementi (snclemen@ucsc.edu)
 */

import java.io.*;
class RLEncoder{
  /**
   * In the main method, the program starts reading from a
   * user-specified file and prints this input to a new
   * user-specified file. There are checks that deal with
   * runs of numbers and prints them out using the special
   * pattern.
   */
  public static void main(String [] args) throws IOException{ 
    //these variables read the user-specified file names
    //and pass them to the input/output variables
    String file=args[0]; 
    String outputFile = args[1];
    //objects responsible for input/output
    InputStream input = new FileInputStream(file);
    OutputStream out = new FileOutputStream(outputFile);
    //variable that actually does the initial reading
    int number = input.read();
    //variable that keeps track of run lengths
    int count=1;
    int endOfFile = -1;
    /*
     * while loop runs while there is input to be read
     * and has checks in it to deal with runs
     */
    while(number!=endOfFile){
      int runIndicatorValue=255;
      int maxByteNum=254; //maximum number a byte can hold
      int minRunLength = 2;
      int numberNext=input.read(); //reads ahead of number
      //deals with non-repeating numbers and numbers that separate runs
      if(number!=numberNext){
        //deals with runs that were longer than 254
        if(count>maxByteNum){
          //prints out the run in 254-sized chunks
          for(int i=1;i<=count/maxByteNum;i++){
            out.write(runIndicatorValue);
            out.write(maxByteNum);
            out.write(number);
          }
          //after printing out the run in 254-sized chunks,
          //prints the remainder of the run
          out.write(runIndicatorValue);
          out.write(count%maxByteNum);
          out.write(number);
        }
        //prints out runs longer than 3 but smaller than 254
        else if(count>minRunLength &&count<=maxByteNum){
          out.write(runIndicatorValue);
          out.write(count);
          out.write(number);
        }
        //deals with "runs" of length two
        else if(count==minRunLength){
          //encodes special case of two 255s right next to each other
          if (number==runIndicatorValue){
            out.write(runIndicatorValue);
            out.write(count);
            out.write(number);
          }
          //just prints out the two numbers
          else{
            out.write(number);
            out.write(number);
          }
        }
        //deals with single numbers
        else{
          //encodes special case of a single inline 255
          //by printing it out twice
          if(number==runIndicatorValue){
            out.write(number);
            out.write(number);
          }
          //prints out the single numbers
          else{
            out.write(number);
          }
        }
        number=numberNext; //moves number to next number
        count=1; //resets count to 1
      }
      //keeps track of how long runs are
      //(only run when number = numberNext)
      else{
        count++;
      }
    }
    out.close(); //closes output stream
  }
}

