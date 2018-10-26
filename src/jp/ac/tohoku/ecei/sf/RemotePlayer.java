package jp.ac.tohoku.ecei.sf;

import java.util.*;
import java.net.*;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/*
  implements remote player
  this player ask server how to play
*/
public class RemotePlayer implements Player, Closeable {
    private final Socket sock;
    private final InputStream is;
    private final OutputStream os;
  
    /*
      constructer connects specified host and port
    */
    public RemotePlayer(String host, int port) throws Exception {
        this(new Socket(host, port));
    }

    public RemotePlayer(Socket sock) throws Exception {
        this.sock = sock;
        this.is = sock.getInputStream();
        this.os = sock.getOutputStream();
    }

    /*
      this method send state of board and color to server
      and get next move
    */
    public Move play(ReversiBoard board, int color){
        byte [] cmd = "MOVE ".getBytes(StandardCharsets.UTF_8);
        byte [] color_and_end = " C\r\n".getBytes(StandardCharsets.UTF_8);
        Move mv = null;

        try {
            this.os.write(cmd);
            board.writeTo(os);

            color_and_end[1] = board.color2Byte(color);
            this.os.write(color_and_end);
            os.flush();

            mv = new Move(is);
            is.skip(2);
        }
        catch (IOException e){
            e.printStackTrace();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return mv;
    }
                       
    public void close() throws IOException{
        sock.close();
    }
}