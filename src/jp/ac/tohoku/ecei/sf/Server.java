package jp.ac.tohoku.ecei.sf;

import java.net.*;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.*;
import java.nio.charset.StandardCharsets;

public class Server implements Closeable {
	private final int  MAX_THREAD = 20;
	private final ServerSocket sock;
	private Player player = null;

	private final ExecutorService pool;

	public Server(int port, String playertype) throws Exception {
		this.sock = new ServerSocket(port);
		this.pool = Executors.newFixedThreadPool(MAX_THREAD);
		if (playertype.equals("human")){
			this.player = new HumanPlayer();
		}
		else if (playertype.equals("random")){
			this.player = new RandomPlayer(true);
		}
		else{
			// raise error
		}
	}

	class Worker implements Runnable {
		private final Socket conn;
		public Worker(Socket s){
			conn = s;
		}

		private void interact(InputStream is, OutputStream os){
			final long threadId = Thread.currentThread().getId();

			byte[] buf = new byte[5];
			byte[] okcmd = "OK".getBytes(StandardCharsets.UTF_8);
			byte[] endcmd = "\r\n".getBytes(StandardCharsets.UTF_8);
        
			try{
				while(true){
                    System.out.format("threadId:[%04d]\n", threadId);
					
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

		public void run(){
			try {
                final InputStream  is = conn.getInputStream();
                final OutputStream os = conn.getOutputStream();            
                interact( is, os );
                conn.close();
			}
			catch (IOException e){
				try { conn.close(); }
				catch ( IOException ee ) {}
			}
		}
	}

	public void waitConnection(){
		try{
			while (true){
		   		final Socket conn = sock.accept();
	   			try{
   					pool.execute(new Worker(conn));
				}
			   	catch (Exception e){
		   			System.out.println("Something wrong happened: the connection from " + conn + " will be closed.");
	   				conn.close();
   				}
			}
		}
	   	catch (IOException e){
			e.printStackTrace();
		   	pool.shutdown();
	   	}	
  	}

	public void close() throws IOException{
		sock.close();
	}	
} 
