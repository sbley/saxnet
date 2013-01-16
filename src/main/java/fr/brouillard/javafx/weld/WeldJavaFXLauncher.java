package fr.brouillard.javafx.weld;

import javafx.application.Application;
import javafx.stage.Stage;

import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

import de.saxsys.saxnet.db.Neo4J;

public class WeldJavaFXLauncher extends Application {

	@Inject
	private Neo4J neo4j;

	/**
	 * Nothing special, we just use the JavaFX Application methods to boostrap
	 * JavaFX
	 */
	public static void main(String[] args) {
		Application.launch(WeldJavaFXLauncher.class, args);
	}

	@SuppressWarnings("serial")
	@Override
	public void start(final Stage primaryStage) throws Exception {
		// Let's initialize CDI/Weld.
		WeldContainer weldContainer = new Weld().initialize();
		// Make the application parameters injectable with a standard CDI
		// annotation
		weldContainer.instance().select(ApplicationParametersProvider.class)
				.get().setParameters(getParameters());
		// Now that JavaFX thread is ready
		// let's inform whoever cares using standard CDI notification mechanism:
		// CDI events
		weldContainer.event()
				.select(Stage.class, new AnnotationLiteral<StartupScene>() {
				}).fire(primaryStage);
	}
}
