package Task1;

import java.io.*;
import java.net.*;

public class Client {
	public static void main(String args[]) throws Exception {
		Socket s = new Socket("localhost", 3333);
		DataInputStream din = new DataInputStream(s.getInputStream());
		DataOutputStream dout = new DataOutputStream(s.getOutputStream());
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

			String str = din.readUTF();
			System.out.println("Server says: " + str);
			
			String x = br.readLine();
			dout.writeUTF(x);
			
			String y = din.readUTF();
			System.out.println("According to server, factorial of " + x + " is " + y);
			dout.flush();

		dout.close();
		s.close();
	}
}