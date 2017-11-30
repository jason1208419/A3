package cs.group11.controllers;

import cs.group11.models.Artwork;
import cs.group11.models.Auction;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class AuctionCellController {

    @FXML
    private ImageView artImage;
    @FXML
    private Label artTitle;
    @FXML
    private Label artCurPrice;
    @FXML
    private Label artType;
    @FXML
    private Label artistName;
    @FXML
    private Label startPrice;
    @FXML
    private Label bidsLeft;

    private Auction auction;


    private void setArtImage(Image image) {
        this.artImage.setImage(image);
    }

    private void setArtTitle(String title) {
        this.artTitle.setText("Artwork name: " + title);
    }

    private void setArtCurPrice(double price) {
        String formattedPrice = String.format("£%.2f", price);
        this.artCurPrice.setText(formattedPrice);
    }

    private void setArtType(String artType) {
        this.artType.setText("Artwork type: " + artType);
    }

    private void setArtistName(String name) {
        this.artistName.setText("Artist name: " + name);
    }

    private void setStartPrice(double price) {
        String formattedPrice = String.format("£%.2f", price);
        this.startPrice.setText(formattedPrice);
    }

    private void setBidsLeft(int bidsLeft) {
        String noBidsLeft = Integer.toString(bidsLeft) + " bids left.";
        this.bidsLeft.setText(noBidsLeft);
    }

    public void viewAuctionInfo(Auction auction) {
        this.auction = auction;
        Artwork artwork = auction.getArtwork();
        this.setArtImage(artwork.getImage());
        this.setArtTitle(artwork.getName());

        boolean emptyAuction = (auction.getBids().size() == 0);
        if (emptyAuction) {
            this.setArtCurPrice(0);
        } else {
            int lastBidIndex = auction.getBids().size() - 1;
            this.setArtCurPrice(auction.getBids().get(lastBidIndex).getPrice());
        }
        this.setArtType(artwork.getClass().getName());
        this.setArtistName(artwork.getArtist());

        if (emptyAuction) {
            this.setStartPrice(0);
        } else {
            this.setStartPrice(auction.getBids().get(0).getPrice());
        }
        this.setBidsLeft(auction.getMaxBids() - auction.getBids().size());
    }



}
