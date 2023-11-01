package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ServerController {
	
	@FXML
	Label server, ipAddress, valueIPAddress, port, valuePort, playersConnected, nbPlayersConnected, labelNewPort;
	
	@FXML
	TextField newPort;
	
	@FXML
	Button confirm;
}
