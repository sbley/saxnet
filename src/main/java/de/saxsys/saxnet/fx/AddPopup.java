package de.saxsys.saxnet.fx;

import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Popup;

import org.tbee.javafx.scene.layout.MigPane;

import de.saxsys.saxnet.SaxnetController;

public class AddPopup extends Popup {
	public AddPopup(final SaxnetController controller, String value,
			final AddPopupCallback callback) {
		Label nameLabel = new Label("Name");
		final TextField nameTextField = new TextField(value);
		nameTextField.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent keyEvent) {
				if (keyEvent.getCode() == KeyCode.ENTER) {
					callback.valueChanged(nameTextField.getText());
					AddPopup.this.hide();

				} else if (keyEvent.getCode() == KeyCode.ESCAPE) {
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
