package de.saxsys.saxnet;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SaxnetApplication extends Application {

	public static Stage stage;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage _stage) throws Exception {
		stage = _stage;
		Parent root = FXMLLoader.load(getClass().getResource("saxnet.fxml"));

		stage.setTitle("Saxnet");
		stage.setScene(new Scene(root, 600, 475));
		stage.show();
	}

	@Override
	public void stop() throws Exception {
		super.stop();
		NeoDB.getInstance().shutdown();
	}
}