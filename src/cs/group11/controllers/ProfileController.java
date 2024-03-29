package cs.group11.controllers;

import java.awt.*;
import java.io.IOException;
import java.util.Date;

import cs.group11.MegaDB;
import cs.group11.interfaces.OnAction;
import cs.group11.interfaces.OnAuctionClick;
import cs.group11.interfaces.OnHeaderAction;
import cs.group11.interfaces.OnUserClick;
import cs.group11.models.*;
import cs.group11.models.artworks.Painting;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.scene.paint.Color;

/**
 * Help to make functions of buttons and tables for the profile.fxml
 *
 * @author Kin Wah Lee 689591 & Nasir
 */

public class ProfileController {

    @FXML private ImageView avatarImageView;
    @FXML
    private ImageView avatar1;
    @FXML
    private Label username1;
    @FXML private Label username;
    @FXML private Label firstname;
    @FXML private Label lastname;
    @FXML private Label phoneNumber;
    private final String HIDE = "Confidential Information";
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
    @FXML
    private Label address;

    private OnAuctionClick onAuctionClick;
    private OnUserClick onUserClick;
    private OnHeaderAction onHeaderAction;
    private OnAction onEditProfileClick;

    private ObservableList<Bid> bidsWonList;
    private ObservableList<Bid> bidsMadeList;
    private ObservableList<Bid> bidsReceivedList;


    private ObservableList<Auction> favouriteAuctionsList;
    private User user;

    /**
     * set and display the correct information on the GUI. Allow users to click on the content of table and change to a more detailed page.
     */
    @FXML
    protected void initialize() {
        setupBidsWonTable();
        setupBidsMadeTable();
        setupBidsReceivedTable();

        setupFavouriteUserTable();
        setupFavouriteArtTable();

        ChangeListener<Bid> onBidClick = (observable, oldValue, newValue) -> {
            //nothing to do when nothing selected
            if (newValue == null) {
                return;
            }

            Auction auction = newValue.getAuction();
            onAuctionClick.clicked(auction);

            Platform.runLater(() -> bidsWon.getSelectionModel().clearSelection());
        };

        ChangeListener<User> userClicked = (observable, oldValue, newValue) -> {
            //nothing to do when nothing selected
            if (newValue == null) {
                return;
            }

            onUserClick.clicked(newValue);

            Platform.runLater(() -> favouriteUsers.getSelectionModel().clearSelection());

        };

        ChangeListener<Auction> auctionClicked = (observable, oldValue, newValue) -> {
            //nothing to do when nothing selected
            if (newValue == null) {
                return;
            }

            onAuctionClick.clicked(newValue);

            Platform.runLater(() -> favouriteAuctions.getSelectionModel().clearSelection());
        };


        bidsWon.getSelectionModel().selectedItemProperty().addListener(onBidClick);
        bidsMade.getSelectionModel().selectedItemProperty().addListener(onBidClick);
        bidsReceived.getSelectionModel().selectedItemProperty().addListener(onBidClick);
        favouriteUsers.getSelectionModel().selectedItemProperty().addListener(userClicked);
        favouriteAuctions.getSelectionModel().selectedItemProperty().addListener(auctionClicked);
    }

    /**
     * set the OnAuctionClick interface instance
     *
     * @param onAuctionClick the instance
     */
    public void setOnAuctionClick(OnAuctionClick onAuctionClick) {
        this.onAuctionClick = onAuctionClick;
    }

    /**
     * set the OnUserClick interface instance
     * @param onUserClick the instance
     */
    public void setOnUserClick(OnUserClick onUserClick) {
        this.onUserClick = onUserClick;
    }

    /**
     * set the OnHeaderClick interface instance
     * @param onHeaderAction the instance
     */
    public void setOnHeaderAction(OnHeaderAction onHeaderAction) {
        this.onHeaderAction = onHeaderAction;
    }

    /**
     * set the OnEditProfileClick interface instance
     * @param onEditProfileClick the instance
     */
    public void setOnEditProfileClick(OnAction onEditProfileClick) {
        this.onEditProfileClick = onEditProfileClick;
    }

