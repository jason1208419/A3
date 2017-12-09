package cs.group11.controllers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import cs.group11.helpers.InvalidDataException;
import cs.group11.helpers.Validator;
import cs.group11.interfaces.*;
import cs.group11.models.*;
import cs.group11.models.artworks.Painting;
import javafx.application.Platform;
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
    private ImageView avatar1;
    @FXML
    private ImageView avatar;
    @FXML
    private Label username1;
    @FXML
    private Label username2;
    private static final FileChooser.ExtensionFilter IMAGE_FILE_EXTENTIONS = new FileChooser.ExtensionFilter("Image Files", "*.png", "*.gif", "*.jpeg", "*.jpg");
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
    @FXML
    private Label error;

    private final int UK_PHONE_MAX_LENGTH = 11;

    private ObservableList<User> favouriteUsersList;
    private ObservableList<Auction> favouriteArtworkList;

    private OnUserClick onUserClick;
    private OnAuctionClick onAuctionClick;
    private OnCancelClick onCancelClick;
    private OnSubmitClick onSubmitClick;
    private OnHeaderAction onHeaderAction;

    private User viewingUser;

    /**
     * set the OnUserClick interface instance
     *
     * @param onUserClick the instance
     */
    public void setOnUserClick(OnUserClick onUserClick) {
        this.onUserClick = onUserClick;
    }

    /**
     * set the OnUserClick interface instance
     * @param onAuctionClick the instance
     */
    public void setOnAuctionClick(OnAuctionClick onAuctionClick) {
        this.onAuctionClick = onAuctionClick;
    }

    /**
     * set the OnCancelClick interface instance
     * @param onCancelClick the instance
     */
    public void setOnCancelClick(OnCancelClick onCancelClick) {
        this.onCancelClick = onCancelClick;
    }

    /**
     * set the OnSubmitClick interface instance
     * @param onSubmitClick the instance
     */
    public void setOnSubmitClick(OnSubmitClick onSubmitClick) {
        this.onSubmitClick = onSubmitClick;
    }

    /**
     * set the OnHeaderClick interface instance
     * @param onHeaderAction the instance
     */
    public void setOnHeaderAction(OnHeaderAction onHeaderAction) {
        this.onHeaderAction = onHeaderAction;
    }

    /**
     * set and display the correct information on the GUI. Allow users to click on the content of table and change to a more detailed page.
     */
    @FXML
    protected void initialize() {
        //setup the two tables
        setupFavouriteArtTable();
        setupFavouriteUserTable();

        //Let users to click on contents inside the favourite user table and show the page of the user being clicked on
        ChangeListener<User> userClicked = (observable, oldValue, newValue) -> {
            //nothing to do when nothing selected
            if (newValue == null) {
                return;
            }

            onUserClick.clicked(newValue);

            Platform.runLater(() -> {
                removeFavouriteUsers.getSelectionModel().clearSelection();
            });
        };

        //Let users to click on contents inside the favourite artwork table and show the page of the artwork being clicked on
        ChangeListener<Auction> auctionClicked = (observable, oldValue, newValue) -> {
            //nothing to do when nothing selected
            if (newValue == null) {
                return;
            }

            onAuctionClick.clicked(newValue);

            Platform.runLater(() -> {
                removeFavouriteArtworks.getSelectionModel().clearSelection();
            });
        };

        phoneIn.textProperty().addListener((observable, oldValue, newValue) -> {
            //nothing to do when nothing selected
            if (newValue == null) {
                return;
            }

            //replace non numerical inputs to ""
            if (!newValue.matches("\\d*")) {
                phoneIn.setText(newValue.replaceAll("[^\\d]", ""));
            } else if (phoneIn.getText().length() > UK_PHONE_MAX_LENGTH) { //can't input more than 11 digits
                phoneIn.setText(phoneIn.getText().substring(0, UK_PHONE_MAX_LENGTH));
            }
        });
        removeFavouriteUsers.getSelectionModel().selectedItemProperty().addListener(userClicked);
        removeFavouriteArtworks.getSelectionModel().selectedItemProperty().addListener(auctionClicked);
    }

    /**
     * Set the user and display it on the page
     * @param user the logined user
     */
    public void setViewingUser(User user) {
        favouriteArtworkList = FXCollections.observableArrayList(user.getFavouriteAuctions());
        favouriteUsersList = FXCollections.observableArrayList(user.getFavouriteUsers());

        //set up the basic details on screen
        Image avatarImage = new Image(user.getAvatarPath());
        this.avatar.setImage(avatarImage);
        this.avatar1.setImage(avatarImage);
        this.username1.setText(user.getUsername());
        this.username2.setText(user.getUsername());

        this.viewingUser = user;
    }

    /**
     * setup the contents inside the favourite artwork table
     */
    private void setupFavouriteArtTable() {
        //add a list of auctions need to be displayed
        favouriteArtworkList = FXCollections.observableArrayList();

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

        //set the name of artwork inside each cell of the name column
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

        //set the artist of artwork inside each cell of the artist column
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

        //set the creation year of artwork inside each cell of the creation year column
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

        //set the remove button inside each cell of the remove column
        tableRemoveArt.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableRemoveArt.setCellFactory(param -> new TableCell<Auction, Auction>() {
            private final Button remove = new Button("Remove");

            @Override
            protected void updateItem(Auction art, boolean empty) {
                super.updateItem(art, empty);

                //nothing to do when nothing to set
                if (art == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(remove);
                remove.setOnAction(event -> {
                    favouriteArtworkList.remove(art);
                    viewingUser.removeFavouriteAuction(art);

                    try {
                        viewingUser.save();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
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
        favouriteUsersList = FXCollections.observableArrayList();

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

                //nothing to do when nothing to set
                if (user1 == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(remove);
                remove.setOnAction(event -> {
                        favouriteUsersList.remove(user1);
                        viewingUser.removeFavouriteUser(user1);
                        try {
                            viewingUser.save();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                );
            }
        });

        //set information for table to display
        removeFavouriteUsers.setItems(favouriteUsersList);
    }

    public void setTestUsers() {
        User df = new User("ggg", "asas", "kijlkl", "07481173742", new Address(new String[]{"29 Flintstones Avenue", "Ding Dong Street", "UK"}, "PDT 0KL"), "http://pixabay.com/static/img/no_hotlinking.png");
        User abc = new User("abc", "Jason", "Lee", "07481173742", new Address(new String[]{"29 Flintstones Avenue", "Ding Dong Street", "UK"}, "PDT 0KL"), "https://www.moma.org/wp/moma_learning/wp-content/uploads/2012/07/Van-Gogh.-Starry-Night-469x376.jpg");
        viewingUser.addFavouriteUser(abc);
        viewingUser.addFavouriteUser(df);
    }

    /**
     * Open a window for the user to use custom image file as their own avatar
     *
     * @return the path of the chosen image
     */
    private String userSelectImage() {
            FileChooser chooser = new FileChooser();
            chooser.getExtensionFilters().add(IMAGE_FILE_EXTENTIONS);
            chooser.setTitle("Select Image");
        File in = chooser.showOpenDialog(avatar.getScene().getWindow());

            //return path if a file chosen
            if (Validator.isFileValid(in)) {
                return in.toURI().toString();
            }
        return null;
    }

    /**
     * This function get called when you pressed the cancel button which will trigger the action
     *
     * @throws IOException Error trace for unsuccessful call of profile fxml or controller
     */
    @FXML
    private void cancelClick() throws IOException {
        this.onCancelClick.cancel(viewingUser);
    }

    /**
     * This function get called when you pressed the avatar button which will trigger the action
     * @throws IOException Error trace for unsuccessful call of profile fxml or controller
     */
    public void avatarClick() throws IOException {
        cancelClick();
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
     * Pop up a window for user to choose avatar from local disk
     */
    public void uploadClick() {
        String currentAvatarPath = viewingUser.getAvatarPath();
        try {
            viewingUser.setAvatarPath(userSelectImage());
            viewingUser.save();
            Image avatarImage = new Image(viewingUser.getAvatarPath());
            this.avatar.setImage(avatarImage);
            this.avatar1.setImage(avatarImage);
        } catch (NullPointerException e) {
            System.out.println("Path not specified");
            viewingUser.setAvatarPath(currentAvatarPath);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Pop up a window to let user create avatar
     * @throws IOException Error trace for unsuccessful call of profile fxml or controller
     */
    public void drawAvatarClick() throws IOException {
        String currentAvatarPath = viewingUser.getAvatarPath();
        Stage drawingStage = new Stage();

        OnSubmitClick onSave = avatarPath -> {
            try {
                viewingUser.setAvatarPath((String) avatarPath);
                viewingUser.save();
                drawingStage.close();
                Image img = new Image(viewingUser.getAvatarPath());
                avatar.setImage(img);
                avatar1.setImage(img);
            } catch (NullPointerException e) {
                System.out.println("Path not specified");
                viewingUser.setAvatarPath(currentAvatarPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        };

        DrawController drawController = new DrawController();
        drawController.setOnSave(onSave);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/drawer.fxml"));
        loader.setController(drawController);
        Parent root = loader.load();
        drawingStage.setTitle("Drawing tool");
        drawingStage.setScene(new Scene(root));
        drawingStage.show();
    }

    /**
     * This function get called when you pressed the submit button which will trigger the action
     * @throws IOException Error trace for unsuccessful call of profile fxml or controller
     */
    public void submitClick() throws IOException {

        //save changes if user input something
        if (firstNameIn.getText() != null && !firstNameIn.getText().isEmpty()) {
            this.viewingUser.setFirstname(firstNameIn.getText());
        }
        //save changes if user input something
        if (lastNameIn.getText() != null && !lastNameIn.getText().isEmpty()) {
            this.viewingUser.setLastname(lastNameIn.getText());
        }
        //save changes if user input something
        if (phoneIn.getText() != null && !phoneIn.getText().isEmpty()) {
            this.viewingUser.setTelNo(phoneIn.getText());
        }
        //save changes if user input something
        if (addressIn.getText() != null && !addressIn.getText().isEmpty()) {
            ArrayList<String> address = new ArrayList<>();
            Collections.addAll(address, addressIn.getText().split("\\n"));
            String[] lines = new String[address.size()];
            lines = address.toArray(lines);
            this.viewingUser.getAddress().setLines(lines);
        }
        //save changes if user input valid postcode
        if (postcodeIn.getText() != null && !postcodeIn.getText().isEmpty()) {
                this.viewingUser.getAddress().setPostcode(postcodeIn.getText());
        }

        try {
            viewingUser.validate();
        } catch (InvalidDataException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("User edit error");
            alert.setHeaderText("There was a problem with your input details.");
            alert.setContentText(e.getMessage());

            alert.showAndWait();
            return;
        }

        //save changes and change to profile page if user input valid postcode
            viewingUser.save();
            onSubmitClick.submit(viewingUser);
            clearAll();
    }

    private void clearAll() {
        firstNameIn.clear();
        lastNameIn.clear();
        phoneIn.clear();
        addressIn.clear();
        postcodeIn.clear();
    }

    /**
     * Pop up a window to let the user choose one of the build in avatar as their own avatar
     * @throws IOException Error trace for unsuccessful call of profile fxml or controller
     */
    public void builtInAvatarClick() throws IOException {
        Stage stage = new Stage();

        OnSubmitClick onAvatarSubmit = avatarPath -> {
            viewingUser.setAvatarPath((String) avatarPath);
            try {
                viewingUser.save();
            } catch (IOException e) {
                e.printStackTrace();
            }
            stage.close();
            Image img = new Image(viewingUser.getAvatarPath());
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
