package cs.group11.controllers;

import cs.group11.models.Artwork;
import cs.group11.models.Auction;
import cs.group11.models.artworks.Sculpture;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Handles fxml file that holds auction data in a custom ListCell.
 *
 * @Author Thomas Collett
 */
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

    /**
     * Sets image in cell based on path provided.
     * @param imagePath The path of the image to be displayed.
     */
    private void setArtImagePath(String imagePath) {
        Image image = new Image(imagePath);
        this.artImage.setImage(image);
    }

    /**
     * Sets the title of the artwork in the GUI.
     * @param title The title of the artwork.
     */
    private void setArtTitle(String title) {
        this.artTitle.setText("Artwork name: " + title);
    }

    /**
     * Sets the current price of the artwork in the GUI.
     * @param price The current price of the artwork.
     */
    private void setArtCurPrice(double price) {
        String formattedPrice = String.format("£%.2f", price);
        this.artCurPrice.setText(formattedPrice);
    }

    /**
     * Sets the type of artwork being auctioned in the GUI.
     * @param artType The type of artwork being auctioned.
     */
    private void setArtType(String artType) {
        this.artType.setText("Artwork type: " + artType);
    }

    /**
     * Sets the name of the artist in the GUI.
     * @param name The name of the artist.
     */
    private void setArtistName(String name) {
        this.artistName.setText("Artist name: " + name);
    }

    /**
     * Sets the starting price of the auction in the GUI
     * @param price The starting price of the auction.
     */
    private void setStartPrice(double price) {
        String formattedPrice = String.format("£%.2f", price);
        this.startPrice.setText(formattedPrice);
    }

    /**
     * Sets the number of bids left on an auction in the GUI.
     * @param bidsLeft The number of bids left on an auction.
     */
    private void setBidsLeft(int bidsLeft) {
        String noBidsLeft = Integer.toString(bidsLeft) + " bids left.";
        this.bidsLeft.setText(noBidsLeft);
    }

    /**
     * Obtains the auction's data and displays it in the GUI.
     * @param auction The auction to be displayed in the GUI.
     */
    public void viewAuctionInfo(Auction auction) {
        this.auction = auction;
        Artwork artwork = auction.getArtwork();
        this.setArtImagePath(artwork.getImagePath());
        this.setArtTitle(artwork.getName());

        boolean emptyAuction = (auction.getBids().size() == 0);
        if (emptyAuction) {
            this.setArtCurPrice(0);
        } else {
            int lastBidIndex = auction.getBids().size() - 1;
            this.setArtCurPrice(auction.getBids().get(lastBidIndex).getPrice());
        }

        String type;
        if (artwork instanceof Sculpture) {
            type = "Sculpture";
        } else {
            type = "Painting";
        }

        this.setArtType(type);
        this.setArtistName(artwork.getArtist());

        if (emptyAuction) {
            this.setStartPrice(0);
        } else {
            this.setStartPrice(auction.getBids().get(0).getPrice());
        }
        this.setBidsLeft(auction.getMaxBids() - auction.getBids().size());
    }


}
