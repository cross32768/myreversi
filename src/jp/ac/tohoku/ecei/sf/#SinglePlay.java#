package jp.ac.tohoku.ecei.sf;

import java.io.IOException;

/**
   メインクラス
 */
// Todo improve code for argument
public class SinglePlay {
    public static void main( String[] args ) throws IOException {
        Player you;
        Player they = null;
        try{
            they = new RemotePlayer(args[0], Integer.parseInt(args[1]));
        }
        catch(Exception e){
            e.printStackTrace();
        }
        if ( args.length > 0 
             && (args[0].charAt(0) == 'r' || args[0].charAt(0) == 'R') ) {
            you = new RandomPlayer(true);
        }       
        else {
            you = new HumanPlayer();
        }

        ReversiBoard b;
        boolean isYourColorBlack = Math.random() < 0.5; 

        if ( isYourColorBlack ) {
            System.out.println("You are a black player.");
            b = Game.game( you, they );
        }
        else {
            System.out.println("You are a white player.");
            b = Game.game( they, you );
        }
        
        System.out.println("-----------------------------------");
        System.out.println("Game finished.");
        b.print();


        System.out.println( "You played " + (isYourColorBlack ? "black" : "white") + ".");
    }
}
