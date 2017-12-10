package cs.group11.controllers;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import cs.group11.MegaDB;
import cs.group11.helpers.InvalidDataException;
import cs.group11.interfaces.OnHeaderAction;
import cs.group11.interfaces.OnUserClick;
import cs.group11.models.Artwork;
import cs.group11.models.Auction;
import cs.group11.models.Bid;
import cs.group11.models.User;
import cs.group11.models.artworks.Painting;
import cs.group11.models.artworks.Sculpture;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

/**
 * Handles fxml files that displays all the info about a specific auction.
 *
 * @Author Lewis Smith
 */
public class ViewAuctionController {

	@FXML
	private ImageView avatar1;
	@FXML
	private Label username1;

	@FXML
    private ListView<String> artworkListView;
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
    private ImageView artworkImageView;

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

    private ObservableList<String> artworkImageList;

	/**
	 * Sets actions to be performed when a user in a list is clicked.
	 * @param onUserClick A collection of actions to perform when a user is clicked.
	 */
	public void setOnUserClick(OnUserClick onUserClick) {
		this.onUserClick = onUserClick;
	}

	@FXML
    protected void initialize() {
        this.user = MegaDB.getLoggedInUser();
        Image img = new Image(user.getAvatarPath());
        this.avatar1.setImage(img);
        this.username1.setText(user.getUsername());

        //Loads extra images into GUI
        this.artworkListView.setCellFactory(param -> new ListCell<String>() {
            @Override
            public void updateItem(String i, boolean empty) {
                super.updateItem(i, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    Image image = new Image(i);

                    ImageView node = new ImageView(image);
                    node.setPreserveRatio(true);
                    node.setFitHeight(150);

                    setGraphic(node);
                }
            }
        });

		// Removess any non-numbers in bidding input box
		bidAmountInput.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.matches("\\d*")) {
				bidAmountInput.setText(newValue.replaceAll("[^\\d]", ""));
			}
		});

		sellerAvatarImageView.setOnMouseClicked((MouseEvent e) -> onUserClick.clicked(this.auction.getCreator()));
	}

	/**
	 * Sets actions to be performed when something in the header is clicked.
	 * @param onHeaderAction A collection of actions to perform when something in the header is clicked.
	 */
    public void setOnHeaderAction(OnHeaderAction onHeaderAction) {
        this.onHeaderAction = onHeaderAction;
    }


	/**
	 * @param artwork The artwork you wish to get the height of.
	 * @return The height of the artwork converted into text.
	 */
	private String getHeight(Artwork artwork) {
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

    /**
     * Method that is called when bid button is clicked.
     *
     * Attempts to create bid instance for auction.
     */
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

	/**
	 * @param artwork The artwork you wish to get the type of.
	 * @return The type of artwork queried, returned as a String.
	 */
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
	 * @param auction The auction to be displayed in the GUI
	 */
	public void setAuction(Auction auction) {
		this.auction = auction;

		if(user.getId() == auction.getCreator().getId()) {
			favUserBtn.setVisible(false);
			favArtBtn.setVisible(false);
		} else {
			favUserBtn.setVisible(true);
			favArtBtn.setVisible(true);
		}

		Artwork artwork = auction.getArtwork();
		this.artType.setText("Type: " + getType(artwork));

        Image image = new Image(artwork.getImagePath());
        artworkImageView.setImage(image);

		//Sets information depending on whether artwork is painting or sculpture
		if (artwork instanceof Painting) {
			Painting painting = (Painting) artwork;
			this.width.setText("Width: " + Double.toString(painting.getWidth()) + " cm");
			this.depth.setVisible(false);
			this.material.setVisible(false);

            this.artworkListView.setVisible(false);

        } else if (artwork instanceof Sculpture) {
			Sculpture sculpture = (Sculpture) artwork;
			this.width.setText("Width: " + Double.toString(sculpture.getWidth()) + " cm");
			this.depth.setText("Depth: " + Double.toString(sculpture.getDepth()) + " cm");
			this.material.setText("Material: " + ((Sculpture) artwork).getMaterial());

            this.artworkListView.setVisible(true);

            this.artworkImageList = FXCollections.observableArrayList(sculpture.getPhotos());
            this.artworkListView.setItems(this.artworkImageList);
        }

		this.height.setText("Height: " + getHeight(artwork) + " cm");

		this.title.setText("Title: " + artwork.getName());
		this.author.setText("Artist: " + artwork.getArtist());


        this.description.setText("Description: " + artwork.getDescription());



		this.artworkCreation.setText("Art creation: " + Integer.toString(artwork.getCreationYear()));

		this.sellerUsername.setText("Seller: " + auction.getCreator().getUsername());
		Image avatarImage = new Image(auction.getCreator().getAvatarPath());
		this.sellerAvatarImageView.setImage(avatarImage);

		double lastBid;
		if (auction.getLastBid() == null) {
			lastBid = auction.getReservePrice();
		} else {
			lastBid = auction.getLastBid().getPrice();
		}

		this.currentPrice.setText("Current price: £" + String.valueOf(lastBid));
		this.currentPrice2.setText("Current price: £" + String.valueOf(lastBid));

		int theMaxBids = auction.getMaxBids();
		String maxAsString = Integer.toString(theMaxBids);
		this.maxBids.setText("Max number of bids: " + maxAsString);

		int currentBids = auction.getBids().size();
		int remainingBids = theMaxBids - currentBids;

		this.remainingBids.setText("Remaining bids: " + Integer.toString(remainingBids));
		this.placedBids.setText("Number of bids placed: " + Integer.toString(currentBids));

		this.startingPrice.setText("Starting Price: £" + String.valueOf(auction.getReservePrice()));

		Date createdDate = auction.getCreationDate();
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		this.auctionCreation.setText("Auction creation: " + dateFormat.format(createdDate));

		// Adds/removes favourite user to/from MegaDB
        EventHandler<ActionEvent> onFavUserClick = (ActionEvent event) -> {
        	if(favUserBtn.isSelected()) {
				MegaDB.getLoggedInUser().addFavouriteUser(this.auction.getCreator());

			} else {
				MegaDB.getLoggedInUser().removeFavouriteUser(this.auction.getCreator());
			}
        };
        favUserBtn.setOnAction(onFavUserClick);

        // Adds/removes favourite auction to/from MegaDB
		EventHandler<ActionEvent> onFavAucClick = (ActionEvent event) -> {
			if(favArtBtn.isSelected()) {
				MegaDB.getLoggedInUser().addFavouriteAuction(this.auction);

			} else {
				MegaDB.getLoggedInUser().removeFavouriteAuction(this.auction);
			}
		};
		favArtBtn.setOnAction(onFavAucClick);

		//Sets checkbox as unchecked if no favourite users exist when switched to
		if(MegaDB.getLoggedInUser().getFavouriteUsers().size() == 0) {
			favUserBtn.setSelected(false);
		} else {
			for (User u : MegaDB.getLoggedInUser().getFavouriteUsers()) {
				//Checkbox filled if user is already a favourite
				if (u.getId() == (this.auction.getCreator().getId())) {
					System.out.println("b");
					favUserBtn.setSelected(true);
				} else {
					favUserBtn.setSelected(false);
				}
			}
		}

		//Sets checkbox as unchecked if no favourite users exist when switched to
		if (MegaDB.getLoggedInUser().getFavouriteAuctions().size() == 0) {
			favArtBtn.setSelected(false);
		} else {
			for (Auction a : user.getFavouriteAuctions()) {
				//Checkbox filled if user is already a favourite
				if (a.getId() == this.auction.getId()) {
					favArtBtn.setSelected(true);
				} else {
					favArtBtn.setSelected(false);
				}
			}
		}

	}

	/**
	 * Switches to screen displaying all auctions
	 * @throws IOException Thrown if fxml file fails to load
	 */
	public void viewAuctionClick() throws IOException {
		onHeaderAction.browseAuctionsClick();
	}

	/**
	 * Switches to create auction screen
	 * @throws IOException Thrown if fxml file fails to load
	 */
	public void createAuctionClick() throws IOException {
		onHeaderAction.createAuctionsClick();
	}

	/**
	 * Switches to screen showing a user's profile
	 * @throws IOException Thrown if fxml file fails to load
	 */
	public void avatarClick() throws IOException {
		onHeaderAction.browseProfileClick();
	}

	/**
	 * Logs a user out and switches them to the login screen
	 * @throws IOException Thrown if fxml file fails to load
	 */
	public void logoutClick() throws IOException {
		onHeaderAction.logoutClick();
	}
}
