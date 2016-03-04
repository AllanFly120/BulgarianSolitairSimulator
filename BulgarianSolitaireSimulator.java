// Name:Yangping Zheng
// USC loginid: yangpinz
// CSCI455 PA2
// Fall 2015


/**
   <add main program comment here>
 */
import java.util.*;
public class BulgarianSolitaireSimulator {

   public static void main(String[] args) {
     
      boolean singleStep = false;
      boolean userConfig = false;
      SolitaireBoard board = null;

      for (int i = 0; i < args.length; i++) {
         if (args[i].equals("-u")) {
            userConfig = true;
         }
         else if (args[i].equals("-s")) {
            singleStep = true;
         }
      }

      // <add code here>
      Scanner in = new Scanner(System.in);
      if(userConfig==true) {
          
          System.out.println("Number of total cards is "+SolitaireBoard.CARD_TOTAL);
          System.out.println("You will be entering the initial configuration of the cards (i.e., how many in each pile).");
          System.out.println("Please enter a space-separated list of positive integers followed by newline:");
          String inputString = in.nextLine();

          while(!SolitaireBoard.isValidConfigString(inputString)) {
        	  System.out.println("ERROR: Each pile must have at least one card and the total number of cards must be "+SolitaireBoard.CARD_TOTAL);
        	  System.out.println("Please enter a space-separated list of positive integers followed by newline:");
        	  if(in.hasNextLine()) {
        	      inputString = in.nextLine();
        	      }
          }
          board = new SolitaireBoard(inputString);
      }
      if(userConfig==false) {
    	  board = new SolitaireBoard();
      }

      if(singleStep==true) {
    	  runByStep(board);
      }
      if(singleStep==false) {
    	  contineousOutput(board);
      }
      in.close();
  }
            
      
      
   
    // <add private static methods here>
   private static void runByStep(SolitaireBoard board) {
	   int iterator=0;
	   System.out.print("Initial configuration: "+board.configString());
	   while((new Scanner(System.in)).hasNextLine()) {
		   iterator++;
		   board.playRound();
		   System.out.println("["+iterator+"] Current configuration: "+board.configString());
		   System.out.print("<Type return to continue>");
		   if(board.isDone()) {
			   System.out.println("Done!");
			   break;
		   }
	   }
   }
  
   private static void contineousOutput(SolitaireBoard board) {
	   int iterator=0;
	   System.out.println("Initial configuration: "+board.configString());
	   while(!board.isDone()) {
		   iterator++;	 
		   board.playRound();
		   System.out.print("["+iterator+"] Current configuration: "+board.configString());
		   System.out.println();    	  
	      }
	   System.out.println("Done!");
   }
}
