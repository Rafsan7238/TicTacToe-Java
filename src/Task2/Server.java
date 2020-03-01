package Task2;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.Executors;

public class Server {

	public static void main(String[] args) throws Exception {
		try (ServerSocket ss = new ServerSocket(3333)) {
			System.out.println("Tic Tac Toe Server is Running...");
			var pool = Executors.newFixedThreadPool(200);
			while (true) {
				Khela game = new Khela();
				pool.execute(game.new Khelowar(ss.accept(), 'X'));
				pool.execute(game.new Khelowar(ss.accept(), 'O'));
			}
		}
	}
}

class Khela {

	private Khelowar[] board = new Khelowar[9];

	Khelowar currentKhelowar;

	public boolean keuJitse() {
		return (board[0] != null && board[0] == board[1] && board[0] == board[2])
				|| (board[3] != null && board[3] == board[4] && board[3] == board[5])
				|| (board[6] != null && board[6] == board[7] && board[6] == board[8])
				|| (board[0] != null && board[0] == board[3] && board[0] == board[6])
				|| (board[1] != null && board[1] == board[4] && board[1] == board[7])
				|| (board[2] != null && board[2] == board[5] && board[2] == board[8])
				|| (board[0] != null && board[0] == board[4] && board[0] == board[8])
				|| (board[2] != null && board[2] == board[4] && board[2] == board[6]);
	}

	public boolean bhoreGese() {
		return Arrays.stream(board).allMatch(p -> p != null);
	}

	public synchronized void move(int location, Khelowar player) {
		if (player != currentKhelowar) {
			throw new IllegalStateException("Koybar marte chao -.-");
		} else if (player.opponent == null) {
			throw new IllegalStateException("Ki re bhai wait. Kar shathe khelba?");
		} else if (board[location] != null) {
			throw new IllegalStateException("Onner ghar ey boshteso keno?");
		}
		board[location] = currentKhelowar;
		currentKhelowar = currentKhelowar.opponent;
	}

	class Khelowar implements Runnable {
		char mark;
		Khelowar opponent;
		Socket s;
		Scanner din;
		PrintWriter dout;

		public Khelowar(Socket socket, char mark) {
			s = socket;
			this.mark = mark;
		}

		@Override
		public void run() {
			try {
				shuru();
				commandMano();
			} 
			
			catch (Exception e) {
				e.printStackTrace();
			} 
			
			finally {
				if (opponent != null && opponent.dout != null) {
					opponent.dout.println("OTHER_PLAYER_LEFT");
				}
				try {
					s.close();
				} 
				catch (IOException e) {
				}
			}
		}

		private void shuru() throws IOException {
			din = new Scanner(s.getInputStream());
			dout = new PrintWriter(s.getOutputStream(), true);
			dout.println("WELCOME " + mark);
			if (mark == 'X') {
				currentKhelowar = this;
				dout.println("MESSAGE Player khujtesi wait...");
			} else {
				opponent = currentKhelowar;
				opponent.opponent = this;
				opponent.dout.println("MESSAGE Your move");
			}
		}

		private void commandMano() {
			while (din.hasNextLine()) {
				var command = din.nextLine();
				if (command.startsWith("QUIT")) {
					return;
				} else if (command.startsWith("MOVE")) {
					processMoveCommand(Integer.parseInt(command.substring(5)));
				}
			}
		}

		private void processMoveCommand(int location) {
			try {
				move(location, this);
				dout.println("VALID_MOVE");
				opponent.dout.println("OPPONENT_MOVED " + location);
				if (keuJitse()) {
					dout.println("VICTORY");
					opponent.dout.println("DEFEAT");
				} else if (bhoreGese()) {
					dout.println("TIE");
					opponent.dout.println("TIE");
				}
			} catch (IllegalStateException e) {
				dout.println("MESSAGE " + e.getMessage());
			}
		}
	}
}