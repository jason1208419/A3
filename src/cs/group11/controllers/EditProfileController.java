package cs.group11.controllers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import cs.group11.Main;
import cs.group11.MegaDB;
import cs.group11.helpers.Validator;
import cs.group11.models.*;
import cs.group11.models.artworks.Painting;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * Help to make functions of buttons and tables for the editProfile.fxml
 *
 * @author Kin Wah Lee 689591
 */
public class EditProfileController {
    @FXML
    private ImageView logo;
    @FXML
    private ImageView avatar1;
    @FXML
    private ImageView avatar;
    @FXML
    private Label username1;
    @FXML
    private Label username2;
    private static final FileChooser.ExtensionFilter IMAGE_FILE_EXTENTIONS = new FileChooser.ExtensionFilter("Image Files", ".png", ".gif", ".jpeg", ".jpg");
    @FXML
    private TableView<Auction> removeFavouriteArtworks;
    @FXML
    private TableView<User> removeFavouriteUsers;
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
    private TableColumn<User, User> tableRemoveUser = new TableColumn<>("Remove");
    @FXML
    private TableColumn<Auction, Auction> tableRemoveArt = new TableColumn<>("Remove");
    @FXML
    private VBox rootBox;
    @FXML
    private TextField firstNameIn;
    @FXML
    private TextField lastNameIn;
    @FXML
    private TextField phoneIn;
    @FXML
    private TextArea addressIn;
    @FXML
    private TextField postcodeIn;

    private User user;
    @FXML
    private Label error;

    private ObservableList<User> favouriteUsersList;
    private ObservableList<Auction> favouriteArtworkList;

