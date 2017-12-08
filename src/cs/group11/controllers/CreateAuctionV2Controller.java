package cs.group11.controllers;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.sun.media.sound.InvalidDataException;

import cs.group11.Main;
import cs.group11.helpers.Validator;
import cs.group11.models.Artwork;
import cs.group11.models.Auction;
import cs.group11.models.User;
import cs.group11.models.artworks.Painting;
import cs.group11.models.artworks.Sculpture;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Pair;

/**
 * @author Filippos Pantekis
 * The controller class for createAuction GUI
 */
public class CreateAuctionV2Controller {

	/**
	 * A Pattern to match non numecial inputs	
	 */
	private static final Pattern NON_NUMERICAL_INPUT_PATTERN = Pattern.compile("[^\\d.]+");

	/**
	 * The default auction image.
	 */
	private static final Image defaultAuctionimage = new Image(
			ClassLoader.getSystemResourceAsStream("res/createAuctionDefaultIcon.png"));

	/**
	 * Supported image formats for file selection
	 */
	private static final ExtensionFilter IMAGE_FILE_EXTENTIONS = new ExtensionFilter("Image Files", "*.png", "*.gif",
			"*.jpeg", "*.jpg");

	/**
	 * The width of images shown in the 'extra image' list for sculptures
	 */
	private static final int DEFAULT_EXTRA_IMAGE_LIST_CELL_WIDTH = 100;
	/**
	 * The height of images shown in the extra image list for sculptures
	 */
	private static final int DEFAULT_EXTRA_IMAGE_LIST_CELL_HEIGHT = 100;

	@FXML
	private ImageView logo;
	@FXML
	private ImageView avatar1;
	@FXML
	private Label username1;

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
	private ListView<Pair<String, Image>> extraImages;
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

	@FXML
	private VBox rootBox;

	private String mainImagePath;
	private User currentUser;

	/**
	 * The controller initialize method$
	 */
	@FXML
	protected void initialize() {
		this.currentUser = Main.getCurrentUser();
		Image avatarImage = new Image(currentUser.getAvatarPath());
		this.logo.setImage(avatarImage);
		this.avatar1.setImage(avatarImage);
		this.username1.setText(currentUser.getUsername());

		makeFieldsNumeric(depth, length, startPrice, maxBids, width);

		extraImages.setCellFactory(param -> new ListCell<Pair<String, Image>>() {
			@Override
			public void updateItem(Pair<String, Image> i, boolean empty) {
				super.updateItem(i, empty);
				if (!empty) {
					ImageView img = new ImageView(i.getValue());
					img.setFitHeight(DEFAULT_EXTRA_IMAGE_LIST_CELL_HEIGHT);
					img.setFitWidth(DEFAULT_EXTRA_IMAGE_LIST_CELL_WIDTH);
					setGraphic(img);
				}
			}
		});

		extraImages.getSelectionModel().selectedItemProperty().addListener((value, oldState, newState) -> {
			image.setImage(value.getValue().getValue());
			mainImagePath = value.getValue().getKey();
			removeImg.setDisable(Validator.isNull(value.getValue()) || extraImages.getItems().size() <= 1);

		});

		addExtraImg.setOnAction((e) -> handleAddExtraImage());

		removeImg.setDisable(true);
		removeImg.setOnAction((e) -> {
			int selectedIndex = extraImages.getSelectionModel().getSelectedIndex();
			if (selectedIndex != -1) {

				extraImages.getItems().remove(selectedIndex);
			}
		});

		// Setup the toggle group for the radiobuttons.
		ToggleGroup radioGroup = new ToggleGroup();
		sculptureRadio.setToggleGroup(radioGroup);
		paintingRadio.setToggleGroup(radioGroup);
		sculptureRadio.selectedProperty().addListener(
				(observVal, oldState, newState) -> sculptureInputs.setVisible(sculptureRadio.isSelected()));

		// Set the image
		image.setImage(defaultAuctionimage);
		image.setOnMouseClicked((e) -> handleImageSelection());

		// Listen to clicks on create button and handle the click invoking
		// createAuction();
		create.setOnAction((e) -> createAuction());

		// Set the default date of the date field to the current date.
		creationDate.setValue(LocalDate.now());
	}

	/**
	 * input an image from the user, and insert it into the
	 * 'extra images' list for the sculpture prespective
	 */
	private void handleAddExtraImage() {
		Pair<String, Image> userSelection = userSelectImage();
		if (!Validator.isNull(userSelection)) {
			extraImages.getItems().add(userSelection);
			extraImages.getSelectionModel().select(userSelection);
		}
	}

