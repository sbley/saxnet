package de.saxsys.saxnet;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

public class SaxnetApplication extends Application
{
    public static Stage stage;
    //private GraphDatabaseService graphDb;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
    public void start(Stage _stage) throws Exception
    {
        stage = _stage;

		graphDb = new GraphDatabaseFactory().newEmbeddedDatabaseBuilder(
				"target/database/location").newGraphDatabase();

		// primaryStage.setTitle("Hello World!");
		// Button btn = new Button();
		// btn.setText("Say 'Hello World'");
		// btn.setOnAction(new EventHandler<ActionEvent>() {
		//
		// @Override
		// public void handle(ActionEvent event) {
		// System.out.println("Hello World!");
		// }
		// });
		//
		// StackPane root = new StackPane();
		// root.getChildren().add(btn);
		// primaryStage.setScene(new Scene(root, 300, 250));
		// primaryStage.show();

		Parent root = FXMLLoader.load(getClass().getResource("saxnet.fxml"));

		stage.setTitle("Saxnet");
		stage.setScene(new Scene(root, 600, 475));
		stage.show();
	}

	@Override
	public void stop() throws Exception {
		super.stop();
		graphDb.shutdown();
	}
}