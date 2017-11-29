package cs.group11.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;

public class CreateAuctionController {

	@FXML
	private ImageView image;

	@FXML
	private RadioButton sculptureRadio;

	@FXML
	private RadioButton paintingRadio;

	@FXML
	private TextField title;

	@FXML
	private TextField auction;

	@FXML
	private TextField startPrice;

	@FXML
	private TextField width;

	@FXML
	private TextField length;

	@FXML
	private TextField creationDate;

	@FXML
	private FlowPane depthPane;

	@FXML
	private TextField depth;

	@FXML
	private FlowPane materialPane;

	@FXML
	private TextField material;

	@FXML
	private Button createButton;

	@FXML
	protected void initialize() {
		ToggleGroup radioGroup = new ToggleGroup();
		sculptureRadio.setToggleGroup(radioGroup);
		paintingRadio.setToggleGroup(radioGroup);

		radioGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {

			@Override
			public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
				if (observable.getValue() != null && observable.getValue().equals(sculptureRadio)) {// TODO test
					boolean show = newValue.isSelected();
					materialPane.setVisible(show);
					depthPane.setVisible(show);
				}
			}
		});

	}

}
