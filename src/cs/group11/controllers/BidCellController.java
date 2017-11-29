package cs.group11.controllers;

import cs.group11.models.Artwork;
import cs.group11.models.Bid;
import cs.group11.models.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BidCellController {

    @FXML private ImageView artworkImageView;
    @FXML private Label artworkName;
    @FXML private Label bidPrice;
    @FXML private Label bidDate;
    @FXML private Label bidderUsername;

    private Bid bid;

    @FXML
    protected void initialize() {

    }

    private void setArtworkImage(Image image) {
        this.artworkImageView.setImage(image);
    }

    private void setArtworkName(String name) {
        this.artworkName.setText(name);
    }

    private void setBidPrice(double price) {
        String formattedPrice = String.format("£%.2f", price);
        this.bidPrice.setText(formattedPrice);
    }

    private void setBidDate(Date date) {
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        String formattedDate = df.format(date);
        this.bidDate.setText(formattedDate);
    }

    private void setBidder(User user) {
        this.bidderUsername.setText(user.getUsername());
    }

    public void setBid(Bid bid) {
        this.bid = bid;

        Artwork artwork = bid.getAuction().getArtwork();

        this.setArtworkImage(artwork.getImage());
        this.setArtworkName(artwork.getName());
        this.setBidDate(bid.getCreationDate());
        this.setBidder(bid.getUser());
        this.setBidPrice(bid.getPrice());
    }
}
