package Task3;

import java.io.*;
import java.net.*;

public class Server {
	
	public static void main(String[] args) {
		
		try {
			
			ServerSocket ss = new ServerSocket(3333);
			int clientNo = 0;
			
			System.out.println("Server has started successfully");
			
			while (true) {
				
				clientNo++;
				Socket s = ss.accept();
				System.out.println("Client No: " + clientNo + " started communication!");
				ThreadedServer ts = new ThreadedServer(s, clientNo);
				ts.start();
				
			}
			
		}
		
		catch(Exception e) {
			System.out.println(e);
		}
	}

}
