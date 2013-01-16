package de.saxsys.saxnet;

import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Popup;
import org.tbee.javafx.scene.layout.MigPane;

/**
 * Created with IntelliJ IDEA.
 * User: TB
 * Date: 16.01.13
 * Time: 19:06
 * To change this template use File | Settings | File Templates.
 */
public class AddPopup extends Popup
{
    public AddPopup(final SaxnetController saxnetController, String value, final AddPopupCallback callback)
    {
        Label nameLabel = new Label("Name");
        final TextField nameTextField = new TextField(value);
        nameTextField.setOnKeyReleased(new EventHandler<KeyEvent>()
        {
            @Override
            public void handle(KeyEvent keyEvent)
            {
                if (keyEvent.getCode() == KeyCode.ENTER)
                {
                    callback.valueChanged(nameTextField.getText());
                    AddPopup.this.hide();

                }
                else if (keyEvent.getCode() == KeyCode.ESCAPE)
                {
                    AddPopup.this.hide();
                }
            }
        });

        MigPane panel = new MigPane("flowy, ins 8", "[fill,grow]", "[][]");
        panel.setStyle("-fx-border-radius: 4px;-fx-border-size: 1px;-fx-border-color: #BBB;-fx-background-color: #EEE; -fx-opacity: .9");
        panel.add(nameLabel);
        panel.add(nameTextField, "grow");

        this.getContent().add(panel);
    }
}
