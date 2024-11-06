package montecarlo;

import java.io.*;
import java.net.*;
import java.util.Random;

public class MonteClient {

	private static final String SERVER_ADDRESS = "127.0.0.1"; // Change as needed
	private static final int PORT = 55555;

	public static void main(String[] args) {
		try (Socket socket = new Socket(SERVER_ADDRESS, PORT);
				BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {

			// Receive chunk size from server
			int chunkSize = Integer.parseInt(reader.readLine());
			System.out.println("Received chunk size: " + chunkSize);

			// Perform Monte Carlo simulation
			int pointsInCircle = 0;
			Random random = new Random();
			for (int i = 0; i < chunkSize; i++) {
				double x = random.nextDouble() * 2 - 1; // Random x between -1 and 1
				double y = random.nextDouble() * 2 - 1; // Random y between -1 and 1
				if (x * x + y * y <= 1) {
					pointsInCircle++;
				}
			}

			// Send results to server
			writer.println(pointsInCircle);
			writer.println(chunkSize);
			System.out.println("Results sent to server.");
		} catch (IOException | NumberFormatException e) {
			System.err.println("Client error: " + e.getMessage());
		}
	}

}
