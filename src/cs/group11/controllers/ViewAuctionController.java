package cs.group11.controllers;

import cs.group11.models.Artwork;
import cs.group11.models.Auction;
import cs.group11.models.artworks.Painting;
import cs.group11.models.artworks.Sculpture;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
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

        this.artworkImageView.setImage(auction.getArtwork().getImage());
        this.artType.setText(artworkType);
        this.title.setText(artwork.getName());
        this.author.setText(artwork.getArtist());

        this.sellerUsername.setText(auction.getCreator().getUsername());
        this.sellerAvatarImageView.setImage(auction.getCreator().getAvatar());

        this.currentPrice.setText(""); // TODO: Add the current bid price
        this.startingPrice.setText(""); // TODO: Add the starting auction price
        //material
        //depth


//Gets the Width
        double artWidth = 0;
        String widthAsString = Double.toString(artWidth);

        if (artwork instanceof Painting) {
            Painting painting = (Painting) artwork;

            artWidth = painting.getWidth();
            this.width.setText(widthAsString);

        } else if (artwork instanceof Sculpture) {
            Sculpture sculpture = (Sculpture) artwork;

            artWidth = sculpture.getWidth();
            this.width.setText(widthAsString);
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


    }


}
