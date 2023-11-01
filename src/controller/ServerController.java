package controller;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.util.Enumeration;
import java.util.ResourceBundle;

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

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
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
}