    /**
     * set the user instance and update the value of that user on the page
     * @param user the user which is being viewed
     */
    public void setViewingUser(User user) {
        this.user = user;

        User loggedInUser = MegaDB.getLoggedInUser();


        bidsWonList = FXCollections.observableArrayList(user.getWonBids());
        bidsWon.setItems(bidsWonList);

        bidsMadeList = FXCollections.observableArrayList(user.getBids());
        bidsMade.setItems(bidsMadeList);


        bidsReceivedList = FXCollections.observableArrayList(user.getReceivedBids());
        bidsReceived.setItems(bidsReceivedList);

        favouriteAuctionsList = FXCollections.observableArrayList(user.getFavouriteAuctions());
        favouriteAuctions.setItems(favouriteAuctionsList);

        Platform.runLater(()->{
            ObservableList<User> favouriteUsersList = FXCollections.observableArrayList(user.getFavouriteUsers());
            favouriteUsers.setItems(favouriteUsersList);
        });


        Image avatar1 = new Image(loggedInUser.getAvatarPath());
        this.avatar1.setImage(avatar1);

        Image avatar = new Image(user.getAvatarPath());
        this.avatarImageView.setImage(avatar);

        this.username1.setText(loggedInUser.getUsername());
        this.username.setText(user.getUsername());

        //the user being viewed is the logined in user
        if (loggedInUser.equals(user)) {
            editProfile.setVisible(true);
            this.firstname.setText(user.getFirstname());
            this.firstname.setTextFill(Color.BLACK);
            this.lastname.setText(user.getLastname());
            this.lastname.setTextFill(Color.BLACK);
            this.phoneNumber.setText(user.getTelNo());
            this.phoneNumber.setTextFill(Color.BLACK);

            String addressResult = "";

            //get the address with next line symbol in the form of string
            for (String addressLine : user.getAddress().getLines()) {
                addressResult += addressLine + "\n";
            }

            //update the address
            if (!this.address.getText().equals(addressResult)) {
                this.address.setText(addressResult);
            }

            this.address.setTextFill(Color.BLACK);

            this.postcode.setText(user.getAddress().getPostcode());
            this.postcode.setTextFill(Color.BLACK);
        } else { //the logined user viewing another user's profile
            editProfile.setVisible(false);
            this.firstname.setText(HIDE);
            this.firstname.setTextFill(Color.RED);
            this.lastname.setText(HIDE);
            this.lastname.setTextFill(Color.RED);
            this.phoneNumber.setText(HIDE);
            this.phoneNumber.setTextFill(Color.RED);
            this.address.setText(HIDE);
            this.address.setTextFill(Color.RED);
            this.postcode.setText(HIDE);
            this.postcode.setTextFill(Color.RED);
        }
    }

