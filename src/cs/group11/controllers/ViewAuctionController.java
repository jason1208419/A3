package cs.group11.controllers;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import cs.group11.MegaDB;
import cs.group11.helpers.InvalidDataException;
import cs.group11.interfaces.OnAction;
import cs.group11.interfaces.OnHeaderAction;
import cs.group11.interfaces.OnSubmitClick;
import cs.group11.interfaces.OnUserClick;
import cs.group11.models.Artwork;
import cs.group11.models.Auction;
import cs.group11.models.Bid;
import cs.group11.models.User;
import cs.group11.models.artworks.Painting;
import cs.group11.models.artworks.Sculpture;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class ViewAuctionController {

	@FXML
	private ImageView avatar1;
	@FXML
	private Label username1;

	@FXML
	private ImageView artworkImageView;
	@FXML
	private Label artType;
	@FXML
	private Label title;
	@FXML
	private Label author;
	@FXML
	private Label currentPrice;
	@FXML
	private Label startingPrice;
	@FXML
	private Label width;
	@FXML
	private Label height;
	@FXML
	private Label material;
	@FXML
	private Label depth;
	@FXML
	private Label artworkCreation;
	@FXML
	private Label auctionCreation;
    @FXML
    private Label description;


	@FXML
	private Label currentPrice2;
	@FXML
	private Label maxBids;
	@FXML
	private Label placedBids;

	@FXML
	private Label remainingBids;

	@FXML
	private Label sellerUsername;
	@FXML
	private ImageView sellerAvatarImageView;

	@FXML
	private VBox rootBox;

	@FXML
	private TextField bidAmountInput;
	@FXML
	private Button submitButton;

	private OnHeaderAction onHeaderAction;
	private OnUserClick onUserClick;
    @FXML
    private CheckBox favUserBtn;

    @FXML
    private CheckBox favArtBtn;

	private OnHeaderAction headerAction;
	private User user;
	private Auction auction;

	public void setOnUserClick(OnUserClick onUserClick) {
		this.onUserClick = onUserClick;
	}

	@FXML
    protected void initialize() {
        this.user = MegaDB.getLoggedInUser();
        Image img = new Image(user.getAvatarPath());
        this.avatar1.setImage(img);
        this.username1.setText(user.getUsername());

		sellerAvatarImageView.setOnMouseClicked((MouseEvent e) -> onUserClick.clicked(this.auction.getCreator()));
	}

	public void setOnHeaderAction(OnHeaderAction onHeaderAction) {
		this.onHeaderAction = onHeaderAction;
	}

	public void setUser(User user) {
		this.user = user;
	}

	private String getHeight(Artwork artwork) {
		// Gets the Height
		double artHeight = 0;

		if (artwork instanceof Painting) {
			Painting painting = (Painting) artwork;

			artHeight = painting.getHeight();

		} else if (artwork instanceof Sculpture) {
			Sculpture sculpture = (Sculpture) artwork;

			artHeight = sculpture.getHeight();
		}
		return Double.toString(artHeight);
	}

	public void placeBidClick() {

		if (bidAmountInput.getText().isEmpty()) {
			return;
		}

    	double bidAmount = Double.parseDouble(bidAmountInput.getText());
		Bid placedBid = null;

    	try {
			placedBid = new Bid(bidAmount, user, auction);
			setAuction(auction);
		} catch (InvalidDataException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Bid error");
			alert.setHeaderText("There was a problem with bid.");
			alert.setContentText(e.getMessage());

			alert.showAndWait();
		}
		if (this.auction.isCompleted()) {
			this.submitButton.setDisable(true);
			this.bidAmountInput.setDisable(true);

			Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
			alert.setTitle("Congratulations");
			alert.setHeaderText("You are the auction winner!");

			alert.showAndWait();


		}
	}

	private String getType(Artwork artwork) {
		if (artwork instanceof Painting) {
			return "Painting";
		} else if (artwork instanceof Sculpture) {
			return "Sculpture";
		}
		return "Artwork";
	}

	/**
	 * Displays all info about an auction in the GUI.
	 *
	 * @param auction
	 */
	public void setAuction(Auction auction) {
		this.auction = auction;

		Artwork artwork = auction.getArtwork();
		this.artType.setText("Type: " + getType(artwork));

		// Gets the Width and depth and material
		if (artwork instanceof Painting) {
			Painting painting = (Painting) artwork;
			this.width.setText("Width: " + Double.toString(painting.getWidth()) + " cm");
			this.depth.setVisible(false);
			this.material.setVisible(false);
		} else if (artwork instanceof Sculpture) {
			Sculpture sculpture = (Sculpture) artwork;
			this.width.setText("Width: " + Double.toString(sculpture.getWidth()) + " cm");
			this.depth.setText("Depth: " + Double.toString(sculpture.getDepth()) + " cm");
			this.material.setText("Material: " + ((Sculpture) artwork).getMaterial());
		}

		this.height.setText("Height: " + getHeight(artwork) + " cm");

		Image image = new Image(auction.getArtwork().getImagePath());
		this.artworkImageView.setImage(image);
		this.title.setText("Title: " + artwork.getName());
		this.author.setText("Artist: " + artwork.getArtist());

        //Get art description
        this.description.setText("Description: " + artwork.getDescription());


		// Gets artworks creation year
		this.artworkCreation.setText("Art creation: " + Integer.toString(artwork.getCreationYear()));

		this.sellerUsername.setText("Seller: " + auction.getCreator().getUsername());
		Image avatarImage = new Image(auction.getCreator().getAvatarPath());
		this.sellerAvatarImageView.setImage(avatarImage);

		// Get most recent bid price
		double lastBid;
		if (auction.getLastBid() == null) {
			lastBid = auction.getReservePrice();
		} else {
			lastBid = auction.getLastBid().getPrice();
		}

		this.currentPrice.setText("Current price: £" + String.valueOf(lastBid));
		this.currentPrice2.setText("Current price: £" + String.valueOf(lastBid));

		// Gets the Maximum amount of bids
		int theMaxBids = auction.getMaxBids();
		String maxAsString = Integer.toString(theMaxBids);
		this.maxBids.setText("Max number of bids: " + maxAsString);

		// Get remaining number of allowed bids
		// Get current number of bids
		int currentBids = auction.getBids().size();
		int remainingBids = theMaxBids - currentBids;

		this.remainingBids.setText("Remaining bids: " + Integer.toString(remainingBids));
		this.placedBids.setText("Number of bids placed: " + Integer.toString(currentBids));

		// Get Starting Price (aka reserve price)
		this.startingPrice.setText("Starting Price: £" + String.valueOf(auction.getReservePrice()));

		// Gets auction creation date
		Date createdDate = auction.getCreationDate();
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		this.auctionCreation.setText("Auction creation: " + dateFormat.format(createdDate));

        /**
         * Adds favourite user to MegaDB
         */
        EventHandler<ActionEvent> onFavUserClick = (ActionEvent event) -> {
        	//TODO validate user not already in list
			MegaDB.getLoggedInUser().addFavouriteUser(this.auction.getCreator());
        };

        favUserBtn.setOnAction(onFavUserClick);

        for (User user : user.getFavouriteUsers()) {
            if (user.getId() == (this.auction.getCreator().getId())) {
                favUserBtn.setSelected(true);
            }
        }
	}

	public void viewAuctionClick() throws IOException {
		favUserBtn.setSelected(false);
		onHeaderAction.browseAuctionsClick();
	}

	@FXML
	public void createAuctionClick() throws IOException {
		favUserBtn.setSelected(false);
		onHeaderAction.createAuctionsClick();
	}

	public void avatarClick() throws IOException {
		favUserBtn.setSelected(false);
		onHeaderAction.browseProfileClick();
	}

	public void logoutClick() throws IOException {
		favUserBtn.setSelected(false);
		onHeaderAction.logoutClick();
	}
}
