package primenumbersolver;

import java.io.*;
import java.net.*;
import java.util.*;

public class PrimeClient {
    private static final String SERVER_ADDRESS = "localhost"; // Replace with server's IP if needed
    private static final int SERVER_PORT = 8123;

    public static void main(String[] args) throws IOException {
        DatagramSocket socket = new DatagramSocket();

        while (true) {
            // Send a request for work
            sendRequest(socket, "REQUEST_WORK");

            // Buffer for receiving responses
            byte[] buffer = new byte[1024];
            DatagramPacket responsePacket = new DatagramPacket(buffer, buffer.length);

            // Receive a response from the server
            socket.receive(responsePacket);
            String serverMessage = new String(responsePacket.getData(), 0, responsePacket.getLength());
            if (serverMessage.equals("NO_WORK_AVAILABLE")) {
                System.out.println("No more work available from the server.");
                break;
            } else {
                // Parse the chunk received (assuming format: [number1, number2, ...])
                List<Integer> chunk = parseChunk(serverMessage);
                List<String> results = new ArrayList<>();

                // Process the chunk to determine if each number is prime
                for (int number : chunk) {
                    boolean isPrime = isPrime(number);
                    results.add("RESULT:" + number + ":" + isPrime);
                }

                // Send results back to the server
                for (String result : results) {
                    sendRequest(socket, result);
                }
            }
        }

        socket.close();
    }

    private static boolean isPrime(int num) {
        if (num <= 1) return false;
        for (int i = 2; i <= Math.sqrt(num); i++) {
            if (num % i == 0) return false;
        }
        return true;
    }

    private static void sendRequest(DatagramSocket socket, String message) throws IOException {
        byte[] buffer = message.getBytes();
        InetAddress serverAddress = InetAddress.getByName(SERVER_ADDRESS);
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, serverAddress, SERVER_PORT);
        socket.send(packet);
    }

    private static List<Integer> parseChunk(String message) {
        // Remove brackets and split by comma
        message = message.replace("[", "").replace("]", "").trim();
        String[] parts = message.split(",");
        List<Integer> numbers = new ArrayList<>();
        for (String part : parts) {
            try {
                numbers.add(Integer.parseInt(part.trim()));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return numbers;
    }
}
