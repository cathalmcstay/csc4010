package simpleserver;

import java.io.*;
import java.net.*;
import java.util.*;

public class SimpleClient {

	public static void main(String[] args) {
		try {
			// For connecting to a hostname we would first resolve it:
			// InetAddress server = InetAddress.getByName("server.qub.ac.uk");
			// And then use the InetAddress in the Socket constructor i.e.
			// new Socket(server, 55550);
			Socket connection = new Socket("127.0.0.1", 55550);
			System.out.println("connected to server");
			Scanner in = new Scanner(connection.getInputStream());
			PrintWriter out = new PrintWriter(connection.getOutputStream());
			System.out.println("sending a greeting to the server...");
			out.println("Hello there server!");
			out.flush();
			System.out.println("waiting for a response from the server...");
			System.out.println("The server said ’" + in.nextLine() + "’");
			in.close();
			out.close();
			System.out.println("closing the connection to the server...");
			connection.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
