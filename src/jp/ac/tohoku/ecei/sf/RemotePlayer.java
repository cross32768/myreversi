package jp.ac.tohoku.ecei.sf;

import java.util.*;
import java.net.*;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.nio.charset.StandardCharsets;


public class RemotePlayer implements Player, Closeable {
    private final Socket sock;
    private final InputStream is;
    private final OutputStream os;

    public RemotePlayer(String host, int port) throws Exception {
        this(new Socket(host, port));
    }

    public RemotePlayer(Socket sock) throws Exception {
        this.sock = sock;
        this.is = sock.getInputStream();
        this.os = sock.getOutputStream();
    }

	public void quit() throws IOException{
		byte [] cmd = "QUIT\r\n".getBytes(StandardCharsets.UTF_8);
		os.write(cmd);
		os.flush();
	}

    public Move play(ReversiBoard board, int color){
        byte [] cmd = "MOVE ".getBytes(StandardCharsets.UTF_8);
        byte [] color_and_end = " C\r\n".getBytes(StandardCharsets.UTF_8);
        Move mv = null;

        try {
            os.write(cmd);
            board.writeTo(os);

            color_and_end[1] = board.color2Byte(color);
            os.write(color_and_end);
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
		try{
			quit();
			sock.close();
		}
		catch (IOException e){
			e.printStackTrace();
		}
    }
}
