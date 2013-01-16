package de.saxsys.saxnet;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.neo4j.graphdb.*;

import java.net.URL;
import java.util.ResourceBundle;

public class SaxnetController implements Initializable
{
    @FXML
    private ListView<String> listEmployees;

    @FXML
    private ListView<String> listNotRelativeEmployees;

    @FXML
    private ListView<String> listRelativeEmployees;

    private String selectedNotRelatedEmployee;
    @FXML
    private TextField txtName;

    @FXML
    private Button btnCreate;

    private static enum RelTypes implements RelationshipType
    {
        WORKS_AT, KNOWS
    }

    @Override
    public void initialize(URL url, ResourceBundle bundle)
    {
//        insertEmployees();

        listEmployees.getSelectionModel().selectedItemProperty()
                .addListener(new InvalidationListener()
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
        // listEmployees.getItems().addAll("Sebastian", "Alex");
        Transaction tx = NeoDB.getInstance().beginTx();
        Node node1 = NeoDB.getInstance().getNodeById(0);
        Iterable<Relationship> relationships = node1.getRelationships();
        for (Relationship rel : relationships)
        {
            listEmployees.getItems().add(
                    (String) rel.getStartNode().getProperty("name"));

        }
        tx.success();
        tx.finish();
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
        double y = popup.getOwnerWindow().getY() + btnCreate.getLayoutY()
                + btnCreate.getHeight() - 0;

        if (!popup.isShowing())
        {
            popup.show(popup.getOwnerWindow());
        }

        popup.show(btnCreate, x, y);

    }

    public void createEmployee(String name)
    {
        Transaction tx = NeoDB.getInstance().beginTx();
        Node node = NeoDB.getInstance().createNode();
        Node companyNode = NeoDB.getInstance().getNodeById(0);
        node.setProperty("name", name);
        node.createRelationshipTo(companyNode, RelTypes.WORKS_AT);
        tx.success();
        tx.finish();
        listEmployees.getItems().add(name);
    }

    @FXML
    public void handleEditEmployee()
    {
        final String oldName = listEmployees.getSelectionModel()
                .getSelectedItem();

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
        double y = popup.getOwnerWindow().getY() + btnCreate.getLayoutY()
                + btnCreate.getHeight() - 0;

        if (!popup.isShowing())
        {
            popup.show(popup.getOwnerWindow());
        }

        popup.show(btnCreate, x, y);

    }

    public void editEmployee(String oldName, String newName)
    {
        listEmployees.getItems().set(listEmployees.getItems().indexOf(oldName),
                newName);
    }

    @FXML
    public void handleRemoveEmployee()
    {
        listEmployees.getItems().remove(this.getSelectedEmployee());
    }

    public String getSelectedEmployee()
    {
        return listEmployees.getSelectionModel().getSelectedItem();
    }

    @FXML
    //Stellt Verbindung her aus ausgewählten Employee (linke Liste) und rechten unteren Liste
    public void handleCreateRelation()
    {
        String employee = this.getSelectedEmployee();
        String notYetRelatedPerson = this.getSelectedNotRelatedEmployee();

        System.out.println("notYetRelatedPerson = " + notYetRelatedPerson);

        //todo: save relation in DB

    }

    @FXML
    public void handleRemoveRelation()
    {
        String employee = this.getSelectedEmployee();
        String relatedPerson = this.getSelectedRelatedEmployee();

        System.out.println("relatedPerson = " + relatedPerson);

        //todo: remove relation in DB

    }

    public void showRelatedEmployees(String employee)
    {
        System.out.println("show relative employees to " + employee);
        listRelativeEmployees.getItems().clear();
        if (null != employee)
        {
            Transaction tx = NeoDB.getInstance().beginTx();
            Node companyNode = NeoDB.getInstance().getNodeById(0);
            Iterable<Relationship> relationships = companyNode
                    .getRelationships();
            for (Relationship rel : relationships)
            {
                if (employee.equals((String) rel.getStartNode().getProperty(
                        "name")))
                {
                    Iterable<Relationship> relKnows = rel.getStartNode()
                            .getRelationships(RelTypes.KNOWS,
                                    Direction.OUTGOING);
                    for (Relationship relK : relKnows)
                    {
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

    public String getSelectedNotRelatedEmployee()
    {
        return this.listNotRelativeEmployees.getSelectionModel().getSelectedItem();
    }

    public String getSelectedRelatedEmployee()
    {
        return this.listRelativeEmployees.getSelectionModel().getSelectedItem();
    }

    public void setSelectedNotRelatedEmployee(String selectedNotRelatedEmployee)
    {
        this.selectedNotRelatedEmployee = selectedNotRelatedEmployee;
    }
}
