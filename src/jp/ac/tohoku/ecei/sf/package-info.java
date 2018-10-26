/**
   一人用リバーシの実装. 

   まずは<code>ant jar</code>で<code>.jar</code>ファイルが作成されるので，
   <pre>java -jar jikkenC-reversi.jar</pre>
   で動かしてみよう．
   <p>
   すると
   <pre>
 |A B C D E F G H
-+----------------
1|- - - - - - - -
2|- - - - - - - -
3|- - - . - - - -
4|- - . O X - - -
5|- - - X O . - -
6|- - - - . - - -
7|- - - - - - - -
8|- - - - - - - -
Black: 2
White: 2
Your turn.
Black?&gt;</pre>
ように現在の盤面が表示され, 手が聞かれる. ここで, 手を入力するには<pre>
F5
</pre>のように入力しエンターを押す. この繰り返しでゲームが進んでいく. やめるときはCtrl+D.    
*/
package jp.ac.tohoku.ecei.sf;
