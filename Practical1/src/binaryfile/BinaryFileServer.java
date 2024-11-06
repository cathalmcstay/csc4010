package binaryfile;

import java.io.*;
import java.net.*;

public class BinaryFileServer {

	public static void main(String[] args) {
		int port = 55555; // Port number to listen on
		String directoryPath = "C:\\Users\\catha\\eclipse-workspace\\CSC4010\\Practical1\\src\\binaryfile\\aslan";

		try (ServerSocket serverSocket = new ServerSocket(port)) {
			System.out.println("Server started. Waiting for connections...");

			while (true) {
				try (Socket clientSocket = serverSocket.accept();
						BufferedReader reader = new BufferedReader(
								new InputStreamReader(clientSocket.getInputStream()));
						PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)) {

					System.out.println("Client connected: " + clientSocket.getInetAddress().getHostName());

					// List available files
					File directory = new File(directoryPath);
					File[] files = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".jpg"));
					if (files != null && files.length > 0) {
						writer.println("Available files:");
						for (File file : files) {
							writer.println(file.getName());
						}
						writer.println("END"); // Signal end of file list
					} else {
						writer.println("No files available for download.");
						continue; // Move on to next client
					}

					// Read client's choice
					String selectedFile = reader.readLine();
					File requestedFile = new File(directoryPath, selectedFile);
					if (requestedFile.exists() && !requestedFile.isDirectory()) {
						// Send the file
						try (FileInputStream fileIn = new FileInputStream(requestedFile);
								BufferedInputStream bufferIn = new BufferedInputStream(fileIn);
								OutputStream out = clientSocket.getOutputStream()) {

							byte[] buffer = new byte[4096];
							int bytesRead;
							while ((bytesRead = bufferIn.read(buffer)) != -1) {
								out.write(buffer, 0, bytesRead);
								out.flush();
							}

							System.out.println("File " + selectedFile + " sent successfully.");
						}
					} else {
						writer.println("File not found.");
					}
				} catch (IOException e) {
					System.err.println("Error while handling client connection: " + e.getMessage());
				}
			}
		} catch (IOException e) {
			System.err.println("Server error: " + e.getMessage());
		}

	}

}
