package cs.group11.controllers;

import java.io.IOException;

import cs.group11.models.Address;
import cs.group11.models.Auction;
import cs.group11.models.Bid;
import cs.group11.models.User;
import cs.group11.models.artworks.Painting;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class ProfileController {

    @FXML private ImageView avatarImageView;
    @FXML private Label username;
    @FXML private Label firstname;
    @FXML private Label lastname;
    @FXML private Label phoneNumber;
    @FXML private VBox addressLines;
    @FXML private Label postcode;

    @FXML private ListView<Bid> bidsWon;
    @FXML private ListView<Bid> bidsMade;
    @FXML private ListView<Bid> bidsReceived;

    @FXML private ListView<User> favouriteUsers;
    @FXML private ListView<Auction> favouriteAuctions;

    private User user;
    private ObservableList<Bid> bidsWonList;
    private ObservableList<Bid> bidsMadeList;
    private ObservableList<Bid> bidsReceivedList;

    private ObservableList<User> favouriteUsersList;
    private ObservableList<Auction> favouriteAuctionsList;

    @FXML
    protected void initialize() {
        bidsWonList = FXCollections.observableArrayList();
        bidsMadeList = FXCollections.observableArrayList();
        bidsReceivedList = FXCollections.observableArrayList();
        favouriteUsersList = FXCollections.observableArrayList();
        favouriteAuctionsList = FXCollections.observableArrayList();

        bidsWon.setItems(bidsWonList);
        bidsMade.setItems(bidsMadeList);
        bidsReceived.setItems(bidsReceivedList);
        favouriteUsers.setItems(favouriteUsersList);
        favouriteAuctions.setItems(favouriteAuctionsList);

        bidsWon.setCellFactory(param -> new BidListCell());
        bidsMade.setCellFactory(param -> new BidListCell());
        bidsReceived.setCellFactory(param -> new BidListCell());

        favouriteUsers.setCellFactory(param -> null); // FIXME
        favouriteAuctions.setCellFactory(param -> null); // FIXME

        Image avatar = new Image(user.getAvatarPath());
        this.avatarImageView.setImage(avatar);
        this.username.setText(user.getUsername());
        this.firstname.setText(user.getFirstname());
        this.lastname.setText(user.getLastname());
        this.phoneNumber.setText(user.getTelNo());

        for (String addressLine : user.getAddress().getLines()) {
            Label label = new Label(addressLine);
            this.addressLines.getChildren().add(label);
        }

        this.postcode.setText(user.getAddress().getPostcode());

        favouriteUsersList.addAll(user.getFavouriteUsers());
        favouriteAuctionsList.addAll(user.getFavouriteAuctions());

        ChangeListener<Bid> onBidClick = (observable, oldValue, newValue) -> {
            Auction auction = newValue.getAuction();
            System.out.println("Clicked on " + auction.getArtwork().getName());

            // TODO: Change to auction page
        };

        ChangeListener<User> onUserClick = (observable, oldValue, newValue) -> {
            System.out.println("Clicked on " + newValue.getUsername());

            // TODO: Change to user page
        };

        ChangeListener<Auction> onAuctionClick = (observable, oldValue, newValue) -> {
            System.out.println("Clicked on " + newValue.getArtwork().getName());

            // TODO: Change to auction page
        };

        bidsWon.getSelectionModel().selectedItemProperty().addListener(onBidClick);
        bidsMade.getSelectionModel().selectedItemProperty().addListener(onBidClick);
        bidsReceived.getSelectionModel().selectedItemProperty().addListener(onBidClick);
        favouriteUsers.getSelectionModel().selectedItemProperty().addListener(onUserClick);
        favouriteAuctions.getSelectionModel().selectedItemProperty().addListener(onAuctionClick);

        addTestBids();
    }

    private void addTestBids() {
        User creator = this.user;

        String description = "The Starry Night is an oil on canvas by the Dutch post-impressionist painter Vincent van Gogh. Painted in June 1889, " +
                "it depicts the view from the east-facing window of his asylum room at Saint-Rémy-de-Provence, just before sunrise, with the addition " +
                "of an idealized village";

        Image artworkImage = new Image("https://www.moma.org/wp/moma_learning/wp-content/uploads/2012/07/Van-Gogh.-Starry-Night-469x376.jpg");
        Painting painting = new Painting("Starry Night", description, artworkImage, "Vincent Van Gogh", 1889, 200, 300);
        Auction auction = new Auction(creator, 7, 10.00, painting);

        Address bidderAddress = new Address(new String[]{"29 Flintstones Avenue", "Ding Dong Street", "UK"}, "PDT 0KL");
        User bidder = new User("bidder", "Not Nasir", "Not Al Jabbouri", "072481844193", bidderAddress, "/res/avatars/creeper.jpg");

        Bid bid = new Bid(11.21, bidder, auction);
        this.bidsWonList.add(bid);
    }

    public void setUser(User user) {
        this.user = user;
    }

    private class BidListCell extends ListCell<Bid> {
        private Node node;
        private BidCellController controller;

        public BidListCell() {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/bidCell.fxml"));
                node = loader.load();
                controller = loader.getController();
            } catch (IOException exc) {
                throw new RuntimeException(exc);
            }
        }

        @Override
        protected void updateItem(Bid bid, boolean empty) {
            super.updateItem(bid, empty);

            if (empty) {
                setGraphic(null);
            } else {
                controller.setBid(bid);
                setGraphic(node);
            }
        }

    }
}