    /**
     * set and display the correct information on the GUI. Allow users to click on the content of table and change to a more detailed page.
     */
    @FXML
    protected void initialize() {
        //set up the basic details on screen
        this.user = Main.getCurrentUser();
        Image avatarImage = new Image(user.getAvatarPath());
        this.logo.setImage(avatarImage);
        this.avatar.setImage(avatarImage);
        this.avatar1.setImage(avatarImage);
        this.username1.setText(user.getUsername());
        this.username2.setText(user.getUsername());

        //setup the two tables
        setupFavouriteArtTable();
        setupFavouriteUserTable();

        //Let users to click on contents inside the favourite user table and show the page of the user being clicked on
        ChangeListener<User> onUserClick = (observable, oldValue, newValue) -> {
            System.out.println("Clicked on " + newValue.getUsername());

            ProfileController profileCon = new ProfileController();
            profileCon.setViewingUser(newValue);
            profileCon.setLoginedUser(this.user);

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

        //Let users to click on contents inside the favourite artwork table and show the page of the artwork being clicked on
        ChangeListener<Auction> onAuctionClick = (observable, oldValue, newValue) -> {
            System.out.println("Clicked on " + newValue.getArtwork().getName());

            ViewAuctionController controller = new ViewAuctionController();
            controller.setUser(this.user);
            controller.setAuction(newValue);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/viewAuction.fxml"));
            loader.setController(controller);
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
        removeFavouriteUsers.getSelectionModel().selectedItemProperty().addListener(onUserClick);
        removeFavouriteArtworks.getSelectionModel().selectedItemProperty().addListener(onAuctionClick);
    }

    /**
     * setup the contents inside the favourite artwork table
     */
    private void setupFavouriteArtTable() {
        //add a list of auctions need to be displayed
        favouriteArtworkList = FXCollections.observableArrayList(this.user.getFavouriteAuctions());

        //set the picture of artwork inside each cell of the picture column
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

        //set the name of artwork inside each cell of the name column
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

        //set the artist of artwork inside each cell of the artist column
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

        //set the creation year of artwork inside each cell of the creation year column
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

        //set the remove button inside each cell of the remove column
        tableRemoveArt.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableRemoveArt.setCellFactory(param -> new TableCell<Auction, Auction>() {
            private final Button remove = new Button("Remove");

            @Override
            protected void updateItem(Auction art, boolean empty) {
                super.updateItem(art, empty);
                if (art == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(remove);
                remove.setOnAction(
                        event -> {
                            favouriteArtworkList.remove(art);
                            user.removeFavouriteAuction(art);
                            try {
                                MegaDB.save();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            printFavouriteAuction();
                        }
                );
            }
        });

        //set information for table to display
        removeFavouriteArtworks.setItems(favouriteArtworkList);
    }

    /**
     * setup the contents inside the favourite user table
     */
    private void setupFavouriteUserTable() {
        //add a list of users need to be displayed
        favouriteUsersList = FXCollections.observableArrayList(this.user.getFavouriteUsers());

        //set the avatar of user inside each cell of the avatar column
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

        //set the username of user inside each cell of the username column
        tableUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        //set the first name of user inside each cell of the first name column
        tableFirstName.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        //set the last name of user inside each cell of the last name column
        tableLastName.setCellValueFactory(new PropertyValueFactory<>("lastname"));

        //set the remove button inside each cell of the remove column
        tableRemoveUser.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableRemoveUser.setCellFactory(param -> new TableCell<User, User>() {
            private final Button remove = new Button("Remove");

            @Override
            protected void updateItem(User user1, boolean empty) {
                super.updateItem(user1, empty);
                if (user1 == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(remove);
                remove.setOnAction(
                        event -> {
                            favouriteUsersList.remove(user1);
                            user.removeFavouriteUser(user1);
                            try {
                                MegaDB.save();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            printFavouriteUsers();
                        }
                );
            }
        });

        //set information for table to display
        removeFavouriteUsers.setItems(favouriteUsersList);
    }

    public void setTestArt() {
        User creator = new User("ggg", "asas", "kijlkl", "07481173742", new Address(new String[]{"29 Flintstones Avenue", "Ding Dong Street", "UK"}, "PDT 0KL"), "http://pixabay.com/static/img/no_hotlinking.png");

        String description = "The Starry Night is an oil on canvas by the Dutch post-impressionist painter Vincent van Gogh. Painted in June 1889, " +
                "it depicts the view from the east-facing window of his asylum room at Saint-Rémy-de-Provence, just before sunrise, with the addition " +
                "of an idealized village";

        String imgPath = "https://www.moma.org/wp/moma_learning/wp-content/uploads/2012/07/Van-Gogh.-Starry-Night-469x376.jpg";
        Painting painting = new Painting("Starry Night", description, imgPath, "Vincent Van Gogh", 1889, 200, 300);
        Auction auction = new Auction(creator, 7, 10.00, painting);
        Bid testBid = new Bid(15.25, creator, auction);
        this.user.addFavouriteAuction(auction);

    }

    public void setTestUsers() {
        User df = new User("ggg", "asas", "kijlkl", "07481173742", new Address(new String[]{"29 Flintstones Avenue", "Ding Dong Street", "UK"}, "PDT 0KL"), "http://pixabay.com/static/img/no_hotlinking.png");
        User abc = new User("abc", "Jason", "Lee", "07481173742", new Address(new String[]{"29 Flintstones Avenue", "Ding Dong Street", "UK"}, "PDT 0KL"), "https://www.moma.org/wp/moma_learning/wp-content/uploads/2012/07/Van-Gogh.-Starry-Night-469x376.jpg");
        this.user.addFavouriteUser(abc);
        this.user.addFavouriteUser(df);
    }

    private void printFavouriteUsers() {
        System.out.println(user.getUsername() + "'s Favourite User:");
        for (int i = 0; i < user.getFavouriteUsers().size(); i++) {
            System.out.println(user.getFavouriteUsers().get(i).getUsername());
        }
    }

    private void printFavouriteAuction() {
        System.out.println(user.getUsername() + "'s Favourite Auction:");
        for (int i = 0; i < user.getFavouriteAuctions().size(); i++) {
            System.out.println(user.getFavouriteAuctions().get(i).getArtwork().getName());
        }
    }

    /**
     * Open a window for the user to use custom image file as their own avatar
     *
     * @return the path of the chosen image
     */
    private String userSelectImage() {
        FileChooser chooser = new FileChooser();
        chooser.setSelectedExtensionFilter(IMAGE_FILE_EXTENTIONS);
        chooser.setTitle("Select Image");
        File in = chooser.showOpenDialog(null);

        //return path if a file chosen
        if (Validator.isFileValid(in)) {
            return in.toURI().toString();
        }
        return null;
    }

    /**
     * Change to the profile page when cancel button clicked
     *
     * @throws IOException Error trace for unsuccessful call of profile fxml or controller
     */
    @FXML
    private void cancelClick() throws IOException {

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

    /**
     * Change to the profile page when avatar on header clicked
     * @throws IOException Error trace for unsuccessful call of profile fxml or controller
     */
    public void avatarClick() throws IOException {
        cancelClick();
    }

    /**
     * Change to the auction list page when browse auction button clicked
     * @throws IOException Error trace for unsuccessful call of profile fxml or controller
     */
    public void viewAuctionClick() throws IOException {
        AuctionListController controller = new AuctionListController();


        FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/auctionList.fxml"));
        loader.setController(controller);
        VBox box = loader.load();

        box.prefHeightProperty().bind(rootBox.heightProperty());

        rootBox.getChildren().setAll(box);
    }

    /**
     * Change to the create auction page when create auction button clicked
     * @throws IOException Error trace for unsuccessful call of profile fxml or controller
     */
    public void createAuctionClick() throws IOException {
        CreateAuctionV2Controller controller = new CreateAuctionV2Controller();


        FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/createAuctionV2.fxml"));
        loader.setController(controller);
        VBox box = loader.load();

        box.prefHeightProperty().bind(rootBox.heightProperty());

        rootBox.getChildren().setAll(box);
    }

    /**
     * Change to the sign in page when logout button clicked
     * @throws IOException Error trace for unsuccessful call of profile fxml or controller
     */
    public void logoutClick() throws IOException {
        VBox box = FXMLLoader.load(getClass().getResource("../views/signIn.fxml"));
        box.prefHeightProperty().bind(rootBox.heightProperty());
        rootBox.getChildren().setAll(box);
    }

    /**
     * Pop up a window for user to choose avatar from local disk
     */
    public void uploadClick() {
        String currentAvatarPath = user.getAvatarPath();
        try {
            user.setAvatarPath(userSelectImage());
            MegaDB.save();
            Image avatarImage = new Image(user.getAvatarPath());
            this.avatar.setImage(avatarImage);
            this.avatar1.setImage(avatarImage);
        } catch (NullPointerException e) {
            System.out.println("Path not specified");
            user.setAvatarPath(currentAvatarPath);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Pop up a window to let user create avatar
     */
    public void drawAvatarClick() throws IOException {
        Stage drawingStage = new Stage();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/drawer.fxml"));
        Parent root = loader.load();
        drawingStage.setTitle("Drawing tool");
        drawingStage.setScene(new Scene(root));
        drawingStage.show();
    }

    /**
     * Change to the profile page and save changed data when submit button clicked
     * @throws IOException Error trace for unsuccessful call of profile fxml or controller
     */
    public void submitClick() throws IOException {
        boolean inputValid = true;
        printUser();

        //save changes if user input something
        if (firstNameIn.getText() != null && !firstNameIn.getText().isEmpty()) {
            this.user.setFirstname(firstNameIn.getText());
        }
        //save changes if user input something
        if (lastNameIn.getText() != null && !lastNameIn.getText().isEmpty()) {
            this.user.setLastname(lastNameIn.getText());
        }
        //save changes if user input something
        if (phoneIn.getText() != null && !phoneIn.getText().isEmpty()) {
            this.user.setTelNo(phoneIn.getText());
        }
        //save changes if user input something
        if (addressIn.getText() != null && !addressIn.getText().isEmpty()) {
            ArrayList<String> address = new ArrayList<>();
            Collections.addAll(address, addressIn.getText().split("\\n"));
            String[] lines = new String[address.size()];
            lines = address.toArray(lines);
            this.user.getAddress().setLines(lines);
        }
        //save changes if user input valid postcode
        if (postcodeIn.getText() != null && !postcodeIn.getText().isEmpty()) {
            if (Address.isPostcodeValid(postcodeIn.getText())) {
                inputValid = true;
                this.user.getAddress().setPostcode(postcodeIn.getText());
            } else {
                inputValid = false;
                error.setText("Postcode not valid!");
            }
        }
        //save changes and change to profile page if user input valid postcode
        if (inputValid) {
            printUser();
            MegaDB.save();

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
    }

    private void printUser() {
        System.out.println("Username: " + this.user.getUsername() + "\nFirst Name: " + this.user.getFirstname() + "\nLast Name: " + this.user.getLastname() + "\nPhone Number: " + this.user.getTelNo() + "\nAddress:");
        for (int i = 0; i < this.user.getAddress().getLines().length; i++) {
            System.out.println(this.user.getAddress().getLine(i + 1));
        }
        System.out.println(this.user.getAddress().getPostcode());
    }

    /**
     * Pop up a window to let the user choose one of the build in avatar as their own avatar
     * @throws IOException Error trace for unsuccessful call of profile fxml or controller
     */
    public void builtInAvatarClick() throws IOException {
        Stage stage = new Stage();

        BuildInAvatarController.OnAvatarSubmit onAvatarSubmit = avatarPath -> {
            user.setAvatarPath(avatarPath);
            try {
                MegaDB.save();
            } catch (IOException e) {
                e.printStackTrace();
            }
            stage.close();
            Image img = new Image(user.getAvatarPath());
            avatar.setImage(img);
            avatar1.setImage(img);
        };

        BuildInAvatarController buildInAvatarController = new BuildInAvatarController();
        buildInAvatarController.setOnAvatarSubmit(onAvatarSubmit);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../views/buildInAvatar.fxml"));
        fxmlLoader.setController(buildInAvatarController);

        Parent root1 = fxmlLoader.load();
        stage.setTitle("Build In Avatar");
        stage.setScene(new Scene(root1));
        stage.show();
    }
}
