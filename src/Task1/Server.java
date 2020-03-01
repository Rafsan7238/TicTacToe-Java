package Task1;

import java.io.*;
import java.net.*;

public class Server {
	public static void main(String args[]) throws Exception {
		ServerSocket ss = new ServerSocket(3333);
		Socket s = ss.accept();
		DataInputStream din = new DataInputStream(s.getInputStream());
		DataOutputStream dout = new DataOutputStream(s.getOutputStream());

		dout.writeUTF("Give me a number please");
		int num = Integer.parseInt(din.readUTF());
		System.out.println("Client says: Give me factorial of " + num);
		int factorial = 1;
		
		for(int i=2; i<=num; i++) {
			factorial = factorial * i;
		}
		
		String str2 = factorial + "";
		
		dout.writeUTF(str2);
		dout.flush();

		din.close();
		s.close();
		ss.close();
	}
}