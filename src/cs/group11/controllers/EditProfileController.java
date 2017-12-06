package cs.group11.controllers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import cs.group11.Main;
import cs.group11.helpers.Validator;
import cs.group11.models.Address;
import cs.group11.models.Artwork;
import cs.group11.models.Auction;
import cs.group11.models.User;
import cs.group11.models.artworks.Painting;
import javafx.beans.property.ReadOnlyObjectWrapper;
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

    @FXML
    protected void initialize() {
        Image avatarImage = new Image(user.getAvatarPath());
        this.logo.setImage(avatarImage);
        this.avatar.setImage(avatarImage);
        this.avatar1.setImage(avatarImage);
        this.username1.setText(user.getUsername());
        this.username2.setText(user.getUsername());

        setupFavouriteArtTable();
        setupFavouriteUserTable();
    }

    public void setUser(User user) {
        this.user = user;
    }

    private void setupFavouriteArtTable() {
        favouriteArtworkList = FXCollections.observableArrayList(this.user.getFavouriteAuctions());

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
                            printFavouriteAuction();
                        }
                );
            }
        });
        removeFavouriteArtworks.setItems(favouriteArtworkList);
    }

    private void setupFavouriteUserTable() {
        favouriteUsersList = FXCollections.observableArrayList(this.user.getFavouriteUsers());

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
                            printFavouriteUsers();
                        }
                );
            }
        });
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

    private String userSelectImage() {
        FileChooser chooser = new FileChooser();
        chooser.setSelectedExtensionFilter(IMAGE_FILE_EXTENTIONS);
        chooser.setTitle("Select Image");
        File in = chooser.showOpenDialog(null);
        if (Validator.isFileValid(in)) {
            return in.toURI().toString();
        }
        return null;
    }

    //TODO: A user logs in to the program and performs actions which are saved locally to disk on that machine

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

    public void avatarClick() throws IOException {
        cancelClick();
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

    public void uploadClick() {
        String currentAvatarPath = user.getAvatarPath();
        try {
            user.setAvatarPath(userSelectImage());
            Image avatarImage = new Image(user.getAvatarPath());
            this.avatar.setImage(avatarImage);
            this.avatar1.setImage(avatarImage);
        } catch (NullPointerException e) {
            System.out.println("Path not specified");
            user.setAvatarPath(currentAvatarPath);
        }

    }

    public void drawAvatarClick() {
    }

    public void submitClick() throws IOException {
        printUser();
        //TODO:validate input
        if (firstNameIn.getText() != null && !firstNameIn.getText().isEmpty()) {
            this.user.setFirstname(firstNameIn.getText());
        }
        if (lastNameIn.getText() != null && !lastNameIn.getText().isEmpty()) {
            this.user.setLastname(lastNameIn.getText());
        }
        if (phoneIn.getText() != null && !phoneIn.getText().isEmpty()) {
            this.user.setTelNo(phoneIn.getText());
        }
        if (addressIn.getText() != null && !addressIn.getText().isEmpty()) {
            ArrayList<String> address = new ArrayList<>();
            Collections.addAll(address, addressIn.getText().split("\\n"));
            String[] lines = new String[address.size()];
            lines = address.toArray(lines);
            this.user.getAddress().setLines(lines);
        }
        if (postcodeIn.getText() != null && !postcodeIn.getText().isEmpty()) {
            this.user.getAddress().setPostcode(postcodeIn.getText());
        }
        printUser();

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

    private void printUser() {
        System.out.println("Username: " + this.user.getUsername() + "\nFirst Name: " + this.user.getFirstname() + "\nLast Name: " + this.user.getLastname() + "\nPhone Number: " + this.user.getTelNo() + "\nAddress:");
        for (int i = 0; i < this.user.getAddress().getLines().length; i++) {
            System.out.println(this.user.getAddress().getLine(i + 1));
        }
        System.out.println(this.user.getAddress().getPostcode());
    }

    public void builtInAvatarClick() throws IOException {
        Stage stage = new Stage();

        BuildInAvatarController.OnAvatarSubmit onAvatarSubmit = new BuildInAvatarController.OnAvatarSubmit() {
            @Override
            public void onSubmit(String avatarPath) {
                user.setAvatarPath(avatarPath);
                stage.close();
                Image img = new Image(user.getAvatarPath());
                avatar.setImage(img);
            }
        };

        BuildInAvatarController buildInAvatarController = new BuildInAvatarController();
        buildInAvatarController.setOnAvatarSubmit(onAvatarSubmit);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../views/buildInAvatar.fxml"));
        fxmlLoader.setController(buildInAvatarController);

        Parent root1 = (Parent) fxmlLoader.load();
        stage.setTitle("Build In Avatar");
        stage.setScene(new Scene(root1));
        stage.show();
    }
}
