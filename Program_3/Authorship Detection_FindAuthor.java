import java.util.regex.*;
import java.util.*;
import java.io.*;

/**
 * This program uses 5 simple linguistic features to try and identify the authorship of an "unknown" text.
 * This is based on an assignment created by Michelle Craig from the University of Toronto.
 * http://nifty.stanford.edu/2013/craig-authorship-detection/
 * 
 * In the comments below a "word" is a String without embedded white space characters and no
 * leading or trailing non-word characters (the word characthers are _,a-z,A-Z, and 0-9).
 * A "token" is a String without embedded white space characters (i.e. what you get when you call next() from the
 * class Scanner).
 */
class FindAuthor {
  static final int NUM_FEATURES = 5;
  
  /**
   * This program expects to be passed the name of the unknown file as the first command line argument,
   * and the name of a directory containing nothing but signature files for known authors as the second
   * command line argument.
   */
  public static void main(String[] args) throws IOException {
    if (args.length != 2) {
      System.out.println("Usage: java FindAuthor unknownAuthorFileName signaturesDirectory");
      System.exit(1);
    }
    
    // read in the new text file from the unknown author
    Scanner textIn = new Scanner(new File(args[0])); 
    ArrayList<String> text = new ArrayList<String>();
    while (textIn.hasNext()) {
      text.add(textIn.next());
    }
    
    //cleans up text arrayList
    for (int j = 0; j < text.size(); j++){
      String cleanedUpText = cleanUp(text.get(j));
      text.set(j, cleanedUpText);
    }
    // Compute the signature of the unknown author
    double[] features = new double[NUM_FEATURES];
    features[0] = averageWordLength(text);
    features[1] = typeTokenRatio(text);
    features[2] = hapaxLegomanaRatio(text);
    features[3] = averageSentenceLength(text);
    features[4] = avgSentenceComplexity(text);
    Signature unknown = new Signature(args[0], features);
    System.out.println(unknown);
    
    double[] weights = {11, 33, 50, 0.4, 4};
    
    // compare the computed signature with those of know authors using the given weights
    File[] files = new File(args[1]).listFiles();
    Signature sig = readSignature(files[0]);
    // guess that the first author is the best match
    double bestScore = compareSignatures(unknown.feature, sig.feature, weights);
    String bestAuthor = sig.author;
    // try the other authors for a better match
    for (int i = 1; i < files.length; i++) {
      sig = readSignature(files[i]);
      double score = compareSignatures(unknown.feature, sig.feature, weights);
      if (score < bestScore) {
        bestScore = score;
        bestAuthor = sig.author;
      } 
    }
    System.out.println("Best author is " + bestAuthor + " with score " + bestScore);      
  }
  
  /**
   * Return a version of String str in which all letters have been
   * converted to lowercase and non-word characters (anything but _,A-Z,a-z,0-9) 
   * have been stripped from both ends. Inner non-word characters are left untouched. 
   * If str does not contain any white space characters, then cleanUp() will convert
   * a "token" into a "word" (see definitions in the opening comment).
   */
  public static String cleanUp(String str) {
    Pattern p = Pattern.compile("(\\W*)(.*?)(\\W*)");
    Matcher m = p.matcher(str);
    m.matches();
    return str.substring(m.end(1),m.end(2)).toLowerCase();    
  }
  
  /**
   * Return the average length of all words (as defined above) in text. 
   * text is a non-empty list of strings.
   * At least one line in text contains a word.
   */
  static double averageWordLength(ArrayList<String> text) {
    int countCharacters=0;
    String[]word=new String[text.size()]; //array created to use length()
    //reads text ArrayList into word Array
    for(int j=0;j<text.size();j++){
      word[j]=text.get(j);
    }
    //adds the length of the word in each index to the total character count
    //(subtracts one to account for punctuation at the end of the token)
    for(int i=0;i<word.length;i++){
      int wordLength=0;
      wordLength = word[i].length();
      if (text.get(i).contains(",")||text.get(i).contains(";") ||text.get(i).contains(":") ){
        wordLength--;
      }
      countCharacters+=wordLength;
    }
    //returns average word length (total number of characters/total number of words)
    return (double)countCharacters/(double)text.size();
  }
  
  /**
   * Return the type token ratio (TTR) for this text.
   * TTR is the number of different words divided by the total number of words.
   * text is a non-empty list of strings.
   * At least one line in text contains a word
   */
  static double typeTokenRatio(ArrayList<String> text){
    ArrayList<String>uniqueWords=new ArrayList<String>(); // arraylist created to contain all the words that show up at least once
    // read in text arraylist, adds words from arraylist text to arraylist word that are not in the list already
    for(int i=0;i<text.size();i++){
      boolean isMatch=false;
      for(int j=0;j<uniqueWords.size();j++){
        if(uniqueWords.get(j).equals(text.get(i))){
          isMatch=true;
        }
      }
      if(!isMatch){
        uniqueWords.add(text.get(i));
      }
    }
    // return type token ratio (number of unique words/all the words)
    return (double)uniqueWords.size()/(double)text.size();            
  }
  
