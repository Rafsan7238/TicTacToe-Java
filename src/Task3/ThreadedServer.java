package Task3;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class ThreadedServer extends Thread {

	Socket s;
	int clientNo;

	public ThreadedServer(Socket in, int n) {

		s = in;
		clientNo = n;

	}

	public void run() {

		try {

			DataInputStream din = new DataInputStream(s.getInputStream());
			DataOutputStream dout = new DataOutputStream(s.getOutputStream());
			String str = "", str2 = "";

			while (!str.equals("stop")) {
				str = din.readUTF();
				System.out.println("Client: " + clientNo + " wants the uppercased version of: " + str);
				str2 = str.toUpperCase();
				str2 = "The uppercased version is: " + str2;
				dout.writeUTF(str2);
				dout.flush();
			}

			din.close();
			dout.close();
			s.close();

		}

		catch (Exception e) {
			System.out.println(e);
		}
		
		finally {
			System.out.println("Client: " + clientNo + " exit!! ");
		}

	}
}
