package cs.group11.controllers;

import java.io.File;
import java.time.LocalDate;

import cs.group11.helpers.Validator;
import cs.group11.models.Auction;
import cs.group11.models.User;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

/**
 * @author Filippos Pantekis
 */
public class CreateAuctionController {

	private static final Image defaultAuctionimage = new Image(
			ClassLoader.getSystemResourceAsStream("res/createAuctionDefaultIcon.png"));

	private static final ExtensionFilter IMAGE_FILE_EXTENTIONS = new ExtensionFilter("Image Files", ".png", ".gif",
			".jpeg", ".jpg");

	private static final int DEFAULT_WINDOW_HEIGHT = 430;
	private static final int DEFAULT_WINDOW_WIDTH = 270;

	@FXML
	private BorderPane mainPane;

	@FXML
	private VBox fieldHolder;

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
	private DatePicker creationDate;

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

	private User user;

	@FXML
	protected void initialize() {

		// set pane min width and height
		mainPane.setMinWidth(DEFAULT_WINDOW_WIDTH);
		mainPane.setMinHeight(DEFAULT_WINDOW_HEIGHT);

		// Setup the toggle group for the radiobuttons.
		ToggleGroup radioGroup = new ToggleGroup();
		sculptureRadio.setToggleGroup(radioGroup);
		paintingRadio.setToggleGroup(radioGroup);
		sculptureRadio.selectedProperty().addListener((observVal, oldState, newState) -> {
			materialPane.setVisible(newState);
			depthPane.setVisible(newState);
		});

		// Set the image
		image.fitWidthProperty().bind(mainPane.widthProperty());
		image.setImage(defaultAuctionimage);
		image.setOnMouseClicked((e) -> {
			Image selected = userSelectImage();
			if (Validator.isNull(selected)) {
				// TODO show alert
			} else
				image.setImage(selected);
		});

		createButton.setOnAction((e) -> {
			String auctionTitle = this.title.getText();
			String auctionAuthor = this.auction.getText();
			String auctionStartPrice = this.startPrice.getText();
			String auctionWidth = this.width.getText();
			String auctionLength = this.length.getText();
			LocalDate artworkCreationDate = this.creationDate.getValue();
			String auctionDepth = this.depth.getText();
			String auctionMaterial = this.material.getText();
			//TODO introduce missing elements!
			//Auction a = new Auction(user, maxBids, reservePrice, artwork)
			

		});

	}

	private Auction createAuction() {

		return null;
	}

	private Image userSelectImage() {
		FileChooser chooser = new FileChooser();
		chooser.setSelectedExtensionFilter(IMAGE_FILE_EXTENTIONS);
		chooser.setTitle("Select Image");
		File in = chooser.showOpenDialog(null);
		if (!Validator.isFileValid(in))
			return null;
		return new Image(in.toURI().toString());
	}

	public void setUser(User u) {
		this.user = u;
	}

	// THIS WILL BE DELETED! JUST FOR TESTING PURPOSES!
	public static class Test extends Application {

		@Override
		public void start(Stage s) throws Exception {
			FXMLLoader l = new FXMLLoader();
			Pane p = l.load(ClassLoader.getSystemResourceAsStream("cs/group11/views/createAuction.fxml"));
			s.setScene(new Scene(p));
			s.setMinWidth(p.getMinWidth());
			s.setMinHeight(p.getMinHeight());
			s.show();
		}

		public static void main(String[] args) {
			launch(args);
		}

	}

}
