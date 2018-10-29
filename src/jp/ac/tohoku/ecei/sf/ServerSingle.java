package jp.ac.tohoku.ecei.sf;

import java.util.*;
import java.net.*;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ServerSingle implements Closeable {
    private final ServerSocket sock;
    private final RandomPlayer player;

    public ServerSingle(int port) throws Exception{
        this.sock = new ServerSocket(port);
        this.sock.setReuseAddress(true);
        this.player = new RandomPlayer(true);
    }

    protected void interact(InputStream is, OutputStream os){
        byte[] buf = new byte[4];
        ReversiBoard board = null;
        int color = -1;
        Move mv = null;
        
        try{
            while(true){
                is.skip(5);
                board = new ReversiBoard(is);
                
                is.read(buf, 0, 4);
                color = board.byte2Color(buf[1]);

                mv = player.play(board, color);
                mv.writeTo(os);
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public void waitConnection(){
        try{
            while(true){
                final Socket conn = sock.accept();
                try{
                    InputStream  is = conn.getInputStream();
                    OutputStream os = conn.getOutputStream();
                    interact(is, os);
                    conn.close();
                }
                catch(IOException e){
                    System.out.println("Something wrong happens: Shutdown the connection from " + conn + ".");
                    conn.close();
                }
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public void close(){
        try{
            sock.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
