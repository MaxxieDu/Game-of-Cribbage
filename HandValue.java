
/** This class provides some methods to compute the total scores of 5 cards inputs on the command line
 * and prints out only the number of points the hand comprising the first four of those card 
 * would score if the fifth card is the start card.  
 *@author Zeyu Du zeyud@student.unimelb.edu.au StudentID:952331
 */

import java.util.Arrays;
 public class HandValue {
    
    private String[] cards;
    
    private int totalScore;
    private String[][] combos;
    
    
    /** This is a main method to run the whole class, It simply get the 5 cards inputs on the command line
     * and prints out only the scores.
     * The 5 cards inputs on the command line should be two-character strings, the first 
     * being a Upper-case A for Ace, K for King, Q for Queen, J for Jack, T for Ten, or digit
     * between 2 to 9. The second character should be C for clubs, D for Diamond, H for Hearts
     * or a S for Spades.
     */  
    
    public static void main(String[] args) {           
        HandValue hand = new HandValue(args);
        int scores = hand.getHandValue();
        System.out.println(scores);        
    }
    
    /**construct the cards on hand.
     * @param cards the cards input on the command line.
     */    
    HandValue(String[] cards) {
        this.cards = cards;
        totalScore = 0;
        combos = Combinations.combinations(cards);
    }
    
    /**Compute the nobs for the cards on hand.
     * @return nobvalue, the scores getting from "one for his nob"
     **/
    private int nob() {
        int nobValue = 0;
        char startSuit = cards[cards.length - 1].charAt(1);
        for (int i = 0; i <= cards.length - 2; i++) {
            char cardRank = cards[i].charAt(0);
            char cardSuit = cards[i].charAt(1);
   
                                   
            if (cardRank=='J'&&cardSuit == startSuit) {
                nobValue += 1;
            }
        }        
        return nobValue;
    }
   
    /**
     * check whether the player's cards are in same suit
     * @return same - true; different - false
     */
    private boolean SameSuit() {
        for (int i = 0; i < cards.length-2; i++) {
            if (cards[i].charAt(1) != cards[i + 1].charAt(1)) {
                return false;
            }
        }
        
        return true;
    }    
 
    /**Compute the scores if there is flush.
     * @return flushValue, the scores getting from "flush"
     */
    private int flush() {
        int flushValue = 0;
        if (SameSuit()) {
            flushValue += 4;
            
            char firstCardSuit = cards[0].charAt(1);
            char StartCardSuit = cards[cards.length-1].charAt(1);
            
            if(firstCardSuit==StartCardSuit){
                flushValue +=1;
            }
        }        
        return flushValue;
    }
    
  
    /**Compute the scores if there are pairs on the hand.
     * @return pValue, the score getting from "pairs"
     */
    private int pairs() {
        int pairValue = 0;
        
        for (int i = 0; i < combos.length; i++) {
            if (combos[i].length == 2 && combos[i][0].charAt(0) == combos[i][1].charAt(0)) {
                pairValue += 2;
            }
        }
        
        return pairValue;
    }
    
        /** For each combinations of the cards, check whether the sum is
     *   equal to 15.
     */
    private int fifteens() {
        int fifteenValue = 0;
        Integer[] faceValue = new Integer[cards.length];
        Integer[][] combos2;
        
        for (int i = 0; i < cards.length; i++) {
            for (CribbageRank card : CribbageRank.values()) {
                if (cards[i].charAt(0) == card.abbrev()) {
                    faceValue[i] = card.faceValue();
                    break;
                }
            }
        }
     
    /**Compute the scores if there are 15s on the hand.
     * @return fifteenValue,the value getting from "15s"
     */       

        combos2 = Combinations.combinations(faceValue);
        for (int i = 0; i < combos2.length; i++) {
            int sum = 0;
            for (int j = 0; j < combos2[i].length; j++) {
                sum += combos2[i][j];
            }
            
            if (sum == 15) {
                fifteenValue += 2;
            }
        }     
        return fifteenValue;
    }
    
    /**
     * this method checks whether a combination is a run
     * @param combo, all the combinations of the cards on the hand.
     * @return true - there is a run, false - this is not a run.
     */
    private boolean isRun(CribbageRank[] combo) {
        for (int i = 0; i < combo.length-1; i++) { 
        	if (combo[i].nextHigher()!= combo[i+1]) {
                 return false;
              }
        }
        return true;
    }
        
    
    /**Compute the scores if there are runs on the hand.
     * @return the value getting from "runs"
     */
    private int runValue() {
        int rValue = 0;
        int maxLength = 0;
        
        CribbageRank[] rank = new CribbageRank[cards.length];
        CribbageRank[][] combos;
        
        for (int i = 0; i < cards.length; i++) {
            for (CribbageRank card : CribbageRank.values()) {
                if (cards[i].charAt(0) == card.abbrev()) {
                    rank[i] = card;
                }
            }
        }
        
        /** Find the maximum length for the combination which can form a run.
        */
        combos = Combinations.combinations(rank);
        
        for (int i = 0; i < combos.length; i++) {
            Arrays.sort(combos[i]);         
          if(combos[i].length>=3 && isRun(combos[i])){
              if (combos[i].length > maxLength) {
                  maxLength = combos[i].length;
                  }
            }            
        }        
         /** Add up the scores if the maximum length combination is a run.
        */
        for (int i = 0; i < combos.length; i++) {
            if (combos[i].length == maxLength && isRun(combos[i])) {
                rValue += combos[i].length;
            }
        }        
        return rValue;
    }

    /**This method is to calculate all the scores from different combinations
     * 
     * @return totalScore, the total score of the cards on the hand.
     */
    
    public int getHandValue() {
        
        totalScore = nob() + flush() + pairs() + fifteens() + runValue();
        
        return totalScore;
    }
    
}