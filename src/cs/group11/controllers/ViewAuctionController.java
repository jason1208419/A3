package cs.group11.controllers;

import cs.group11.AucListTest;
import cs.group11.models.Artwork;
import cs.group11.models.Auction;
import cs.group11.models.Bid;
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


    public void setAuction(Auction auction) {


        this.auction = auction;

        Artwork artwork = auction.getArtwork();
        String artworkType = "Artwork";

        if (artwork instanceof Painting) {
            artworkType = "Painting";
        } else if (artwork instanceof Sculpture) {
            artworkType = "Sculpture";
        }

        Image image = new Image(auction.getArtwork().getImagePath());
        this.artworkImageView.setImage(image);
        this.artType.setText("Type: " + (artworkType));
        this.title.setText("Title: " + (artwork.getName()));
        this.author.setText("Artist: " + (artwork.getArtist()));

        this.sellerUsername.setText("Seller: " + (auction.getCreator().getUsername()));
        Image avatarImage = new Image(auction.getCreator().getAvatarPath());
        this.sellerAvatarImageView.setImage(avatarImage);


//Get Starting Price (aka reserve price)

        double starterPrice;
        starterPrice = this.auction.getReservePrice();

        this.startingPrice.setText("Starting Price: " + (String.valueOf(starterPrice)));

//Get most recent bid price

        double lastBid;
        lastBid = this.auction.getLastBid().getPrice();

        this.currentPrice.setText("Current price: " + (String.valueOf(lastBid)));

        this.currentPrice2.setText("Current price: " + (String.valueOf(lastBid)));


//Gets the Width and depth and material

        double artWidth = 0;
        double artDepth = 0;
        String widthAsString = Double.toString(artWidth);
        String depthAsString = Double.toString(artDepth);

        if (artwork instanceof Painting) {
            Painting painting = (Painting) artwork;

            artWidth = painting.getWidth();
            this.width.setText("Width: " + widthAsString);

            this.depth.setVisible(false);
            this.material.setVisible(false);


        } else if (artwork instanceof Sculpture) {
            Sculpture sculpture = (Sculpture) artwork;

            artWidth = sculpture.getWidth();
            this.width.setText("Width: " + widthAsString);

            artDepth = sculpture.getDepth();
            this.depth.setText("Depth: " + depthAsString);

            this.material.setText("Material: " + ((Sculpture) artwork).getMaterial());
        }

//Gets the Height
        double artHeight = 0;
        String heightAsString = Double.toString(artHeight);

        if (artwork instanceof Painting) {
            Painting painting = (Painting) artwork;

            artHeight = painting.getHeight();
            this.height.setText("Height: " + heightAsString);

        } else if (artwork instanceof Sculpture) {
            Sculpture sculpture = (Sculpture) artwork;

            artHeight = sculpture.getHeight();
            this.height.setText("Height: " + heightAsString);
        }

//Gets artworks creation year
        int creationYear = artwork.getCreationYear();
        String yearAsString = Integer.toString(creationYear);
        this.artworkCreation.setText("Art creation: " + yearAsString);

//Gets auction creation date
        Date createdDate = auction.getCreationDate();
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        String strCreatedDate = dateFormat.format(createdDate);

        this.auctionCreation.setText("Auction creation: " + strCreatedDate);


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
            Stage primaryStage = AucListTest.getPrimaryStage();
            primaryStage.setScene(viewAuc);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
