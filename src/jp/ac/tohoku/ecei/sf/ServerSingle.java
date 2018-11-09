package jp.ac.tohoku.ecei.sf;

import java.net.*;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ServerSingle implements Closeable {
    private final ServerSocket sock;
    private Player player = null;

    public ServerSingle(int port, String playertype) throws Exception{
        this.sock = new ServerSocket(port);
        this.sock.setReuseAddress(true);
		if (playertype.equals("human")){
			this.player = new HumanPlayer();
		}
		else if(playertype.equals("random")){
			this.player = new RandomPlayer(true);
		}
		else{
			// raise error
		}
    }

    protected void interact(InputStream is, OutputStream os){
        byte[] buf = new byte[5];
		byte[] okcmd = "OK".getBytes(StandardCharsets.UTF_8);
	    byte[] endcmd = "\r\n".getBytes(StandardCharsets.UTF_8);
        
        try{
            while(true){
				for (int i = 0;i < 5;i++){
					int b = is.read();
					buf[i] = (byte) b;
				}

				String strbuf = new String(buf, StandardCharsets.UTF_8);
				String cmd = strbuf.substring(0, 4);

				if (cmd.equals("QUIT")){
					return;
				}

				if (cmd.equals("MOVE")){
					ReversiBoard board = new ReversiBoard(is);				
					for (int i = 0;i < 4;i++){
						int b = is.read();
						buf[i] = (byte) b;
					}
					int color = board.byte2Color(buf[1]);

					Move mv = player.play(board, color);
					mv.writeTo(os);
					os.write(endcmd);
					os.flush();
				}
				else if (cmd.equals("NOOP")){
					is.skip(1);
					os.write(okcmd);
					os.write(endcmd);
					os.flush();
				}
				else {
					throw new IOException(cmd + " is invalid command.");
				}					
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
			System.out.println("Server is closed.");
            sock.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
