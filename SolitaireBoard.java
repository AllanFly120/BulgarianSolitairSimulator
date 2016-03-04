// Name:Yangping Zheng
// USC loginid:yangpinz
// CSCI455 PA2
// Fall 2015



/*
   class SolitaireBoard
   The board for Bulgarian Solitaire.  You can change what the total number of cards is for the game
   by changing NUM_FINAL_PILES, below.  Don't change CARD_TOTAL directly, because there are only some values
   for CARD_TOTAL that result in a game that terminates.
   (See comments below next to named constant declarations for more details on this.)
 */

import java.util.*;
public class SolitaireBoard {   
    public static final int NUM_FINAL_PILES = 9;
   // number of piles in a final configuration
   // (note: if NUM_FINAL_PILES is 9, then CARD_TOTAL below will be 45)
   
    public static final int CARD_TOTAL = NUM_FINAL_PILES * (NUM_FINAL_PILES + 1) / 2;
   // bulgarian solitaire only terminates if CARD_TOTAL is a triangular number.
   // see: http://en.wikipedia.org/wiki/Bulgarian_solitaire for more details
   // the above formula is the closed form for 1 + 2 + 3 + . . . + NUM_FINAL_PILES
    
	public static final int ARRAY_UPPER_BOUND = 100;
     // set the upper bound of the array in which we store the number of cards in each pile 
   
   /**
  Representation invariant:
 	pileNumber is integer and >=1 and <= total number of cards
    first pileNumber elements in cardsInPile[] are integers and >=1
    sum of all piles' size is equal to the total number of cards
*/

   
   // <add instance variables here>
   public static int[] cardsInPile = new int[ARRAY_UPPER_BOUND];
   public static int pileNumber;           //keep track of the number of piles. Count from 1
 
   /**
     Initialize the board with the given configuration.
     PRE: SolitaireBoard.isValidConfigString(numberString)
   */
    public SolitaireBoard(String numberString) {
    	int cardsInPileIndex = 0;
    	
    	assert isValidConfigString(numberString);
    	
    	String[] parseInputString = numberString.trim().split(" ");
	    for(int i=0;i<parseInputString.length;i++) {
		   cardsInPile[i]=parseNumber(parseInputString[i]);   //store the cards number in each pile in a array
		   cardsInPileIndex++;
	    }	   
	    pileNumber = cardsInPileIndex;
	    assert isValidSolitaireBoard();   // sample assert statement (you will be adding more of these calls)
   }
    

   /**
      Create a random initial configuration.
      
      The first number is generated from the range[0, CARD_TOTAL], and following numbers
      are generated asymptoticly.
   */
   public SolitaireBoard() {
	   Random r = new Random();
	   int sum = 0;                     //compute sum of random configuration
	   pileNumber=0;
	   while(sum<CARD_TOTAL) {
		   int tmp = 0;
		   while(tmp<=0) {
			   tmp = (int)Math.round(r.nextDouble()*(CARD_TOTAL-sum));
		   }
		   cardsInPile[pileNumber]=tmp;
		   sum = sum+cardsInPile[pileNumber];
		   pileNumber++;		   
	   }
	   if(sum<CARD_TOTAL) {
		   cardsInPile[pileNumber] = CARD_TOTAL - sum;		   
	   }
	   assert isValidSolitaireBoard(); 
   }
  
   
   /**
  Play one round of Bulgarian solitaire.  Updates the configuration according to the rules of Bulgarian
  solitaire: Takes one card from each pile, and puts them all together in a new pile.
*/
   public void playRound() {
	   assert isValidSolitaireBoard(); 
	   int cardExtractCount = 0;
	   int i;
	   for(i=0;i<pileNumber;i++) {
		   if(cardsInPile[i]>0){
		    	cardsInPile[i]-=1;
		    	cardExtractCount+=1;			   
		   }
	   }
	   cardsInPile[i] = cardExtractCount;
	   pileNumber++;
	   excludZero();
   }
   

   /**
  Return true iff the current board is at the end of the game.  That is, there are NUM_FINAL_PILES
  piles that are of sizes 1, 2, 3, . . . , NUM_FINAL_PILES, in any order.
  Firstly this method determine whether the pileNumber equals to NUM_FINAL_PILES.
  To avoid nested for loop, I use the numberReg[] as a register of numbers that has shown up in the array.
  0     1   2   3   4   5   6   7   8 
  false f   f   f   f   f   f   f   f
  If 9 is contained in cardsInPile[], we set numberReg[8] (9th element) as true.
  0     1   2   3   4   5   6   7   8 
  false f   f   f   f   f   f   f   true
  In the end, if all elements(within pileNumber) are true in numberReg[], the method returns true.
  The complexity of this method is O(n)
*/
   
