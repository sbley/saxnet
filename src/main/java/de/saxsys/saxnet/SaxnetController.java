package de.saxsys.saxnet;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

public class SaxnetController implements Initializable {

	@FXML
	private ListView<String> listEmployees;

	@FXML
	private TextField txtName;

	private EmployeeRepository employeeRepo;

	@Override
	public void initialize(URL url, ResourceBundle bundle) {
		insertEmployees();
	}

	public void insertEmployees() {
		// "Sebastian", "Alex"
		Transaction tx = NeoDB.getInstance().beginTx();
		Node node1 = NeoDB.getInstance().getNodeById(1);
		tx.finish();
		listEmployees.getItems().addAll(
				new String[] { (String) node1.getProperty("name") });
	}

	@FXML
	private void handleCreateEmployee() {
		listEmployees.getItems().add(txtName.getText());
	}

}
