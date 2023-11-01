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
		
		System.out.println("Lancement server : ");
		server = new Server (8080);
		new Thread (() -> server.start()).start();
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
	
	@Override
    public void stop() throws IOException {
       // Clean up and release resources before the application exits
       System.out.println("Application is about to exit");

       // Close the server
       if (server != null) {
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