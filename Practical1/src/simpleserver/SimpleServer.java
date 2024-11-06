package simpleserver;

import java.io.*;
import java.net.*;
import java.util.*;

public class SimpleServer {

	public static void main(String[] args) {
		try {
			System.out.println("Creating scoket...");
			ServerSocket server = new ServerSocket(55550);
			System.out.println("Waiting for request for connection...");
			while (true) {
				Socket connection = server.accept();
				System.out.println("connection established with " + connection.getInetAddress().getHostName());
				Scanner in = new Scanner(connection.getInputStream());
				PrintWriter out = new PrintWriter(connection.getOutputStream());
				System.out.println("find out what the client wants...");
				System.out.println("The client said ’" + in.nextLine() + "’");
				System.out.println("respond to the client");
				out.println("Hello there client!");
				out.flush();
				in.close();
				out.close();
				System.out.println("terminating the connection" + " to the client");
				connection.close();
				System.out.println("terminating the socket");
				server.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("bye bye...");
	}

}
