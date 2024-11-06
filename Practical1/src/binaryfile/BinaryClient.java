package binaryfile;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class BinaryClient {

	public static void main(String[] args) {
		String serverAddress = "127.0.0.1"; // Server address (localhost)
		int port = 55555; // Server port
		String outputDirectory = "C:\\Users\\catha\\Downloads"; // Directory to save the received file

		try (Socket socket = new Socket(serverAddress, port);
				BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
				InputStream in = socket.getInputStream()) {

			System.out.println("Connected to server.");

			// Read available files
			System.out.println("Available files for download:");
			String fileName;
			while (!(fileName = reader.readLine()).equals("END")) {
				System.out.println(fileName);
			}

			try (// Get user input to select a file
			Scanner scanner = new Scanner(System.in)) {
				System.out.print("Enter the name of the file you want to download: ");
				String selectedFile = scanner.nextLine();
				writer.println(selectedFile); // Send file choice to server

				// Save the received file
				String outputFilePath = outputDirectory + File.separator + selectedFile;
				try (FileOutputStream fileOut = new FileOutputStream(outputFilePath);
						BufferedOutputStream bufferOut = new BufferedOutputStream(fileOut)) {

					byte[] buffer = new byte[4096];
					int bytesRead;
					while ((bytesRead = in.read(buffer)) != -1) {
						bufferOut.write(buffer, 0, bytesRead);
					}

					System.out.println("File " + selectedFile + " downloaded successfully to " + outputFilePath);
				} catch (IOException e) {
					System.err.println("Error saving the file: " + e.getMessage());
				}
			}
		} catch (IOException e) {
			System.err.println("Client error: " + e.getMessage());
		}

	}

}
