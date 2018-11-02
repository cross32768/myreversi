package jp.ac.tohoku.ecei.sf;

import java.io.IOException;

public class MultiPlay{

	private static String usage() {
		return ("Usage: java -jar jikkenC-reversi.jar (client HOST PORT|server PORT)");
	}

	private static void startServer(int port) {
		ServerSingle s = null;
		try{
			s = new ServerSingle(port);
			s.waitConnection();
			s.close();
		}
		catch(Exception e) {
			System.out.println("Starting server failed.");
		}

		try{
			if (s != null) s.close();
		}
		catch (Exception e) {}
	}

	private static void startClient(String host, int port) {
		Player you = null;
		Player they = null;

		try{
			they = new RemotePlayer(host, port);
			you = new HumanPlayer();

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
		if (args[0].startsWith("c")){
			if (args.length < 3){
				System.out.println(usage());
				return;
			}
			String host = args[1];
			int    port = Integer.parseInt(args[2]);

			startClient(host, port);
		}
		else {
			if (args.length < 2){
				System.out.println(usage());
			}
			int port = Integer.parseInt(args[1]);

			startServer(port);
		}
	}
}

