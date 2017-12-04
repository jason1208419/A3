package cs.group11.controllers;

import cs.group11.models.Artwork;
import cs.group11.models.Auction;
import cs.group11.models.Bid;
import cs.group11.models.artworks.Painting;
import cs.group11.models.artworks.Sculpture;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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
    ;
    @FXML
    private Label remainingBids;
    ;

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
        this.artType.setText(artworkType);
        this.title.setText(artwork.getName());
        this.author.setText(artwork.getArtist());

        this.sellerUsername.setText(auction.getCreator().getUsername());
        Image avatarImage = new Image(auction.getCreator().getAvatarPath());
        this.sellerAvatarImageView.setImage(avatarImage);


//Get Starting Price (aka reserve price)

        double starterPrice;
        starterPrice = this.auction.getReservePrice();

        this.startingPrice.setText(String.valueOf(starterPrice));

//Get most recent bid price

        double lastBid;
        lastBid = this.auction.getLastBid().getPrice();

        this.currentPrice.setText(String.valueOf(lastBid));

        this.currentPrice2.setText(String.valueOf(lastBid));


//Gets the Width and depth and material

        double artWidth = 0;
        double artDepth = 0;
        String widthAsString = Double.toString(artWidth);
        String depthAsString = Double.toString(artDepth);

        if (artwork instanceof Painting) {
            Painting painting = (Painting) artwork;

            artWidth = painting.getWidth();
            this.width.setText(widthAsString);

        } else if (artwork instanceof Sculpture) {
            Sculpture sculpture = (Sculpture) artwork;

            artWidth = sculpture.getWidth();
            this.width.setText(widthAsString);

            artDepth = sculpture.getDepth();
            this.depth.setText(depthAsString);

            this.material.setText(((Sculpture) artwork).getMaterial());
        }

//Gets the Height
        double artHeight = 0;
        String heightAsString = Double.toString(artHeight);

        if (artwork instanceof Painting) {
            Painting painting = (Painting) artwork;

            artHeight = painting.getWidth();
            this.height.setText(heightAsString);

        } else if (artwork instanceof Sculpture) {
            Sculpture sculpture = (Sculpture) artwork;

            artHeight = sculpture.getHeight();
            this.height.setText(heightAsString);
        }


//Gets artworks creation year
        int creationYear = 0;
        String yearAsString = Integer.toString(creationYear);
        this.artworkCreation.setText(yearAsString);

//Gets auction creation date
        Date createdDate = auction.getCreationDate();
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        String strCreatedDate = dateFormat.format(createdDate);

        this.auctionCreation.setText(strCreatedDate);

//Gets the Maximum amount of bids
        int theMaxBids = 0;
        String maxAsString = Integer.toString(theMaxBids);

        this.maxBids.setText(maxAsString);


//Get remaining number of allowed bids
        //Get current number of bids

        double currentBids;
        double remainingBids;


        currentBids = auction.getBids().size();

        remainingBids = theMaxBids - currentBids;

        String remainingBidsAsString = Double.toString(remainingBids);
        String currentBidsAsString = Double.toString(currentBids);


        this.remainingBids.setText(remainingBidsAsString);
        this.placedBids.setText(currentBidsAsString);

    }


}
