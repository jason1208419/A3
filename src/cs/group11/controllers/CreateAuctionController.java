package cs.group11.controllers;

import java.io.File;

import cs.group11.helpers.Validator;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

/**
 * @author Filippos Pantekis
 */
public class CreateAuctionController {

	private static final Image NO_IMAGE_SELECTED_ICON;
	static {
		NO_IMAGE_SELECTED_ICON = new Image(ClassLoader.getSystemResourceAsStream("res/createAuctionDefaultIcon.png"));
	}

	@FXML
	private BorderPane mainPane;

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
		// Setup the toggle group for the radiobuttons.
		ToggleGroup radioGroup = new ToggleGroup();
		sculptureRadio.setToggleGroup(radioGroup);
		paintingRadio.setToggleGroup(radioGroup);
		sculptureRadio.selectedProperty().addListener((observVal, oldState, newState) -> {
			materialPane.setVisible(newState);
			depthPane.setVisible(newState);
		});

		// Set the image
		image.setImage(NO_IMAGE_SELECTED_ICON);
		image.setOnMouseClicked((e) -> {
			Image selected = userSelectImage();
			if (Validator.isNull(selected)) {
				// TODO show alert
			} else
				image.setImage(selected);
		});

		
	}

	
	private Image userSelectImage() {
		FileChooser chooser = new FileChooser();
		chooser.setSelectedExtensionFilter(new ExtensionFilter("Image Files", ".png", ".gif", ".jpeg", ".jpg"));
		chooser.setTitle("Select Image");
		File in = chooser.showOpenDialog(null);
		if (!Validator.isFileValid(in))
			return null;
		return new Image(in.toURI().toString());
	}

	// THIS WILL BE DELETED! JUST FOR TESTING PURPOSES!
	public static class Test extends Application {

		@Override
		public void start(Stage s) throws Exception {
			FXMLLoader l = new FXMLLoader();
			Pane p = l.load(ClassLoader.getSystemResourceAsStream("cs/group11/views/createAuction.fxml"));
			s.setScene(new Scene(p));
			s.show();
		}

		public static void main(String[] args) {
			launch(args);
		}

	}

}
