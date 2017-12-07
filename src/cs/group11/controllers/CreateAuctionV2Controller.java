package cs.group11.controllers;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.Pair;

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

	@FXML
	protected void initialize() {
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

		// set pane min width and height
		//mainPane.setMinWidth(DEFAULT_WINDOW_WIDTH);
		//mainPane.setMinHeight(DEFAULT_WINDOW_HEIGHT);

		// Setup the toggle group for the radiobuttons.
		ToggleGroup radioGroup = new ToggleGroup();
		sculptureRadio.setToggleGroup(radioGroup);
		paintingRadio.setToggleGroup(radioGroup);
		sculptureRadio.selectedProperty().addListener((observVal, oldState, newState) -> sculptureInputs.setVisible(sculptureRadio.isSelected()));

		// Set the image
		image.setImage(defaultAuctionimage);
		image.setOnMouseClicked((e) -> handleImageSelection());

		create.setOnAction((e) -> createAuction());

		creationDate.setValue(LocalDate.now());
	}

	public void setUser(User user) {
		this.currentUser = user;
	}

	private void handleAddExtraImage() {
		Pair<String, Image> userSelection = userSelectImage();
		if (!Validator.isNull(userSelection)) {
			extraImages.getItems().add(userSelection);
			extraImages.getSelectionModel().select(userSelection);
		}
	}

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

	private void makeFieldsNumeric(TextField... textFields) {
		for (TextField field : textFields) {
			field.textProperty().addListener((observable, oldValue, newValue) -> {
				Matcher m = NON_NUMERICAL_INPUT_PATTERN.matcher(newValue);
				if (m.find()) {
					field.setText(m.replaceAll(""));
				}
			});
		}
	}

	private Pair<String, Image> userSelectImage() {
		FileChooser chooser = new FileChooser();
		chooser.setSelectedExtensionFilter(IMAGE_FILE_EXTENTIONS);
		chooser.setTitle("Select Image");
		File in = chooser.showOpenDialog(null);
		if (!Validator.isFileValid(in))
			return null;
		return new Pair<>(in.toURI().toString(), new Image(in.toURI().toString()));
	}

	private Auction createAuction() {
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
		return new Auction(currentUser, auctionMaxBids, auctionStartPrice, forAuctioning);
	}

	private List<String> getExtraImagePaths() {
		// Take all images, map them to just the path and return the new list.
		return extraImages.getItems().stream().map(Pair::getKey).collect(Collectors.toList());
	}

	private double parseDecimal(String str) {
		return Double.parseDouble(str);
	}

	public void viewAuctionClick() throws IOException {
		AuctionListController controller = new AuctionListController();
		controller.setUser(this.currentUser);

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