package cs.group11.controllers;

import java.io.IOException;
import java.util.Date;

import cs.group11.Main;
import cs.group11.models.*;
import cs.group11.models.artworks.Painting;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
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

    @FXML
    private TableView<Bid> bidsWon;
    @FXML
    private TableView<Bid> bidsMade;
    @FXML
    private TableView<Bid> bidsReceived;
    @FXML
    private TableView<User> favouriteUsers;
    @FXML
    private TableView<Auction> favouriteAuctions;
    @FXML
    private TableColumn<Bid, Auction> wonPic;
    @FXML
    private TableColumn<Bid, Auction> wonName;
    @FXML
    private TableColumn<Bid, Double> wonPrice;
    @FXML
    private TableColumn<Bid, Date> wonDate;
    @FXML
    private TableColumn<Bid, Auction> madePic;
    @FXML
    private TableColumn<Bid, Auction> madeName;
    @FXML
    private TableColumn<Bid, Double> madePrice;
    @FXML
    private TableColumn<Bid, Date> madeDate;
    @FXML
    private TableColumn<Bid, Auction> receivedPic;
    @FXML
    private TableColumn<Bid, Auction> receivedName;
    @FXML
    private TableColumn<Bid, Double> receivedPrice;
    @FXML
    private TableColumn<Bid, Date> receivedDate;
    @FXML
    private TableColumn<Bid, User> receivedUsername;
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
        setupBidsWonTable();
        setupBidsMadeTable();
        setupBidsReceivedTable();

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
    }

    public void addTestBids() {
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
        this.loginedUser.addBid(bid);
    }

    public void setLoginedUser(User user) {
        this.loginedUser = user;
    }

    public void setViewingUser(User user) {
        this.viewingUser = user;
    }

    private void setupBidsWonTable() {
        bidsWonList = FXCollections.observableArrayList(this.viewingUser.getWonBids());

        wonPic.setCellValueFactory(new PropertyValueFactory<>("auction"));
        wonPic.setPrefWidth(100);
        wonPic.setCellFactory(new Callback<TableColumn<Bid, Auction>, TableCell<Bid, Auction>>() {
            @Override
            public TableCell<Bid, Auction> call(TableColumn<Bid, Auction> param) {
                return new TableCell<Bid, Auction>() {
                    @Override
                    public void updateItem(Auction auction, boolean empty) {
                        super.updateItem(auction, empty);

                        if (empty) {
                            setGraphic(null);
                        } else {
                            ImageView node = new ImageView();
                            Image image = new Image(auction.getArtwork().getImagePath());
                            node.setImage(image);
                            node.setFitWidth(100);
                            node.setPreserveRatio(true);
                            setGraphic(node);
                        }
                    }
                };
            }
        });

        wonName.setCellValueFactory(new PropertyValueFactory<>("auction"));
        wonName.setCellFactory(new Callback<TableColumn<Bid, Auction>, TableCell<Bid, Auction>>() {
            @Override
            public TableCell<Bid, Auction> call(TableColumn<Bid, Auction> param) {
                return new TableCell<Bid, Auction>() {
                    @Override
                    public void updateItem(Auction auction, boolean empty) {
                        super.updateItem(auction, empty);

                        if (empty) {
                            setGraphic(null);
                        } else {
                            Label node = new Label();
                            node.setText(auction.getArtwork().getName());
                            setGraphic(node);
                        }
                    }
                };
            }
        });
        wonPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        wonDate.setCellValueFactory(new PropertyValueFactory<>("creationDate"));
        bidsWon.setItems(bidsWonList);
    }

    private void setupBidsMadeTable() {
        bidsMadeList = FXCollections.observableArrayList(this.viewingUser.getBids());

        madePic.setCellValueFactory(new PropertyValueFactory<>("auction"));
        madePic.setPrefWidth(100);
        madePic.setCellFactory(new Callback<TableColumn<Bid, Auction>, TableCell<Bid, Auction>>() {
            @Override
            public TableCell<Bid, Auction> call(TableColumn<Bid, Auction> param) {
                return new TableCell<Bid, Auction>() {
                    @Override
                    public void updateItem(Auction auction, boolean empty) {
                        super.updateItem(auction, empty);

                        if (empty) {
                            setGraphic(null);
                        } else {
                            ImageView node = new ImageView();
                            Image image = new Image(auction.getArtwork().getImagePath());
                            node.setImage(image);
                            node.setFitWidth(100);
                            node.setPreserveRatio(true);
                            setGraphic(node);
                        }
                    }
                };
            }
        });

        madeName.setCellValueFactory(new PropertyValueFactory<>("auction"));
        madeName.setCellFactory(new Callback<TableColumn<Bid, Auction>, TableCell<Bid, Auction>>() {
            @Override
            public TableCell<Bid, Auction> call(TableColumn<Bid, Auction> param) {
                return new TableCell<Bid, Auction>() {
                    @Override
                    public void updateItem(Auction auction, boolean empty) {
                        super.updateItem(auction, empty);

                        if (empty) {
                            setGraphic(null);
                        } else {
                            Label node = new Label();
                            node.setText(auction.getArtwork().getName());
                            setGraphic(node);
                        }
                    }
                };
            }
        });
        madePrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        madeDate.setCellValueFactory(new PropertyValueFactory<>("creationDate"));
        bidsMade.setItems(bidsMadeList);
    }

    private void setupBidsReceivedTable() {
        bidsReceivedList = FXCollections.observableArrayList(this.viewingUser.getReceivedBids());

        receivedPic.setCellValueFactory(new PropertyValueFactory<>("auction"));
        receivedPic.setPrefWidth(100);
        receivedPic.setCellFactory(new Callback<TableColumn<Bid, Auction>, TableCell<Bid, Auction>>() {
            @Override
            public TableCell<Bid, Auction> call(TableColumn<Bid, Auction> param) {
                return new TableCell<Bid, Auction>() {
                    @Override
                    public void updateItem(Auction auction, boolean empty) {
                        super.updateItem(auction, empty);

                        if (empty) {
                            setGraphic(null);
                        } else {
                            ImageView node = new ImageView();
                            Image image = new Image(auction.getArtwork().getImagePath());
                            node.setImage(image);
                            node.setFitWidth(100);
                            node.setPreserveRatio(true);
                            setGraphic(node);
                        }
                    }
                };
            }
        });

        receivedName.setCellValueFactory(new PropertyValueFactory<>("auction"));
        receivedName.setCellFactory(new Callback<TableColumn<Bid, Auction>, TableCell<Bid, Auction>>() {
            @Override
            public TableCell<Bid, Auction> call(TableColumn<Bid, Auction> param) {
                return new TableCell<Bid, Auction>() {
                    @Override
                    public void updateItem(Auction auction, boolean empty) {
                        super.updateItem(auction, empty);

                        if (empty) {
                            setGraphic(null);
                        } else {
                            Label node = new Label();
                            node.setText(auction.getArtwork().getName());
                            setGraphic(node);
                        }
                    }
                };
            }
        });
        receivedPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        receivedDate.setCellValueFactory(new PropertyValueFactory<>("creationDate"));
        receivedUsername.setCellValueFactory(new PropertyValueFactory<>("user"));
        receivedUsername.setCellFactory(new Callback<TableColumn<Bid, User>, TableCell<Bid, User>>() {
            @Override
            public TableCell<Bid, User> call(TableColumn<Bid, User> param) {
                return new TableCell<Bid, User>() {
                    @Override
                    public void updateItem(User user, boolean empty) {
                        super.updateItem(user, empty);

                        if (empty) {
                            setGraphic(null);
                        } else {
                            Label node = new Label();
                            node.setText(user.getUsername());
                            setGraphic(node);
                        }
                    }
                };
            }
        });
        bidsReceived.setItems(bidsReceivedList);
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/auctionList.fxml"));
        Parent root = loader.load();
        AuctionListController controller = loader.getController();
        Scene viewAuc = new Scene(root, 600, 500);
        Stage primaryStage = Main.getPrimaryStage();
        primaryStage.setScene(viewAuc);
    }

    public void createAuctionClick() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/createAuctionV2.fxml"));
        Parent root = loader.load();
        CreateAuctionV2Controller controller = loader.getController();
        Scene createAuc = new Scene(root, 600, 500);
        Stage primaryStage = Main.getPrimaryStage();
        primaryStage.setScene(createAuc);
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
        Parent root = loader.load();

        EditProfileController controller = loader.getController();
        Scene viewAuc = new Scene(root, 600, 500);
        Stage primaryStage = Main.getPrimaryStage();
        primaryStage.setScene(viewAuc);
    }
}