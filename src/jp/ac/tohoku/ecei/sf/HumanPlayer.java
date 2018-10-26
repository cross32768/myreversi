package jp.ac.tohoku.ecei.sf;

import java.io.*;

/**
   人間プレイヤーの実装．このプレイヤーは標準入出力を利用して手をユーザに問い合わせる．  
 */
public class HumanPlayer implements Player {

    /**
       
       {@inheritDoc}
       

       
       この関数は標準入出力にインタラクティブに手を問い合わせる．
     */
    public synchronized Move play ( ReversiBoard board, int color ) {
        board.print( color );

        if ( !board.isPlayable( color ) ) {
            System.out.println( "Your turn is skpped because you have no move to play." );
            return new Move();
        }
       

        final BufferedReader br = new BufferedReader( new InputStreamReader( System.in ) );

        System.out.println( "Your turn." );
        
        String line;
        try {
            while ( true ) {
                System.out.print( (color == ReversiBoard.WHITE ) ? "White?> " : "Black?> " );
                System.out.flush();

                line = br.readLine();
                if ( line == null ) {
                    System.exit(1); 
                }
                if ( line.length() < 2 ) {
                    System.out.println( "Error: input two characters." );                    
                    System.out.println( "Syntax: [A-H][1-8]" );
                    continue;
                }
                char c0 = line.charAt(0);
                char c1 = line.charAt(1);
                Move move = new Move( (c1 - '1') + 1, (c0 - 'A') + 1);
                if ( board.isLegalMove( move, color ) ) {
                    return move;
                }
                else {
                    System.out.println( "Invalid move." );
                    System.out.println( "Syntax: [A-H][1-8]" );
                    continue;
                }
            }            
        } catch ( Exception e ) {
            e.printStackTrace();
            System.exit(1);
        }
        return null; // unreachable
    }
}
