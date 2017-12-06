package cs.group11.controllers;

import cs.group11.Main;
import cs.group11.models.Artwork;
import cs.group11.models.Auction;
import cs.group11.models.artworks.Painting;
import cs.group11.models.artworks.Sculpture;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ViewAuctionController {

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
    private Label currentPrice2;
    @FXML
    private Label maxBids;
    @FXML
    private Label placedBids;

    @FXML
    private Label remainingBids;

    @FXML
    private Button backBtn;

    @FXML
    private Label sellerUsername;
    @FXML
    private ImageView sellerAvatarImageView;

    private Auction auction;

    /**
     * Displays info about bids in the GUI.
     */
    private void setBidInfo() {
        //Get most recent bid price
        double lastBid;
        lastBid = this.auction.getLastBid().getPrice();

        this.currentPrice.setText("Current price: " + (String.valueOf(lastBid)));
        this.currentPrice2.setText("Current price: " + (String.valueOf(lastBid)));

        //Gets the Maximum amount of bids
        int theMaxBids = auction.getMaxBids();
        String maxAsString = Integer.toString(theMaxBids);

        this.maxBids.setText("Max number of bids: " + maxAsString);

        //Get remaining number of allowed bids
        //Get current number of bids
        int currentBids;
        int remainingBids;

        currentBids = auction.getBids().size();
        remainingBids = theMaxBids - currentBids;

        String remainingBidsAsString = Integer.toString(remainingBids);
        String currentBidsAsString = Integer.toString(currentBids);

        this.remainingBids.setText("Remaining bids: " + remainingBidsAsString);
        this.placedBids.setText("Number of bids placed: " + currentBidsAsString);
    }

    /**
     * Sets reserve price of auction.
     */
    private void setAucDetails() {
        //Get Starting Price (aka reserve price)

        double starterPrice;
        starterPrice = this.auction.getReservePrice();

        this.startingPrice.setText("Starting Price: " + (String.valueOf(starterPrice)));

        //Gets auction creation date
        Date createdDate = auction.getCreationDate();
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        String strCreatedDate = dateFormat.format(createdDate);

        this.auctionCreation.setText("Auction creation: " + strCreatedDate);
    }

    /**
     * Displays info about artworks in the GUI.
     */
    private void setArtworkDetails() {
        Artwork artwork = auction.getArtwork();
        setArtworkDimensions(artwork);


        Image image = new Image(auction.getArtwork().getImagePath());
        this.artworkImageView.setImage(image);
        this.title.setText("Title: " + (artwork.getName()));
        this.author.setText("Artist: " + (artwork.getArtist()));

        //Gets artworks creation year
        int creationYear = artwork.getCreationYear();
        String yearAsString = Integer.toString(creationYear);
        this.artworkCreation.setText("Art creation: " + yearAsString);

    }

    /**
     * Displays height, width, depth and material of the artwork in the GUI.
     *
     * @param artwork The artwork to be displayed
     */
    private void setArtworkDimensions(Artwork artwork) {
        String artworkType = "Artwork";
        if (artwork instanceof Painting) {
            artworkType = "Painting";
        } else if (artwork instanceof Sculpture) {
            artworkType = "Sculpture";
        }
        this.artType.setText("Type: " + (artworkType));

        //Gets the Width and depth and material
        if (artwork instanceof Painting) {
            Painting painting = (Painting) artwork;

            double artWidth = painting.getWidth();
            String widthAsString = Double.toString(artWidth);
            this.width.setText("Width: " + widthAsString);
            this.depth.setVisible(false);
            this.material.setVisible(false);


        } else if (artwork instanceof Sculpture) {
            Sculpture sculpture = (Sculpture) artwork;

            double artWidth = sculpture.getWidth();
            String widthAsString = Double.toString(artWidth);
            this.width.setText("Width: " + widthAsString);

            double artDepth = sculpture.getDepth();
            String depthAsString = Double.toString(artDepth);
            this.depth.setText("Depth: " + depthAsString);

            this.material.setText("Material: " + ((Sculpture) artwork).getMaterial());
        }

        //Gets the Height
        double artHeight = 0;

        if (artwork instanceof Painting) {
            Painting painting = (Painting) artwork;

            artHeight = painting.getHeight();

        } else if (artwork instanceof Sculpture) {
            Sculpture sculpture = (Sculpture) artwork;

            artHeight = sculpture.getHeight();
        }
        String heightAsString = Double.toString(artHeight);
        this.height.setText("Height: " + heightAsString);
    }


    /**
     * Displays info about the artwork in the GUI.
     */
    private void setUserDetails() {
        this.sellerUsername.setText("Seller: " + (auction.getCreator().getUsername()));
        Image avatarImage = new Image(auction.getCreator().getAvatarPath());
        this.sellerAvatarImageView.setImage(avatarImage);
    }

    /**
     * Displays all info about an auction in the GUI.
     *
     * @param auction
     */
    public void setAuction(Auction auction) {

        this.auction = auction;
        setArtworkDetails();
        setUserDetails();
        setBidInfo();
        setAucDetails();

        EventHandler<ActionEvent> onButtonClick = (ActionEvent event) -> {
            previousScreen();
        };

        backBtn.setOnAction(onButtonClick);
    }

    private void previousScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/auctionList.fxml"));
            Parent root = loader.load();

            AuctionListController controller = loader.getController();
            Scene viewAuc = new Scene(root, 600, 500);
            Stage primaryStage = Main.getPrimaryStage();
            primaryStage.setScene(viewAuc);


        } catch (IOException e) {
            System.out.println("Failed to load fxml file");
        }
    }


}
