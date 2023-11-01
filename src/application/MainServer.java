package application;

import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainServer extends Application {
	private Server server;

	public MainServer() throws IOException {
		server = new Server(8070);
		new Thread(() -> server.start()).start();
	}

	/**
	 * Starts the application by displaying the main window
	 * 
	 * @param primaryStage, main window of the application
	 */
	@Override
	public void start(Stage primaryStage) {
		try {

			// Here we start the application
			Parent root = FXMLLoader.load(getClass().getResource(".." + File.separator + "view" + File.separator + "Server.fxml"));
			Scene scene1 = new Scene(root);
			primaryStage.setTitle("Server");
			primaryStage.setScene(scene1);
			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Method that allows to close the server correctly
	 */
	@Override
	public void stop() throws IOException{
		System.out.println("Server is about to close");
		
		if(server != null) {
			server.close();
		}
	}

	/**
	 * Main entry point for the JavaFX application
	 * 
	 * @param args, Command line arguments passed to the application
	 */
	public static void main(String[] args) {
		launch(args);
	}
}