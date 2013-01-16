package de.saxsys.saxnet;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class SaxnetController implements Initializable
{
    @FXML
    private ListView<String> listEmployees;

    @FXML
    private ListView<String> listRelativeEmployees;

    @FXML
    private TextField txtName;

    @FXML
    private Button btnCreate;


    @Override
    public void initialize(URL url, ResourceBundle bundle)
    {
        insertEmployees();
	public void insertEmployees() {
		// "Sebastian", "Alex"
		Transaction tx = NeoDB.getInstance().beginTx();
		Node node1 = NeoDB.getInstance().getNodeById(0);
		node1.getRelationships(RelationShipType.)
		tx.success();
		listEmployees.getItems().addAll(
				new String[] { (String) node1.getProperty("name") });
	}

        listEmployees.getSelectionModel().selectedItemProperty().addListener(new InvalidationListener()
        {
            @Override
            public void invalidated(Observable observable)
            {
                ReadOnlyObjectProperty prop = (ReadOnlyObjectProperty) observable;
                String employee = (String) prop.getValue();
                showRelatedEmployees(employee);
            }
        });
    }

    public void insertEmployees()
    {
        //listEmployees.getItems().addAll("Sebastian", "Alex");
        Transaction tx = NeoDB.getInstance().beginTx();
        Node node1 = NeoDB.getInstance().getNodeById(1);
        tx.success();
        listEmployees.getItems().addAll(
                new String[] { (String) node1.getProperty("name") });
    }

    @FXML
    public void handleCreateEmployee()
    {
        AddPopupCallback callback = new AddPopupCallback()
        {
            @Override
            public void valueChanged(String value)
            {
                createEmployee(value);
            }
        };
        AddPopup popup = new AddPopup(this, "", callback);
        popup.show(SaxnetApplication.stage);

        double x = popup.getOwnerWindow().getX() + btnCreate.getLayoutX();
        double y = popup.getOwnerWindow().getY() + btnCreate.getLayoutY() + btnCreate.getHeight() - 0;

        if (!popup.isShowing())
        {
            popup.show(popup.getOwnerWindow());
        }

        popup.show(btnCreate, x, y);

    }

    public void createEmployee(String name)
    {
        listEmployees.getItems().add(name);
    }

    @FXML
    public void handleEditEmployee()
    {
        final String oldName = listEmployees.getSelectionModel().getSelectedItem();

        AddPopupCallback callback = new AddPopupCallback()
        {
            @Override
            public void valueChanged(String newName)
            {
                editEmployee(oldName, newName);
            }
        };
        AddPopup popup = new AddPopup(this, oldName, callback);
        popup.show(SaxnetApplication.stage);

        double x = popup.getOwnerWindow().getX() + btnCreate.getLayoutX();
        double y = popup.getOwnerWindow().getY() + btnCreate.getLayoutY() + btnCreate.getHeight() - 0;

        if (!popup.isShowing())
        {
            popup.show(popup.getOwnerWindow());
        }

        popup.show(btnCreate, x, y);


    }

    public void editEmployee(String oldName, String newName)
    {
        listEmployees.getItems().set(listEmployees.getItems().indexOf(oldName), newName);
    }

    @FXML
    public void handleRemoveEmployee()
    {
        String item = listEmployees.getSelectionModel().getSelectedItem();
        listEmployees.getItems().remove(item);

    }

    //todo: to implement
    public void showRelatedEmployees(String employee)
    {
        System.out.println("show relative employees to " + employee);

        listRelativeEmployees.getItems().clear();

        if (employee != null)
        {
            listRelativeEmployees.getItems().addAll("Horst", "Hans", "Juliane");
        }
    }

}
