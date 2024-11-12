package networkudp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class UDPClient {

	public static void main(String[] args) throws IOException {
		Scanner sc = new Scanner(System.in);
		int port = 8123; // port to communicate over

		// Create a datagram socket
		DatagramSocket ds = new DatagramSocket();

		InetAddress ip = InetAddress.getByName("localhost");
		byte buf[] = null;

		boolean quit = false;
		while (!quit) // loop until a quit condition
		{
			System.out.print("Send (quit to exit): "); // prompt
			// read the input from the user
			String input = sc.nextLine();

			// convert the string into a byte array
			buf = input.getBytes();

			// create a datagram packet to send
			DatagramPacket packet = new DatagramPacket(buf, buf.length, ip, port);

			// and send it
			ds.send(packet);

			// break the loop if user enters "bye"
			if (input.equalsIgnoreCase("quit"))
				quit = true;
		}

		ds.close();
		sc.close();
	}

}
