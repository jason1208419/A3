package cs.group11.controllers;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import cs.group11.Main;
import cs.group11.models.Artwork;
import cs.group11.models.Auction;
import cs.group11.models.User;
import cs.group11.models.artworks.Painting;
import cs.group11.models.artworks.Sculpture;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

    @FXML
    private VBox rootBox;

    private User user;
    private Auction auction;

    @FXML
    protected void initialize() {
        this.user = Main.getCurrentUser();
        Image img = new Image(user.getAvatarPath());
        this.avatar1.setImage(img);
        this.username1.setText(user.getUsername());

        Artwork artwork = auction.getArtwork();
        this.artType.setText("Type: " + getType(artwork));

        //Gets the Width and depth and material
        if (artwork instanceof Painting) {
            Painting painting = (Painting) artwork;
            this.width.setText("Width: " + Double.toString(painting.getWidth()));
            this.depth.setVisible(false);
            this.material.setVisible(false);

        } else if (artwork instanceof Sculpture) {
            Sculpture sculpture = (Sculpture) artwork;
            this.width.setText("Width: " + Double.toString(sculpture.getWidth()));
            this.depth.setText("Depth: " + Double.toString(sculpture.getDepth()));
            this.material.setText("Material: " + ((Sculpture) artwork).getMaterial());
        }

        this.height.setText("Height: " + getHeight(artwork));

        Image image = new Image(auction.getArtwork().getImagePath());
        this.artworkImageView.setImage(image);
        this.title.setText("Title: " + artwork.getName());
        this.author.setText("Artist: " + artwork.getArtist());

        //Gets artworks creation year
        this.artworkCreation.setText("Art creation: " + Integer.toString(artwork.getCreationYear()));

        this.sellerUsername.setText("Seller: " + auction.getCreator().getUsername());
        Image avatarImage = new Image(auction.getCreator().getAvatarPath());
        this.sellerAvatarImageView.setImage(avatarImage);


        //Get most recent bid price
        double lastBid = this.auction.getLastBid().getPrice();

        this.currentPrice.setText("Current price: " + String.valueOf(lastBid));
        this.currentPrice2.setText("Current price: " + String.valueOf(lastBid));

        //Gets the Maximum amount of bids
        int theMaxBids = auction.getMaxBids();
        String maxAsString = Integer.toString(theMaxBids);
        this.maxBids.setText("Max number of bids: " + maxAsString);

        //Get remaining number of allowed bids
        //Get current number of bids
        int currentBids = auction.getBids().size();
        int remainingBids = theMaxBids - currentBids;

        this.remainingBids.setText("Remaining bids: " + Integer.toString(remainingBids));
        this.placedBids.setText("Number of bids placed: " + Integer.toString(currentBids));

        //Get Starting Price (aka reserve price)
        this.startingPrice.setText("Starting Price: " + String.valueOf(this.auction.getReservePrice()));

        //Gets auction creation date
        Date createdDate = auction.getCreationDate();
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        this.auctionCreation.setText("Auction creation: " + dateFormat.format(createdDate));

        EventHandler<ActionEvent> onButtonClick = (ActionEvent event) -> previousScreen();

        backBtn.setOnAction(onButtonClick);
    }

    public void setUser(User user) {
        this.user = user;
    }

    private String getHeight(Artwork artwork) {
        //Gets the Height
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
    }

    private void previousScreen() {
        try {
            AuctionListController controller = new AuctionListController();


            FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/auctionList.fxml"));
            loader.setController(controller);
            VBox box = loader.load();

            box.prefHeightProperty().bind(rootBox.heightProperty());

            rootBox.getChildren().setAll(box);
        } catch (IOException e) {
            System.out.println("Failed to load fxml file");
        }
    }

    public void viewAuctionClick() throws IOException {
        AuctionListController controller = new AuctionListController();


        FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/auctionList.fxml"));
        loader.setController(controller);
        VBox box = loader.load();

        box.prefHeightProperty().bind(rootBox.heightProperty());

        rootBox.getChildren().setAll(box);
    }

    @FXML
    public void createAuctionClick() throws IOException {
        CreateAuctionV2Controller controller = new CreateAuctionV2Controller();


        FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/createAuctionV2.fxml"));
        loader.setController(controller);
        VBox box = loader.load();

        box.prefHeightProperty().bind(rootBox.heightProperty());

        rootBox.getChildren().setAll(box);
    }

    public void avatarClick() throws IOException {
        ProfileController profileCon = new ProfileController();
        profileCon.setLoginedUser(this.user);
        profileCon.setViewingUser(this.user);

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
}
