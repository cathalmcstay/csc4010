package montecarlo;

import java.io.*;
import java.net.*;
import java.util.concurrent.atomic.AtomicInteger;

public class MonteServer {

	private static final int PORT = 55555;
	private static AtomicInteger pointsWithinCircle = new AtomicInteger(0);
	private static AtomicInteger totalPoints = new AtomicInteger(0);

	public static void main(String[] args) {
		try (ServerSocket serverSocket = new ServerSocket(PORT)) {
			System.out.println("Monte Carlo Server started. Waiting for clients...");

			while (true) {
				try (Socket clientSocket = serverSocket.accept();
						BufferedReader reader = new BufferedReader(
								new InputStreamReader(clientSocket.getInputStream()));
						PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)) {

					System.out.println("Client connected: " + clientSocket.getInetAddress().getHostName());

					// Send parameters (e.g., radius and chunk size)
					int chunkSize = 10000; // Example chunk size
					writer.println(chunkSize);

					// Receive results from client
					int pointsInCircle = Integer.parseInt(reader.readLine());
					int totalPointsReceived = Integer.parseInt(reader.readLine());

					// Update counts
					pointsWithinCircle.addAndGet(pointsInCircle);
					totalPoints.addAndGet(totalPointsReceived);

					// Calculate Pi
					double piEstimate = 4.0 * pointsWithinCircle.get() / totalPoints.get();
					System.out.println("Current Pi Estimate: " + piEstimate);
				} catch (IOException | NumberFormatException e) {
					System.err.println("Error handling client connection: " + e.getMessage());
				}
			}
		} catch (IOException e) {
			System.err.println("Server error: " + e.getMessage());
		}
	}

}
