/**
 * Program #1 -- Decoder
 * This program takes the encoded files produced by RLEncoder.java,
 * decodes them, and prints the results to a user-specified file.
 *
 * Authors: Yunyi Ding (yding13@ucsc.edu) and
 *          Shayne Clementi (snclemen@ucsc.edu)
 */
import java.io.*;
public class RLDecoder{
  /**
   * In the main method, the program starts reading from a
   * user-specified file. As long as the variable responsible
   * for reading (lastRead) has more input to read, the while
   * loop executes and prints the input to a new file.
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
    int lastRead = input.read();
    int endOfFile = -1;
    //special value that indicates there's possibly a run
    int runIndicatorValue = 255;
    /*
     * while loop runs while there is input to be read
     * and has checks in it to deal with runs
     */
    while(lastRead!=endOfFile){
      //variables used when a potential run is encountered
      int numberCount;
      int numberRepeated;
      //checks to see if lastRead has come across
      //the special indicator value
      if(lastRead==runIndicatorValue){
        numberCount=input.read(); //set to be the next number after lastRead
        //checks to see if there are two 255s in a row;
        //if there are, then this is the special case
        //where there was a single 255 in the uncompressed file
        if(numberCount==runIndicatorValue){
          lastRead=numberCount;
          out.write(lastRead);
        }
        //if this is not that special single 255 case, then it's a run
        //and this else statement prints out the repeated value (numberRepeated)
        //a specific number of times (numberCount)
        else {
          numberRepeated=input.read();
          for(int i=1;i<=numberCount;i++){
            out.write(numberRepeated);
          }
        }
      }
      //as long as lastRead doesn't come across 255,
      //it just prints the values it reads
      else{
        out.write(lastRead);
      }
      lastRead=input.read(); //moves to next value
    }
    out.close(); //closes output stream
  }
}
