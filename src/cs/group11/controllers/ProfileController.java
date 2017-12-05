package cs.group11.controllers;

import java.io.IOException;

import cs.group11.models.*;
import cs.group11.models.artworks.Painting;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class ProfileController {

    @FXML private ImageView avatarImageView;
    @FXML
    private ImageView avatar1;
    @FXML private Label username;
    @FXML private Label firstname;
    @FXML private Label lastname;
    @FXML private Label phoneNumber;
    @FXML private VBox addressLines;
    @FXML private Label postcode;
    @FXML
    private Button editProfile;

    @FXML private ListView<Bid> bidsWon;
    @FXML private ListView<Bid> bidsMade;
    @FXML private ListView<Bid> bidsReceived;

    @FXML
    private TableView<User> favouriteUsers;
    @FXML
    private TableView<Auction> favouriteAuctions;
    @FXML
    private TableColumn<Auction, Artwork> tablePic;
    @FXML
    private TableColumn<Auction, Artwork> tableName;
    @FXML
    private TableColumn<Auction, Artwork> tableArtist;
    @FXML
    private TableColumn<Auction, Artwork> tableCreationYear;
    @FXML
    private TableColumn<User, String> tableAvatar;
    @FXML
    private TableColumn<User, String> tableUsername;
    @FXML
    private TableColumn<User, String> tableFirstName;
    @FXML
    private TableColumn<User, String> tableLastName;
    @FXML
    private VBox rootBox;
    private EditProfileController editProfileCon = new EditProfileController();

    private User loginedUser;
    private User viewingUser;
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

        bidsWon.setItems(bidsWonList);
        bidsMade.setItems(bidsMadeList);
        bidsReceived.setItems(bidsReceivedList);

        bidsWon.setCellFactory(param -> new BidListCell());
        bidsMade.setCellFactory(param -> new BidListCell());
        bidsReceived.setCellFactory(param -> new BidListCell());

        setupFavouriteUserTable();
        setupFavouriteArtTable();

        Image avatar1 = new Image(loginedUser.getAvatarPath());
        this.avatar1.setImage(avatar1);
        Image avatar = new Image(viewingUser.getAvatarPath());
        this.avatarImageView.setImage(avatar);
        this.username.setText(viewingUser.getUsername());
        this.firstname.setText(viewingUser.getFirstname());
        this.lastname.setText(viewingUser.getLastname());
        this.phoneNumber.setText(viewingUser.getTelNo());

        for (String addressLine : viewingUser.getAddress().getLines()) {
            Label label = new Label(addressLine);
            this.addressLines.getChildren().add(label);
        }

        this.postcode.setText(viewingUser.getAddress().getPostcode());

        if (!loginedUser.equals(viewingUser)) {
            editProfile.setVisible(false);
        }

        ChangeListener<Bid> onBidClick = (observable, oldValue, newValue) -> {
            Auction auction = newValue.getAuction();
            System.out.println("Clicked on " + auction.getArtwork().getName());

            // TODO: Change to auction page
        };

        ChangeListener<User> onUserClick = (observable, oldValue, newValue) -> {
            System.out.println("Clicked on " + newValue.getUsername());

            ProfileController profileCon = new ProfileController();
            profileCon.setViewingUser(newValue);
            profileCon.setLoginedUser(this.loginedUser);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/profile.fxml"));
            loader.setController(profileCon);
            VBox box = null;
            try {
                box = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (box != null) {
                box.prefHeightProperty().bind(rootBox.heightProperty());
            }

            rootBox.getChildren().setAll(box);
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
        User creator = this.loginedUser;

        String description = "The Starry Night is an oil on canvas by the Dutch post-impressionist painter Vincent van Gogh. Painted in June 1889, " +
                "it depicts the view from the east-facing window of his asylum room at Saint-Rémy-de-Provence, just before sunrise, with the addition " +
                "of an idealized village";

        String artworkImagePath = "https://www.moma.org/wp/moma_learning/wp-content/uploads/2012/07/Van-Gogh.-Starry-Night-469x376.jpg";
        Painting painting = new Painting("Starry Night", description, artworkImagePath, "Vincent Van Gogh", 1889, 200, 300);
        Auction auction = new Auction(creator, 7, 10.00, painting);

        Address bidderAddress = new Address(new String[]{"29 Flintstones Avenue", "Ding Dong Street", "UK"}, "PDT 0KL");
        User bidder = new User("bidder", "Not Nasir", "Not Al Jabbouri", "072481844193", bidderAddress, "/res/avatars/creeper.jpg");

        Bid bid = new Bid(11.21, bidder, auction);
        this.bidsWonList.add(bid);
    }

    public void setLoginedUser(User user) {
        this.loginedUser = user;
    }

    public void setViewingUser(User user) {
        this.viewingUser = user;
    }

    private void setupFavouriteArtTable() {
        favouriteAuctionsList = FXCollections.observableArrayList(this.viewingUser.getFavouriteAuctions());

        tablePic.setCellValueFactory(new PropertyValueFactory<>("artwork"));
        tablePic.setPrefWidth(100);
        tablePic.setCellFactory(new Callback<TableColumn<Auction, Artwork>, TableCell<Auction, Artwork>>() {
            @Override
            public TableCell<Auction, Artwork> call(TableColumn<Auction, Artwork> param) {
                return new TableCell<Auction, Artwork>() {
                    @Override
                    public void updateItem(Artwork artwork, boolean empty) {
                        super.updateItem(artwork, empty);

                        if (empty) {
                            setGraphic(null);
                        } else {
                            ImageView node = new ImageView();
                            Image image = new Image(artwork.getImagePath());
                            node.setImage(image);
                            node.setFitWidth(100);
                            node.setPreserveRatio(true);
                            setGraphic(node);
                        }
                    }
                };
            }
        });

        tableName.setCellValueFactory(new PropertyValueFactory<>("artwork"));
        tableName.setCellFactory(new Callback<TableColumn<Auction, Artwork>, TableCell<Auction, Artwork>>() {
            @Override
            public TableCell<Auction, Artwork> call(TableColumn<Auction, Artwork> param) {
                return new TableCell<Auction, Artwork>() {
                    @Override
                    public void updateItem(Artwork artwork, boolean empty) {
                        super.updateItem(artwork, empty);

                        if (empty) {
                            setGraphic(null);
                        } else {
                            Label node = new Label();
                            node.setText(artwork.getName());
                            setGraphic(node);
                        }
                    }
                };
            }
        });
        tableArtist.setCellValueFactory(new PropertyValueFactory<>("artwork"));
        tableArtist.setCellFactory(new Callback<TableColumn<Auction, Artwork>, TableCell<Auction, Artwork>>() {
            @Override
            public TableCell<Auction, Artwork> call(TableColumn<Auction, Artwork> param) {
                return new TableCell<Auction, Artwork>() {
                    @Override
                    public void updateItem(Artwork artwork, boolean empty) {
                        super.updateItem(artwork, empty);

                        if (empty) {
                            setGraphic(null);
                        } else {
                            Label node = new Label();
                            node.setText(artwork.getArtist());
                            setGraphic(node);
                        }
                    }
                };
            }
        });
        tableCreationYear.setCellValueFactory(new PropertyValueFactory<>("artwork"));
        tableCreationYear.setCellFactory(new Callback<TableColumn<Auction, Artwork>, TableCell<Auction, Artwork>>() {
            @Override
            public TableCell<Auction, Artwork> call(TableColumn<Auction, Artwork> param) {
                return new TableCell<Auction, Artwork>() {
                    @Override
                    public void updateItem(Artwork artwork, boolean empty) {
                        super.updateItem(artwork, empty);

                        if (empty) {
                            setGraphic(null);
                        } else {
                            Label node = new Label();
                            node.setText(Integer.toString(artwork.getCreationYear()));
                            setGraphic(node);
                        }
                    }
                };
            }
        });
        favouriteAuctions.setItems(favouriteAuctionsList);
    }

    private void setupFavouriteUserTable() {
        favouriteUsersList = FXCollections.observableArrayList(this.viewingUser.getFavouriteUsers());

        tableAvatar.setCellValueFactory(new PropertyValueFactory<>("avatarPath"));
        tableAvatar.setPrefWidth(100);
        tableAvatar.setCellFactory(new Callback<TableColumn<User, String>, TableCell<User, String>>() {
            @Override
            public TableCell<User, String> call(TableColumn<User, String> param) {
                return new TableCell<User, String>() {
                    @Override
                    public void updateItem(String avatarPath, boolean empty) {
                        super.updateItem(avatarPath, empty);

                        if (empty) {
                            setGraphic(null);
                        } else {
                            ImageView node = new ImageView();

                            Image image = new Image(avatarPath);
                            node.setImage(image);
                            node.setFitWidth(100);
                            node.setPreserveRatio(true);
                            setGraphic(node);
                        }
                    }
                };
            }
        });

        tableUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        tableFirstName.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        tableLastName.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        favouriteUsers.setItems(favouriteUsersList);
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

    public void avatarClick() throws IOException {
        ProfileController profileCon = new ProfileController();
        profileCon.setViewingUser(this.loginedUser);
        profileCon.setLoginedUser(this.loginedUser);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/profile.fxml"));
        loader.setController(profileCon);
        VBox box = loader.load();

        box.prefHeightProperty().bind(rootBox.heightProperty());

        rootBox.getChildren().setAll(box);
    }

    public void viewAuctionClick() throws IOException {
        VBox box = FXMLLoader.load(getClass().getResource("../views/auctionList.fxml"));
        box.prefHeightProperty().bind(rootBox.heightProperty());
        rootBox.getChildren().setAll(box);
    }

    public void createAuctionClick() throws IOException {
        VBox box = FXMLLoader.load(getClass().getResource("../views/createAuctionV2.fxml"));
        box.prefHeightProperty().bind(rootBox.heightProperty());
        rootBox.getChildren().setAll(box);
    }

    public void logoutClick() throws IOException {
        VBox box = FXMLLoader.load(getClass().getResource("../views/signIn.fxml"));
        box.prefHeightProperty().bind(rootBox.heightProperty());
        rootBox.getChildren().setAll(box);
    }

    public void editProfileClick() throws IOException {
        editProfileCon.setUser(this.loginedUser);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/editProfile.fxml"));
        loader.setController(editProfileCon);
        VBox box = loader.load();

        box.prefHeightProperty().bind(rootBox.heightProperty());

        rootBox.getChildren().setAll(box);
    }
}