    /**
     * setup the table content of bids won
     */
    private void setupBidsWonTable() {
        bidsWonList = FXCollections.observableArrayList();

        wonPic.setCellValueFactory(new PropertyValueFactory<>("auction"));
        wonPic.setPrefWidth(100);
        wonPic.setCellFactory(new Callback<TableColumn<Bid, Auction>, TableCell<Bid, Auction>>() {
            @Override
            public TableCell<Bid, Auction> call(TableColumn<Bid, Auction> param) {
                return new TableCell<Bid, Auction>() {
                    @Override
                    public void updateItem(Auction auction, boolean empty) {
                        super.updateItem(auction, empty);

                        //display nothing inside the table if there is nothing
                        if (empty) {
                            setGraphic(null);
                        } else { //display the content if there is something
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

                        //display nothing inside the table if there is nothing
                        if (empty) {
                            setGraphic(null);
                        } else { //display the content if there is something
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

    }

    /**
     * setup the table content of bids made
     */
    private void setupBidsMadeTable() {
        bidsMadeList = FXCollections.observableArrayList();

        madePic.setCellValueFactory(new PropertyValueFactory<>("auction"));
        madePic.setPrefWidth(100);
        madePic.setCellFactory(new Callback<TableColumn<Bid, Auction>, TableCell<Bid, Auction>>() {
            @Override
            public TableCell<Bid, Auction> call(TableColumn<Bid, Auction> param) {
                return new TableCell<Bid, Auction>() {
                    @Override
                    public void updateItem(Auction auction, boolean empty) {
                        super.updateItem(auction, empty);

                        //display nothing inside the table if there is nothing
                        if (empty) {
                            setGraphic(null);
                        } else { //display the content if there is something
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

                        //display nothing inside the table if there is nothing
                        if (empty) {
                            setGraphic(null);
                        } else { //display the content if there is something
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

    }

    /**
     * setup the table content of bids received
     */
    private void setupBidsReceivedTable() {
        bidsReceivedList = FXCollections.observableArrayList();

        receivedPic.setCellValueFactory(new PropertyValueFactory<>("auction"));
        receivedPic.setPrefWidth(100);
        receivedPic.setCellFactory(new Callback<TableColumn<Bid, Auction>, TableCell<Bid, Auction>>() {
            @Override
            public TableCell<Bid, Auction> call(TableColumn<Bid, Auction> param) {
                return new TableCell<Bid, Auction>() {
                    @Override
                    public void updateItem(Auction auction, boolean empty) {
                        super.updateItem(auction, empty);

                        //display nothing inside the table if there is nothing
                        if (empty) {
                            setGraphic(null);
                        } else { //display the content if there is something
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

                        //display nothing inside the table if there is nothing
                        if (empty) {
                            setGraphic(null);
                        } else { //display the content if there is something
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

                        //display nothing inside the table if there is nothing
                        if (empty) {
                            setGraphic(null);
                        } else { //display the content if there is something
                            Label node = new Label();
                            node.setText(user.getUsername());
                            setGraphic(node);
                        }
                    }
                };
            }
        });

    }

    /**
     * setup the table content of favourite artwork
     */
    private void setupFavouriteArtTable() {

        tablePic.setCellValueFactory(new PropertyValueFactory<>("artwork"));
        tablePic.setPrefWidth(100);
        tablePic.setCellFactory(new Callback<TableColumn<Auction, Artwork>, TableCell<Auction, Artwork>>() {
            @Override
            public TableCell<Auction, Artwork> call(TableColumn<Auction, Artwork> param) {
                return new TableCell<Auction, Artwork>() {
                    @Override
                    public void updateItem(Artwork artwork, boolean empty) {
                        super.updateItem(artwork, empty);

                        //display nothing inside the table if there is nothing
                        if (empty) {
                            setGraphic(null);
                        } else { //display the content if there is something
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

                        //display nothing inside the table if there is nothing
                        if (empty) {
                            setGraphic(null);
                        } else { //display the content if there is something
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

                        //display nothing inside the table if there is nothing
                        if (empty) {
                            setGraphic(null);
                        } else { //display the content if there is something
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

                        //display nothing inside the table if there is nothing
                        if (empty) {
                            setGraphic(null);
                        } else { //display the content if there is something
                            Label node = new Label();
                            node.setText(Integer.toString(artwork.getCreationYear()));
                            setGraphic(node);
                        }
                    }
                };
            }
        });

    }

    /**
     * setup the table content of favourite user
     */
    private void setupFavouriteUserTable() {

        tableAvatar.setCellValueFactory(new PropertyValueFactory<>("avatarPath"));
        tableAvatar.setPrefWidth(100);
        tableAvatar.setCellFactory(new Callback<TableColumn<User, String>, TableCell<User, String>>() {
            @Override
            public TableCell<User, String> call(TableColumn<User, String> param) {
                return new TableCell<User, String>() {
                    @Override
                    public void updateItem(String avatarPath, boolean empty) {
                        super.updateItem(avatarPath, empty);

                        //display nothing inside the table if there is nothing
                        if (empty) {
                            setGraphic(null);
                        } else { //display the content if there is something
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
    }

    /**
     * This function get called when you pressed the avatar button which will trigger the action
     * @throws IOException Error trace for unsuccessful call of profile fxml or controller
     */
    public void avatarClick() throws IOException {
        onHeaderAction.browseProfileClick();
    }

    /**
     * This function get called when you pressed the browse auction button which will trigger the action
     * @throws IOException Error trace for unsuccessful call of profile fxml or controller
     */
    public void viewAuctionClick() throws IOException {
        onHeaderAction.browseAuctionsClick();
    }

    /**
     * This function get called when you pressed the create auction button which will trigger the action
     * @throws IOException Error trace for unsuccessful call of profile fxml or controller
     */
    public void createAuctionClick() throws IOException {
        onHeaderAction.createAuctionsClick();
    }

    /**
     * This function get called when you pressed the logout button which will trigger the action
     * @throws IOException Error trace for unsuccessful call of profile fxml or controller
     */
    public void logoutClick() throws IOException {
        onHeaderAction.logoutClick();
    }

    /**
     * This function get called when you pressed the edit profile button which will trigger the action
     * @throws IOException Error trace for unsuccessful call of profile fxml or controller
     */
    public void editProfileClick() throws IOException {
        onEditProfileClick.call(user);
    }
}