  /**
   * Return the hapaxLegomana ratio for this text.
   * This ratio is the number of words that occur exactly once divided
   * by the total number of words.
   * text is a list of strings.
   * At least one line in text contains a word.
   */
  static double hapaxLegomanaRatio(ArrayList<String> text) {
    ArrayList<String>showsUpOnce=new ArrayList<String>();
    ArrayList<String>moreThanOnce = new ArrayList<String>();
    //cleans up text array
    for (int z = 0; z < text.size(); z++){
      String cleanedUpText = cleanUp(text.get(z));
      text.set(z, cleanedUpText);
    }
    //runs through text, figures out what words
    //show up once/more than once
    for(int i=0;i<text.size();i++){
      boolean isInShowsUpOnce = false;
      //sees if word in text is in showsUpOnce; if it is, adds word to moreThanOnce
      for(int j=0;j<showsUpOnce.size();j++){
        if(showsUpOnce.get(j).equals(text.get(i))){
          moreThanOnce.add(showsUpOnce.get(j));
          isInShowsUpOnce = true;
        }
      }
      if (!isInShowsUpOnce){
        showsUpOnce.add(text.get(i));
      }
    }
    //returns hapax legomana ratio ((words that occur more than once - words that occur once) / total number of words)
    return Math.abs(((double)moreThanOnce.size()-(double)showsUpOnce.size()))/(double)text.size();
  }
  
  /**
   * Return the average number of words per sentence in text.
   * text is guaranteed to have at least one sentence.
   * Terminating punctuation is defined as !?.
   * A sentence is defined as a non-empty sequence of words
   * terminated by a token ending in terminating punctuation 
   * or end of file.
   */
  static double averageSentenceLength(ArrayList<String> text) {
    int sentenceCount = 0; // variable created to count number of sentences
    // remove "-" or "--" in order to pass the findAuthortest
    for(int j = 0; j < text.size(); j++){
      if(text.get(j).equals("-") || text.get(j).equals("--")){
        text.remove(j);
      }
    }
    // count the total number of sentences
    for(int i = 0; i < text.size(); i++){
      if(text.get(i).contains(".") || text.get(i).contains("?") || text.get(i).contains("!")){
        sentenceCount++;
      }
    }
    // return average sentence length (the number of words/the number of sentences)
    return (double)text.size()/(double)sentenceCount;
  }
  
  /**
   * Return the average number of phrases per sentence.
   * Terminating punctuation defined as !?.
   * A sentence is defined as a non-empty sequence of words
   * terminated by a token ending in terminating punctuation 
   * or end of file.
   * Phrases are subsequences of a sentences terminated by a token
   * ending with ,;: or by the end of the sentence.
   */
  static double avgSentenceComplexity(ArrayList<String> text) {
    int phraseCount = 0;
    int sentenceCount = 0;
    //goes through text
    //increments sentence count when it comes across an endline punctiation (. or ? or !)
    //increments phrase when it comes across a phase ending punctuation (, or ; or : or - or --)
    for(int i = 0; i < text.size(); i++){
      if(text.get(i).contains(".") || text.get(i).contains("?") || text.get(i).contains("!")){
        sentenceCount++;
      }
      if(text.get(i).contains(",")||text.get(i).contains(";")||text.get(i).contains(":")||text.get(i).equals("-") || text.get(i).equals("--")){
        phraseCount++;
      }
    }
    //returns average sentence complexity (number of phrases divided by the number of sentences)
    return (double)phraseCount/(double)sentenceCount;
  }
  
  /**
   * Return a non-negative real number indicating the similarity of two
   * linguistic signatures. The smaller the number the more similar the
   * signatures. Zero indicates identical signatures.
   * sig1 and sig2 are NUM_FEATURES element double arrays with the following elements
   *
   * 0  : average word length
   * 1  : TTR
   * 2  : Hapax Legomana Ratio
   * 3  : average sentence length
   * 4  : average sentence complexity
   * weight is a list of multiplicative weights to apply to each
   * linguistic feature.
   */
  static double compareSignatures(double[] sig1, double[] sig2, double[] weight) {
    /*
     * Create variables:
     * Average word length
     * Type text ratio
     * Hapax Legomana ratio
     * Average sentence length
     * average sentence complexity
     */
    double awl = 0;
    double ttr = 0;
    double hlr = 0;
    double asl = 0;
    double asc = 0;
    // calculates the score of each of the test one at a time to put it into the array
    for(int i = 0; i < weight.length; i++){
      //computes scores for each test
      if (i == 0)
        awl = Math.abs(sig1[i]-sig2[i])*weight[i];
      else if (i == 1)
        ttr = Math.abs(sig1[i]-sig2[i])*weight[i];
      else if (i == 2)
        hlr = Math.abs(sig1[i]-sig2[i])*weight[i];
      else if (i == 3)
        asl = Math.abs(sig1[i]-sig2[i])*weight[i];
      else if (i == 4)
        asc = Math.abs(sig1[i]-sig2[i])*weight[i];
    }
    //add all these things up
    double sum = awl + ttr + hlr + asl + asc;
    // return sum of the test
    return sum;
  }
  
  /**
   * Read a linguistic signature from file and return it as
   * a Signature object.
   */
  static Signature readSignature(File file) throws IOException {
    Scanner readFile = new Scanner (file); //reads .stats file
    String author = readFile.nextLine(); //reads in author's name
    double[] features = {0,0,0,0,0}; //array for the scores in the .stats file
    //puts scores in .stats file into features array
    for (int i = 0; i < features.length; i++){
      features[i] = readFile.nextDouble();
    }
    //returns a Signature object to replace sig in main()
    return new Signature(author, features);
  } 
}
