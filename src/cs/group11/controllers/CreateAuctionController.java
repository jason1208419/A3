package cs.group11.controllers;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class CreateAuctionController {

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
		ToggleGroup radioGroup = new ToggleGroup();
		sculptureRadio.setToggleGroup(radioGroup);
		paintingRadio.setToggleGroup(radioGroup);
		sculptureRadio.selectedProperty().addListener((observVal, oldState, newState) -> {
			materialPane.setVisible(newState);
			depthPane.setVisible(newState);
		});
		
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
