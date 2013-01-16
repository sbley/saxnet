package de.saxsys.saxnet;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class SaxnetController implements Initializable {
	@FXML
	private ListView<String> listEmployees;

	@FXML
	private TextField txtName;

	@Override
	public void initialize(URL url, ResourceBundle bundle) {
		insertEmployees();
	}

	public void insertEmployees() {
		listEmployees.getItems().addAll("Sebastian", "Alex");
	}

	@FXML
	private void handleCreateEmployee() {
		listEmployees.getItems().add(txtName.getText());
	}

}
