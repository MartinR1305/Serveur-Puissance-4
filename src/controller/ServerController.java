package controller;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.util.Enumeration;
import java.util.ResourceBundle;

import application.Server;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ServerController implements Initializable {

	@FXML
	Label server, ipAddress, valueIPAddress, port, valuePort, playersConnected, nbPlayersConnected, labelNewPort;

	@FXML
	TextField newPort;

	@FXML
	Button confirm;
	
	private static int nbClient;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		valuePort.setText(String.valueOf(Server.getPort())); 
		nbPlayersConnected.setText(String.valueOf(Server.getNbClient()));
		this.updateNbClient();
		this.updateColorNbClient();

		// Here we take the current address IP in order to display it
		try {

			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
			while (interfaces.hasMoreElements()) {
				NetworkInterface networkInterface = interfaces.nextElement();
				// Check if it's a WiFi interface
				if (networkInterface.getName().startsWith("wlan")) {
					Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
					while (addresses.hasMoreElements()) {
						InetAddress address = addresses.nextElement();
						// Checks if it's a non-looped IPv4 address
						if (address.getAddress().length == 4 && !address.isLoopbackAddress()) {
							valueIPAddress.setText(address.getHostAddress());
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method that allows to change the port of the server
	 * 
	 * @throws IOException
	 */
	public void changeThePort() throws IOException {

		int numPort = Integer.valueOf(newPort.getText());
		valuePort.setText(newPort.getText());
		newPort.clear();

		Server.changePort(numPort);
	}
	
	/**
	 * Method that allows to actualize the client's number on the server
	 */
	public void actualizeNbClient() {
		nbClient = Server.getNbClient();
	}
	
	 /**
	 * Method that allows to update the client's number on the server
	 */
	public void updateNbClient() {
	    Thread stateUpdateThread = new Thread(() -> {
	        while (true) {
	            try {
	                Thread.sleep(500);
	                Platform.runLater(() -> {
	                	nbPlayersConnected.setText(String.valueOf(nbClient));
	                });
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	        }
	    });

	    stateUpdateThread.setDaemon(true);
	    stateUpdateThread.start();
	}
	
	 /**
	 * Method that allows to update the color of the label numberClient
	 */
	public void updateColorNbClient() {
	    Thread thread = new Thread(() -> {
	        while (true) {
	            try {
	                Thread.sleep(500);
	                Platform.runLater(() -> {
	                	if(nbClient < 2) {
	                		nbPlayersConnected.setStyle("-fx-text-fill: red;");
	                	}
	                	
	                	else if (nbClient == 2) {
	                		nbPlayersConnected.setStyle("-fx-text-fill: green;");
	                	}
	                });
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	        }
	    });
	    thread.setDaemon(true);
	    thread.start();
	}
}