   public boolean isDone() {
	   assert isValidSolitaireBoard(); 
	   boolean rtn = false;
	   if(pileNumber!=NUM_FINAL_PILES) return rtn;
	   
	   boolean[] numberReg = new boolean[ARRAY_UPPER_BOUND];
	   for(int i=0;i < numberReg.length;i++) {
		   numberReg[i] = false;
	   }
	   
	   for(int i=0;i < pileNumber;i++) {
		  numberReg[cardsInPile[i]-1]=true; 
	   }
	   	  
	   for(int i=0;i<NUM_FINAL_PILES;i++) {
		   if(numberReg[i]==false) {
			   return false;
		   }
	   }
       return rtn = true; 
      
   }

   
   /**
  Returns current board configuration as a string with the format of
  a space-separated list of numbers with no leading or trailing spaces.
  The numbers represent the number of cards in each non-empty pile.
*/
   public String configString() {
	   StringBuffer configStringBuf = new StringBuffer();
	   
	   for(int i=0;i<pileNumber;i++) {
		   configStringBuf.append(Integer.toString(cardsInPile[i]));
		   if(i==pileNumber-1) break;            //no space in the end of string;
		   configStringBuf.append(" ");
	   }
	   
      return configStringBuf.toString();   
   }
   
   

   
   /**
      Returns true iff configString is a space-separated list of numbers that
      is a valid Bulgarian solitaire board assuming the card total SolitaireBoard.CARD_TOTAL
   */
   public static boolean isValidConfigString(String configString) {
	   boolean rtn=true;
	   int cardsSum = 0;
	   if(configString.length()==0||configString==null) return rtn=false;
	   String[] parseInputString = configString.trim().split(" ");
	   
	   if(parseInputString.length>ARRAY_UPPER_BOUND)return rtn=false;
	   
	   for(int i=0;i<parseInputString.length;i++) {
		   int tmp =parseNumber(parseInputString[i]);   //store the cards number in each pile in a array
		   if(tmp<=0) return rtn=false;
		   cardsSum=cardsSum + tmp;
	   }
	   if(cardsSum!=CARD_TOTAL) return rtn=false;
	   
       return rtn;  
      
   }


   /**
  Returns true iff the solitaire board data is in a valid state
  (See representation invariant comment for more details.)
*/
   private boolean isValidSolitaireBoard() {
	   return isValidConfigString(configString());
	   
   }
   

    // <add any additional private methods here>
   /*
    * transmit the string denoting the number of cards in each pile from configString
    * into integers.
    * return: positive integer if the string can be parsed
    * 	       -1 if the string cannot be parsed into integer
    * */
   private static int parseNumber (String str) {
	   
	   int rtn=0;                        //return value
	   try {
		   rtn = Integer.parseInt(str);
		   return rtn;
	   } catch(NumberFormatException e) {
		   return rtn=-1;               //mistake flag. including double input and character input
	   }
   }
   
   /*
    * Get rid of zeros in the cardsInPile[]
    * Use 2 pointer algorithm. First pointer points to the position of elements in new array
    * (without zeros). Second pointer points to the position of elements in original array.
    * First pointer jump one element in every one iteration. Second pointer jump over all zeros
    * and when it jumps to the end of array, first point fills zeros in every elements it points to. 
    * */   
      private static void excludZero() {
   	   int originLoc = 0; //first pointer
   	   int targetLoc = 0; //second pointer
   	   int pileNumberReduce = 0;
   	   for(;targetLoc<=pileNumber;targetLoc++) {
   		   while(cardsInPile[originLoc]==0 && originLoc<=pileNumber) { //jump over all zeros in the head of array
   			   originLoc++;
   		   }
   		   if(originLoc <= pileNumber) { //set the element to which first pointer points to as value of second pointer.
   			   cardsInPile[targetLoc]=cardsInPile[originLoc];
   		   }
   		   if( originLoc > pileNumber) {
   			   cardsInPile[targetLoc]=0;
   			   pileNumberReduce++;   //adjust pileNumber
   		   }
   		   originLoc++;
   	   }
   	   pileNumber=pileNumber-pileNumberReduce+1;//adjust length of cardsInPile[]
      }
      
  

}
