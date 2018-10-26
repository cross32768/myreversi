package jp.ac.tohoku.ecei.sf;

import java.util.*;

/**
   ランダムプレイヤーの実装．このプレイヤーは合法手をランダムに指す．
 */
public class RandomPlayer implements Player {
    private final Random  rgen;
    private final boolean isQuiet;
    
    /**
       （うるさい）ランダムプレイヤーを作成する．

       うるさいランダムプレイヤーは{@link #play(ReversiBoard,int)}時に
       着手後の版面と選んだ手の情報を標準出力に出力する．
     */
    public RandomPlayer() {
        this( false, new Random() );
    };

    /**
       静けさフラグを与えて，ランダムプレイヤーを作成する

       静かなランダムプレイヤーは{@link #play(ReversiBoard,int)}時に標準出力に出力を行わない．
       @param isQuiet もし{@code true}ならばメッセージ出力を抑制する
     */
    public RandomPlayer( boolean isQuiet ) {
        this( isQuiet, new Random() );
    }

    /**
       乱数生成器を与えて，（うるさい）ランダムプレイヤーを作成する．
       @param rgen 乱数生成器
     */
    public RandomPlayer( Random rgen ) {
        this( false, rgen );
    }

    /**
       より一般的な{@link RandomPlayer}のコンストラクタ
       @param isQuiet もし{@code true}ならばメッセージ出力を抑制する
       @param rgen    乱数生成器
    */
    public RandomPlayer( boolean isQuiet, Random rgen ) {
        this.isQuiet = isQuiet;
        this.rgen    = rgen;
    }
       

    /**
       {@inheritDoc}

       このプレイヤーは合法手をランダムに選択する．
     */    
    public Move play ( ReversiBoard board, int color ) {
        List<Move> moves = board.legalMoves( color );
        if ( moves.isEmpty() ) {
            return new Move();
        }

        final int i = rgen.nextInt( moves.size() );
        final Move mv = moves.get(i);

        if ( !isQuiet ) {
            board.print( color );
            System.out.println("Random player played " + mv);
        }
        return mv;
    }
}
