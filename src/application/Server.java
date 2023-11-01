package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import controller.ServerController;

public class Server implements AutoCloseable{

	private static ServerSocket serverSocket;
	private static List<Socket> listClient;
	private ServerController serverController;
	
	private static boolean serverIsRunning;
	private static boolean portChange;
	
	private static int numPort;
	private static int nbClient;

	/**
	 * Constructor for the server with the server
	 * 
	 * @param port
	 * @throws IOException
	 */
	public Server(int port, ServerController controller) throws IOException {
		try {

			serverSocket = new ServerSocket(port);
			numPort = port;
			nbClient = 0;
			listClient = new ArrayList<>();
			serverIsRunning = false;
			serverController = controller;

		} catch (IOException IOE) {

			System.err.println("Address already in use");
			throw IOE;
		}
	}

	/**
	 * Getter for the port of the server
	 * 
	 * @return
	 */
	public static int getPort() {
		return numPort;
	}
	
	/**
	 * Getter for the client's number of the server
	 * 
	 * @return
	 */
	public static int getNbClient() {
		return nbClient;
	}

	/**
	 * Method that starts a server
	 */
	public void start() {
		System.out.println("Waiting for client connection");
		serverIsRunning = true;

		// We accept all connections from clients while the server is running
		while (serverIsRunning) {
			try {
				// We also check that there is not a port's change at the moment
				if (!portChange) {
					
					Socket clientSocket = serverSocket.accept();
					System.out.println("Client connected : " + clientSocket.getInetAddress());
					
					// We add the client and actualize data on server
					listClient.add(clientSocket);
					nbClient++;
					serverController.actualizeNbClient();
					
					PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
					writer.println("N" + String.valueOf(nbClient));
					
					Thread clientThread = new Thread(() -> handleClient(clientSocket));
					clientThread.start();
					
				}
			} catch (IOException IOE) {
				System.err.println("Server Closed");
			}
		}
	}
	
	/**
     * Method for read and update data 
     * @param clientSocket
     */
    private void handleClient(Socket clientSocket) {
        try (
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
        ) {
            //While the server is open -> read messages
            while (serverIsRunning) {
                String inputLine = reader.readLine();
                
                if (inputLine != null) {
                    //Help the client to close the thread of reconnection
                    if (inputLine.equals("STOP")){
                        writer.println("STOP");
                        clientSocket.close ();
                        listClient.remove(clientSocket);
                        nbClient--;
                        serverController.actualizeNbClient();
                        System.out.println("Client disconnected : " + clientSocket.getInetAddress());
                    }
                }
                else {
                    clientSocket.close ();
                    listClient.remove(clientSocket);
                    nbClient--;
                    serverController.actualizeNbClient();
                    System.out.println("Client disconnected : " + clientSocket.getInetAddress());
                }
            }

        } catch (IOException IOE) {
            System.err.println("Client Disconnected !");

        } 
    }
	
	/**
	 * Method that allows to change the port of a server
	 * 
	 * @param newPort
	 * @throws IOException
	 */
	public static void changePort(int newPort) throws IOException {
		portChange = true;
		
		// We close all the connections with clients
		if (listClient != null) {

			for (Socket clientSocket : listClient) {

				// We check if the socket is valid or not
				if (clientSocket != null && !clientSocket.isClosed()) {

					// Send the message "STOP" to the client
					PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
					writer.println("STOP");

					clientSocket.close();
				}
			}
		}
		
		// We close the server's socket
		if (serverSocket != null && !serverSocket.isClosed()) {
			serverSocket.close();
			System.out.println("Closing of the server");
		}
		
		// We recreate a new socket for the server with the new port
		serverSocket = new ServerSocket(newPort);
		numPort = newPort;
		listClient = new ArrayList<>();
		
		System.out.println("Waiting for client connection");
		
		portChange = false;
	}

	/**
	 * Method that allows to close the server and all
	 * 
	 * @throws IOException
	 */
	public void close() throws IOException {
		serverIsRunning = false;

		if (listClient != null) {
			for (Socket clientSocket : listClient) {

				// We check if the socket is valid or not
				if (clientSocket != null && !clientSocket.isClosed()) {

					// Send the message "STOP" to the client
					PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
					writer.println("STOP");

					clientSocket.close();
				}
			}
		}

		// We close the server's socket
		if (serverSocket != null && !serverSocket.isClosed()) {
			serverSocket.close();
			System.out.println("Closing of the server");
		}
	}
}