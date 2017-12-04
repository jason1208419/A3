package cs.group11.controllers;

import java.io.File;
import java.time.LocalDate;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cs.group11.helpers.Validator;
import cs.group11.models.Artwork;
import cs.group11.models.Auction;
import cs.group11.models.User;
import cs.group11.models.artworks.Painting;
import cs.group11.models.artworks.Sculpture;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class CreateAuctionV2Controller {

	private static final Pattern NON_NUMERICAL_INPUT_PATTERN = Pattern.compile("[^\\d.]+");

	private static final Image defaultAuctionimage = new Image(
			ClassLoader.getSystemResourceAsStream("res/createAuctionDefaultIcon.png"));

	private static final ExtensionFilter IMAGE_FILE_EXTENTIONS = new ExtensionFilter("Image Files", ".png", ".gif",
			".jpeg", ".jpg");

	private static final int DEFAULT_WINDOW_HEIGHT = 430;
	private static final int DEFAULT_WINDOW_WIDTH = 270;

	private static final int DEFAULT_EXTRA_IMAGE_LIST_CELL_WIDTH = 100;
	private static final int DEFAULT_EXTRA_IMAGE_LIST_CELL_HEIGHT = 100;

	@FXML
	private BorderPane mainPane;
	@FXML
	private VBox sculptureInputs;
	@FXML
	private TextField title;
	@FXML
	private TextField artist;
	@FXML
	private TextField startPrice;
	@FXML
	private TextField maxBids;
	@FXML
	private TextField width;
	@FXML
	private TextField length;
	@FXML
	private DatePicker creationDate;
	@FXML
	private TextField depth;
	@FXML
	private TextField material;
	@FXML
	private Button addExtraImg;
	@FXML
	private Button removeImg;
	@FXML
	private ListView<Image> extraImages;
	@FXML
	private TextArea description;
	@FXML
	private RadioButton sculptureRadio;
	@FXML
	private RadioButton paintingRadio;
	@FXML
	private ImageView image;
	@FXML
	private Button create;

	private User currentUser;

	@FXML
	protected void initialize() {

		makeFieldsNumeric(depth, length, startPrice, maxBids, width);

		extraImages.setCellFactory(param -> new ListCell<Image>() {
			@Override
			public void updateItem(Image i, boolean empty) {
				super.updateItem(i, empty);
				if (!empty) {
					ImageView img = new ImageView(i);
					img.setFitHeight(DEFAULT_EXTRA_IMAGE_LIST_CELL_HEIGHT);
					img.setFitWidth(DEFAULT_EXTRA_IMAGE_LIST_CELL_WIDTH);
					setGraphic(img);
				}
			}
		});

		extraImages.getSelectionModel().selectedItemProperty().addListener((value, oldState, newState) -> {
			image.setImage(value.getValue());
			removeImg.setDisable(Validator.isNull(value.getValue()) || extraImages.getItems().size() <= 1);

		});

		addExtraImg.setOnAction((e) -> {
			handleaddExtraImage();
		});

		removeImg.setDisable(true);
		removeImg.setOnAction((e) -> {
			int selectedIndex = extraImages.getSelectionModel().getSelectedIndex();
			if (selectedIndex != -1) {
				extraImages.getItems().remove(selectedIndex);
			}
		});

		// set pane min width and height
		mainPane.setMinWidth(DEFAULT_WINDOW_WIDTH);
		mainPane.setMinHeight(DEFAULT_WINDOW_HEIGHT);

		// Setup the toggle group for the radiobuttons.
		ToggleGroup radioGroup = new ToggleGroup();
		sculptureRadio.setToggleGroup(radioGroup);
		paintingRadio.setToggleGroup(radioGroup);
		sculptureRadio.selectedProperty().addListener((observVal, oldState, newState) -> {
			sculptureInputs.setVisible(sculptureRadio.isSelected());
		});

		// Set the image
		image.setImage(defaultAuctionimage);
		image.setOnMouseClicked((e) -> {
			handleImageSelection();
		});

		create.setOnAction((e) -> {
			createAuction();
		});

		creationDate.setValue(LocalDate.now());
	}

	private void handleaddExtraImage() {
		Image i = userSelectImage();
		if (Validator.isNull(i)) {
			// TODO alert
		} else {
			extraImages.getItems().add(i);
			extraImages.getSelectionModel().select(i);
		}
	}

	private void handleImageSelection() {
		Image selected = userSelectImage();
		if (Validator.isNull(selected)) {
			// TODO show alert
		} else {
			ObservableList<Image> extraImgs = extraImages.getItems();
			if (image.getImage() != null) {
				extraImgs.remove(image.getImage());// remove the previous
													// image from the list.
			}
			extraImgs.add(selected);
			image.setImage(selected);
		}
	}

	private void makeFieldsNumeric(TextField... textFields) {
		for (TextField field : textFields) {
			field.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					Matcher m = NON_NUMERICAL_INPUT_PATTERN.matcher(newValue);
					if (m.find()) {
						field.setText(m.replaceAll(""));
					}
				}
			});
		}
	}

	private Auction createAuction() {
		Image auctionImage = this.image.getImage();
		String auctionTitle = this.title.getText();// title
		String auctionAuthor = this.artist.getText();// artist
		double auctionStartPrice = parseDecimal(this.startPrice.getText());// startPrice
		int auctionMaxBids = (int) parseDecimal(this.maxBids.getText());
		double auctionWidth = parseDecimal(this.width.getText());// width
		double auctionLength = parseDecimal(this.length.getText());// length
		LocalDate artworkCreationDate = this.creationDate.getValue();// creation
		String auctionDescription = description.getText(); // date

		Artwork forAuctioning;

		if (sculptureRadio.isSelected()) {
			double auctionDepth = parseDecimal(this.depth.getText());// depth
			String auctionMaterial = this.material.getText(); // material
			List<Image> auctionExtraImages = this.extraImages.getItems();
			forAuctioning = new Sculpture(auctionTitle, auctionDescription, auctionImage, auctionAuthor,
					artworkCreationDate.getYear(), auctionWidth, auctionLength, auctionDepth, auctionMaterial,
					auctionExtraImages);
		} else {
			forAuctioning = new Painting(auctionTitle, auctionDescription, auctionImage, auctionAuthor,
					artworkCreationDate.getYear(), auctionWidth, auctionLength);
		}
		return new Auction(currentUser, auctionMaxBids, auctionStartPrice, forAuctioning);
	}

	private double parseDecimal(String str) {
		return Double.parseDouble(str);
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

	// THIS WILL BE DELETED! JUST FOR TESTING PURPOSES!
	public static class Test extends Application {

		@Override
		public void start(Stage s) throws Exception {
			FXMLLoader l = new FXMLLoader();
			Pane p = l.load(ClassLoader.getSystemResourceAsStream("cs/group11/views/createAuctionV2.fxml"));
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
