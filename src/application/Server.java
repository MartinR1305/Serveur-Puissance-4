package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server implements AutoCloseable {
	private static ServerSocket serverSocket;

	private static boolean isRunning;
	private static boolean portChange;

	private static List<Socket> clientConnections;

	/**
	 * Method Initialize information for start the server
	 * 
	 * @param port
	 * @throws IOException
	 */
	public Server(int port) throws IOException {
		try {
			serverSocket = new ServerSocket(port);

			isRunning = false;
			clientConnections = new ArrayList<>();

		} catch (IOException e) {
			System.err.println("Address already in use!");
			throw e;
		}
	}

	/**
	 * Method for start the server
	 */
	public void start() {
		System.out.println("Waiting for client connections...");
		isRunning = true;

		// accepts as many client as there are requests
		while (isRunning) {
			try {
				if (!portChange) {
					Socket clientSocket = serverSocket.accept();
					System.out.println("Client connected: ");

					clientConnections.add(clientSocket);

					// start a thread for handle clients
					Thread clientThread = new Thread(() -> handleClient(clientSocket));
					clientThread.start();
				}

			} catch (IOException IOE) {
				System.err.println("Serveur closed !");
			}
		}
	}

	/**
	 * Method for read and update data
	 * 
	 * @param clientSocket
	 */
	private void handleClient(Socket clientSocket) {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)) {
			// While the server is open -> read messages
			while (isRunning) {
				String inputLine = reader.readLine();

				if (inputLine != null) {
					// Message receive from client (get data for update FXML page)
					if (inputLine.equals("START")) {

					}
					// Help the client to close the thread of reconnection
					else if (inputLine.equals("STOP")) {
						writer.println("STOP");
						clientSocket.close();
						clientConnections.remove(clientSocket);
					} else {
						// Scoring and update data
						sendIntColumn(inputLine);

					}
				} else {
					clientSocket.close();
					clientConnections.remove(clientSocket);
				}
			}

		} catch (IOException IOE) {
			System.err.println("Client Disconnected !");

		} finally {
			try {
				clientSocket.close();
				clientConnections.remove(clientSocket);

				System.out.println("Client disconnected: " + clientSocket.getInetAddress());
			} catch (IOException IOE) {
				System.err.println("Error closing client socket: " + IOE.getMessage());
			}
		}
	}

	/**
	 * Method using for changing Server ports
	 * 
	 * @param newPort
	 * @throws IOException
	 */
	public static void ChangePort(int newPort) throws IOException {
		portChange = true;

		// Close all connections with clients
		for (Socket clientSocket : clientConnections) {
			if (clientSocket != null && !clientSocket.isClosed()) {
				PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
				writer.println("STOP");

				clientSocket.close();
			}
		}

		// Close socket
		if (serverSocket != null && !serverSocket.isClosed()) {
			serverSocket.close();
			System.out.println("Server socket closed.");
		}

		// ReCreate a new Socket with new Port
		serverSocket = new ServerSocket(newPort);
		clientConnections = new ArrayList<>();

		System.out.println("Waiting for client connections...");

		portChange = false;
	}

	/**
	 * Method for close every connection when application is closing
	 */
	@Override
	public void close() throws IOException {
		isRunning = false;

		for (Socket clientSocket : clientConnections) {
			if (clientSocket != null && !clientSocket.isClosed()) {
				PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
				writer.println("STOP");

				clientSocket.close();
			}
		}

		if (serverSocket != null && !serverSocket.isClosed()) {
			serverSocket.close();
			System.out.println("Server socket closed.");
		}
	}

	/**
	 * Method for update score of employee
	 * 
	 * @param messageClient
	 */
	private void sendIntColumn(String messageClient) {
		if (isRunning && !serverSocket.isClosed()) {

			String[] message_sep = messageClient.split(";");
			System.out.println(message_sep[0]);

			// Send the column played

		}
	}
}