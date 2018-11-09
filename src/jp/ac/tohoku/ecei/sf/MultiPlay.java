package jp.ac.tohoku.ecei.sf;

import java.io.IOException;

public class MultiPlay{

	private static String usage() {
		return ("Usage: java -jar jikkenC-reversi.jar (client HOST PORT|server PORT)");
	}

	private static void startServer(int port, String servertype) {
		ServerSingle s = null;
		try{
			s = new ServerSingle(port, servertype);
			s.waitConnection();
			s.close();
		}
		catch(Exception e) {
			e.printStackTrace();
			System.out.println("Starting server failed.");
		}

		try{
			if (s != null) s.close();
		}
		catch (Exception e) {}
	}

	private static void startClient_and_play_game(String host, int port, String playertype) {
		Player you = null;
		RemotePlayer they = null;

		try{
			they = new RemotePlayer(host, port);
			if (playertype.equals("human")){
				you = new HumanPlayer();
			}
			else if (playertype.equals("random")){
				you = new RandomPlayer();		
			}
			else {
				// raise error
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

			they.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("Connection failed.");
		}
	}

	public static void main(String[] args) {
		if (args.length == 0) {
			System.out.println(usage());
			return;
		}
		
		if (args[0].equals("client")){
			String clienttype = null;
			String host = null;
			int    port = 0;

			if (args.length < 3){
				System.out.println(usage());
				return;
			}
			if (args.length == 4){
				if (args[1].equals("-h")){
					clienttype = "human";
				    host = args[2];
				    port = Integer.parseInt(args[3]);
				}
				else {
					// usage for argument of client

					return;
				}
			}
			else if(args.length == 3){
			    clienttype = "random";
			    host = args[1];
			    port = Integer.parseInt(args[2]);
			}
			else{
				System.out.println(usage());
				return;
			}
			startClient_and_play_game(host, port, clienttype);
		}
		else if(args[0].equals("server")){
			String servertype = null;
			int port = 0;
			
			if (args.length < 2){
				System.out.println(usage());
			}

			if (args.length == 3){
				if (args[1].equals("-h")){
						servertype = "human";
						port = Integer.parseInt(args[2]);
				}
				else{
					// usage for arcument of server
				}	
			}
			else if (args.length == 2){
				servertype = "random";
				port = Integer.parseInt(args[1]);
			}
			else{
				System.out.println(usage());
				return;
			}

			startServer(port, servertype);
		}
		else{
			System.out.println(usage());
			return;
		}
	}
}

