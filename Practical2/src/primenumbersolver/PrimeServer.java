package primenumbersolver;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class PrimeServer {
	private static final int PORT = 8123; // Port for communication
	private static final int CHUNK_SIZE = 10; // Number of elements to send in a chunk

	// Use a thread-safe structure to manage chunks
	private static final List<Integer> numbers = Collections.synchronizedList(new ArrayList<>());
	private static final Map<Integer, String> primeHashMap = new ConcurrentHashMap<>();

	public static void main(String[] args) throws IOException {
		Random rand = new Random();
		int n = 1000000; // Number of random numbers to generate

		// Populate the list and the map
		for (int i = 0; i < n; i++) {
			int randNum = rand.nextInt(n * 100);
			numbers.add(randNum);
			primeHashMap.put(randNum, "Untested");
		}

		DatagramSocket socket = new DatagramSocket(PORT);
		System.out.println("PrimeServer is running...");

		// Listening loop
		while (true) {
			try {
				// Receive a request from a client
				byte[] buffer = new byte[1024];
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
				socket.receive(packet);

				String clientMessage = new String(packet.getData(), 0, packet.getLength());
				InetAddress clientAddress = packet.getAddress();
				int clientPort = packet.getPort();

				if (clientMessage.equals("REQUEST_WORK")) {
					List<Integer> chunk = getChunk(CHUNK_SIZE);
					if (chunk.isEmpty()) {
						sendResponse(socket, "NO_WORK_AVAILABLE", clientAddress, clientPort);
					} else {
						sendResponse(socket, chunk.toString(), clientAddress, clientPort);
					}
				} else if (clientMessage.startsWith("RESULT")) {
					processResult(clientMessage);
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// Method to retrieve a chunk of numbers from the list
	private static synchronized List<Integer> getChunk(int chunkSize) {
		List<Integer> chunk = new ArrayList<>();
		for (int i = 0; i < chunkSize && !numbers.isEmpty(); i++) {
			chunk.add(numbers.remove(0));
		}
		return chunk;
	}

	// Method to process results from clients and update the map
	private static void processResult(String result) {
		// Expected format: "RESULT:number:isPrime"
		String[] parts = result.split(":");
		if (parts.length == 3 && parts[0].equals("RESULT")) {
			int number = Integer.parseInt(parts[1]);
			boolean isPrime = Boolean.parseBoolean(parts[2]);
			primeHashMap.put(number, isPrime ? "Prime" : "Not Prime");
			System.out.println("Updated: " + number + " -> " + (isPrime ? "Prime" : "Not Prime"));
		}
	}

	// Helper method to send responses to clients
	private static void sendResponse(DatagramSocket socket, String message, InetAddress address, int port)
			throws IOException {
		byte[] buffer = message.getBytes();
		DatagramPacket responsePacket = new DatagramPacket(buffer, buffer.length, address, port);
		socket.send(responsePacket);
	}
}
