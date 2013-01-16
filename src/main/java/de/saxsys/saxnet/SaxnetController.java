package de.saxsys.saxnet;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Application.Parameters;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;

import de.saxsys.saxnet.db.Neo4J;
import de.saxsys.saxnet.fx.AddPopup;
import de.saxsys.saxnet.fx.AddPopupCallback;
import fr.brouillard.javafx.weld.StartupScene;

public class SaxnetController implements Initializable {
	@FXML
	private ListView<String> listEmployees;

	@FXML
	private ListView<String> listNotRelativeEmployees;

	@FXML
	private ListView<String> listRelativeEmployees;

	@FXML
	private TextField txtName;

	@FXML
	private Button btnCreate;

	private Stage stage;

	@Inject
	private Parameters applicationParameters;

	@Inject
	private Neo4J neo4j;

	private static enum RelTypes implements RelationshipType {
		WORKS_AT, KNOWS
	}

	@Override
	public void initialize(URL url, ResourceBundle bundle) {
		loadEmployees();

		listEmployees.getSelectionModel().selectedItemProperty()
				.addListener(new InvalidationListener() {
					@Override
					public void invalidated(Observable observable) {
						ReadOnlyObjectProperty<String> prop = (ReadOnlyObjectProperty<String>) observable;
						String employee = prop.getValue();
						showRelatedEmployees(employee);
						// TODO showNotRelatedEmployees(employee);
					}
				});
	}

	public void loadEmployees() {
		Node node1 = NeoDB.getInstance().getNodeById(0);
		Iterable<Relationship> relationships = node1.getRelationships();
		Iterable<Relationship> relationships = node1
				.getRelationships(RelTypes.WORKS_AT);
		for (Relationship rel : relationships) {
			listEmployees.getItems().add(
					(String) rel.getStartNode().getProperty("name"));
		}
	}

	@FXML
	public void handleCreateEmployee() {
		AddPopupCallback callback = new AddPopupCallback() {
			@Override
			public void valueChanged(String value) {
				createEmployee(value);
			}
		};
		AddPopup popup = new AddPopup(this, "", callback);
		popup.show(stage);

		double x = popup.getOwnerWindow().getX() + btnCreate.getLayoutX();
		double y = popup.getOwnerWindow().getY() + btnCreate.getLayoutY()
				+ btnCreate.getHeight() - 0;

		if (!popup.isShowing()) {
			popup.show(popup.getOwnerWindow());
		}

		popup.show(btnCreate, x, y);

	}

	public void createEmployee(String name) {
		Transaction tx = neo4j.db().beginTx();
		Node node = neo4j.db().createNode();
		Node companyNode = neo4j.db().getNodeById(0);
		node.setProperty("name", name);
		node.createRelationshipTo(companyNode, RelTypes.WORKS_AT);
		tx.success();
		tx.finish();
		listEmployees.getItems().add(name);
	}

	@FXML
	public void handleEditEmployee() {
		final String oldName = listEmployees.getSelectionModel()
				.getSelectedItem();

		AddPopupCallback callback = new AddPopupCallback() {
			@Override
			public void valueChanged(String newName) {
				editEmployee(oldName, newName);
			}
		};
		AddPopup popup = new AddPopup(this, oldName, callback);
		popup.show(stage);

		double x = popup.getOwnerWindow().getX() + btnCreate.getLayoutX();
		double y = popup.getOwnerWindow().getY() + btnCreate.getLayoutY()
				+ btnCreate.getHeight() - 0;

		if (!popup.isShowing()) {
			popup.show(popup.getOwnerWindow());
		}

		popup.show(btnCreate, x, y);

	}

	public void editEmployee(String oldName, String newName) {
		listEmployees.getItems().set(listEmployees.getItems().indexOf(oldName),
				newName);
	}

	@FXML
	public void handleRemoveEmployee() {
		listEmployees.getItems().remove(this.getSelectedEmployee());
		Transaction tx = NeoDB.getInstance().beginTx();
		Node companyNode = NeoDB.getInstance().getNodeById(0);
		for (Relationship relWorksAt : companyNode.getRelationships()) {
			if (this.getSelectedEmployee().equals(
					(String) relWorksAt.getStartNode().getProperty("name"))) {
				for (Relationship rel : relWorksAt.getStartNode()
						.getRelationships()) {
					rel.delete();
				}
				relWorksAt.getStartNode().delete();
				break;
			}
		}
		tx.success();
		tx.finish();
	}

	/**
	 * Stellt Verbindung her aus ausgewählten Employee (linke Liste) und rechten
	 * unteren Liste
	 */
	@FXML
	public void handleCreateRelation() {
		Transaction tx = NeoDB.getInstance().beginTx();
		Node companyNode = NeoDB.getInstance().getNodeById(0);
		Node selectedEmployeeNode = null;
		Node selectedNotRelatedEmployeeNode = null;
		for (Relationship relWorksAt : companyNode.getRelationships()) {
			if (this.getSelectedEmployee().equals(
					(String) relWorksAt.getStartNode().getProperty("name"))) {
				selectedEmployeeNode = relWorksAt.getStartNode();
			} else if (this.getSelectedNotRelatedEmployee().equals(
					(String) relWorksAt.getStartNode().getProperty("name"))) {
				selectedNotRelatedEmployeeNode = relWorksAt.getStartNode();
			}
		}

		selectedEmployeeNode.createRelationshipTo(
				selectedNotRelatedEmployeeNode, RelTypes.KNOWS);
		tx.success();
		tx.finish();

	}

	@FXML
	public void handleRemoveRelation() {
		String employee = this.getSelectedEmployee();
		String relatedPerson = this.getSelectedRelatedEmployee();

		System.out.println("relatedPerson = " + relatedPerson);

		// TODO: remove relation in DB

	}

	public void showRelatedEmployees(String employee) {
		System.out.println("show relative employees to " + employee);
		listRelativeEmployees.getItems().clear();
		if (null != employee) {
			Transaction tx = neo4j.db().beginTx();
			Node companyNode = neo4j.db().getNodeById(0);
			for (Relationship rel : companyNode.getRelationships()) {
				if (employee.equals((String) rel.getStartNode().getProperty(
						"name"))) {
					Iterable<Relationship> relKnows = rel.getStartNode()
							.getRelationships(RelTypes.KNOWS,
									Direction.OUTGOING);
					for (Relationship relK : relKnows) {
						listRelativeEmployees.getItems().add(
								(String) relK.getEndNode().getProperty("name"));
					}
					break;
				}
			}
			tx.success();
			tx.finish();
		}
	}

	public String getSelectedNotRelatedEmployee() {
		return this.listNotRelativeEmployees.getSelectionModel()
				.getSelectedItem();
	}

	public String getSelectedRelatedEmployee() {
		return this.listRelativeEmployees.getSelectionModel().getSelectedItem();
	}

	public String getSelectedEmployee() {
		return listEmployees.getSelectionModel().getSelectedItem();
	}

	public void setStage(@Observes @StartupScene Stage stage) {
		this.stage = stage;
	}
}