	/**
	 * handle the selection of an image from the user.
	 */
	private void handleImageSelection() {
		Pair<String, Image> selected = userSelectImage();
		if (!Validator.isNull(selected)) {
			ObservableList<Pair<String, Image>> extraImgs = extraImages.getItems();

			// remove the previous image from the list.
			int oldImageIndex = -1;
			for (int i = 0; i < extraImgs.size(); i++) {
				if (extraImgs.get(i).getValue().equals(image.getImage())) {
					oldImageIndex = i;
					break;// save ourselves some iterations.
				}
			}
			if (oldImageIndex != -1) {// If we found it
				extraImgs.remove(oldImageIndex);
			}
			extraImgs.add(selected);
			image.setImage(selected.getValue());
			mainImagePath = selected.getKey();
		}
	}

	/**
	 * Makes a field only accept decimal input.
	 * @param textFields
	 */
	private void makeFieldsNumeric(TextField... textFields) {
		for (TextField field : textFields) {
			field.textProperty().addListener((observable, oldValue, newValue) -> {
				Matcher m = NON_NUMERICAL_INPUT_PATTERN.matcher(newValue);
				if (m.find()) {// If there is non numeric input in the field
					field.setText(m.replaceAll(""));// remove it
				}
			});
		}
	}

	/**
	 * Prompts the user to select an image from the local filesystem.
	 * @return a Pair of image and String where string represents the path
	 * of the image in the filesystem and the image object is the loaded image.
	 */
	private Pair<String, Image> userSelectImage() {
		FileChooser chooser = new FileChooser();
		chooser.getExtensionFilters().add(IMAGE_FILE_EXTENTIONS);
		chooser.setTitle("Select Image");
		File in = chooser.showOpenDialog(null);
		if (!Validator.isFileValid(in))
			return null;
		return new Pair<>(in.toURI().toString(), new Image(in.toURI().toString()));
	}

	/**
	 * Create an auction based on data inputed by the user
	 * @return an Auction object created using the data provided by the user.
	 * @throw {@link InvalidDataException} if the input data is not valid.
	 */
	private void createAuction() {
		if (isAnyEmpty(title, artist, startPrice, maxBids, width, length, description)
				|| (sculptureRadio.isSelected() && isAnyEmpty(depth, material))) {
			Alert alert = new Alert(AlertType.ERROR, "All fields must be completed before\ncreating the auction",
					ButtonType.OK);
			alert.show();
			return;
		}
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
			List<String> extraImagePaths = getExtraImagePaths();

			forAuctioning = new Sculpture(auctionTitle, auctionDescription, mainImagePath, auctionAuthor,
					artworkCreationDate.getYear(), auctionWidth, auctionLength, auctionDepth, auctionMaterial,
					extraImagePaths);
		} else {
			forAuctioning = new Painting(auctionTitle, auctionDescription, mainImagePath, auctionAuthor,
					artworkCreationDate.getYear(), auctionWidth, auctionLength);
		}
		 new Auction(currentUser, auctionMaxBids, auctionStartPrice, forAuctioning);//Automatically added to megadb
	}

	/**
	 * Checks if any of the parameter TextInputControlls has no text (left blank)
	 * @param tf The text input controlls to check (TextField, TextArea etc are subclasses)
	 * @return true if at least one of them is empty, false otherwise.
	 */
	private boolean isAnyEmpty(TextInputControl... tf) {
		for (TextInputControl t : tf) {
			if (t.getText().trim().isEmpty())
				return true;
		}
		return false;

	}

	/**
	 * Get the extra image paths.
	 * @return a List of String with the local path of each extra image in it.
	 */
	private List<String> getExtraImagePaths() {
		// Take all images, map them to just the path and return the new list.
		return extraImages.getItems().stream().map(Pair::getKey).collect(Collectors.toList());
	}

	/**
	 * Convert a String to a double
	 * @param str the input string
	 * @return a double produced by parsing this string.
	 */
	private double parseDecimal(String str) {
		return Double.parseDouble(str);
	}

	public void viewAuctionClick() throws IOException {
		AuctionListController controller = new AuctionListController();

		FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/auctionList.fxml"));
		loader.setController(controller);
		VBox box = loader.load();

		box.prefHeightProperty().bind(rootBox.heightProperty());

		rootBox.getChildren().setAll(box);
	}

	public void avatarClick() throws IOException {
		ProfileController profileCon = new ProfileController();
		profileCon.setLoginedUser(this.currentUser);
		profileCon.setViewingUser(this.currentUser);

		FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/profile.fxml"));
		loader.setController(profileCon);
		profileCon.addTestBids();

		VBox box = loader.load();

		box.prefHeightProperty().bind(rootBox.heightProperty());

		rootBox.getChildren().setAll(box);
	}

	public void logoutClick() throws IOException {
		VBox box = FXMLLoader.load(getClass().getResource("../views/signIn.fxml"));
		box.prefHeightProperty().bind(rootBox.heightProperty());
		rootBox.getChildren().setAll(box);
	}

}