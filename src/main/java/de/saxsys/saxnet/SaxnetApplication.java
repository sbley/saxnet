package de.saxsys.saxnet;

import java.io.IOException;
import java.io.InputStream;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import fr.brouillard.javafx.weld.StartupScene;

public class SaxnetApplication {
	@Inject
	private FXMLLoader fxmlLoader;

	public void launchJavaFXApplication(@Observes @StartupScene Stage stage) {
		InputStream is = null;

		try {
			is = getClass().getResourceAsStream("saxnet.fxml");
			Parent root = (Parent) fxmlLoader.load(is);
			stage.setTitle("Saxnet");
			stage.setScene(new Scene(root, 600, 475));
			stage.show();
		} catch (IOException e) {
			throw new IllegalStateException("cannot load FXML", e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
				}
			}
		}
	}
}