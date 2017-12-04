package cs.group11.controllers;

import java.io.IOException;
import java.util.ArrayList;

import cs.group11.models.Address;
import cs.group11.models.Artwork;
import cs.group11.models.Auction;
import cs.group11.models.User;
import cs.group11.models.artworks.Painting;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
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
    @FXML
    private TableView<Auction> removeFavouriteArtworks;
    @FXML
    private TableView<User> removeFavouriteUsers;
    @FXML
    private TableColumn tablePic;
    @FXML
    private TableColumn tableName;
    @FXML
    private TableColumn tableArtist;
    @FXML
    private TableColumn tableCreationYear;
    @FXML
    private TableColumn tableAvatar;
    @FXML
    private TableColumn tableUsername;
    @FXML
    private TableColumn tableFirstName;
    @FXML
    private TableColumn tableLastName;
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

    private Stage stage;

    private User user;

    private ObservableList<User> favouriteUsersList;
    private ObservableList<Auction> favouriteArtworkList;

    @FXML
    protected void initialize() {
        Address address = new Address(new String[]{"29 Flintstones Avenue", "Ding Dong Street", "UK"}, "PDT 0KL");
        User creator = new User("admin", "Nasir", "Al Jabbouri", "07481173742", address, "res/avatars/creeper.jpg");
        setUser(creator);
        Image avatarImage = new Image(user.getAvatarPath());
        this.avatar.setImage(avatarImage);
        this.avatar1.setImage(avatarImage);
        this.username1.setText(user.getUsername());
        this.username2.setText(user.getUsername());

        setTestUsers();
        setTestArt();
        setupFavouriteArtTable();
        setupFavouriteUserTable();
    }

    public void setUser(User user) {
        this.user = user;
    }

    private void setupFavouriteArtTable() {
        favouriteArtworkList = FXCollections.observableArrayList(this.user.getFavouriteAuctions());

        tablePic.setCellValueFactory(new PropertyValueFactory<Auction, Artwork>("artwork"));
        tablePic.setPrefWidth(100);
        tablePic.setCellFactory(new Callback<TableColumn<Auction, Artwork>, TableCell<Auction, Artwork>>() {
            @Override
            public TableCell<Auction, Artwork> call(TableColumn<Auction, Artwork> param) {
                TableCell<Auction, Artwork> cell = new TableCell<Auction, Artwork>() {
                    @Override
                    public void updateItem(Artwork artwork, boolean empty) {
                        super.updateItem(artwork, empty);

                        if (empty) {
                            setGraphic(null);
                        } else {
                            ImageView node = new ImageView();
                            node.setImage(artwork.getImage());
                            node.setFitWidth(100);
                            node.setPreserveRatio(true);
                            setGraphic(node);
                        }
                    }
                };
                return cell;
            }
        });

        tableName.setCellValueFactory(new PropertyValueFactory<Auction, Artwork>("artwork"));
        tableName.setCellFactory(new Callback<TableColumn<Auction, Artwork>, TableCell<Auction, Artwork>>() {
            @Override
            public TableCell<Auction, Artwork> call(TableColumn<Auction, Artwork> param) {
                TableCell<Auction, Artwork> cell = new TableCell<Auction, Artwork>() {
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
                return cell;
            }
        });
        tableArtist.setCellValueFactory(new PropertyValueFactory<Auction, Artwork>("artwork"));
        tableArtist.setCellFactory(new Callback<TableColumn<Auction, Artwork>, TableCell<Auction, Artwork>>() {
            @Override
            public TableCell<Auction, Artwork> call(TableColumn<Auction, Artwork> param) {
                TableCell<Auction, Artwork> cell = new TableCell<Auction, Artwork>() {
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
                return cell;
            }
        });
        tableCreationYear.setCellValueFactory(new PropertyValueFactory<Auction, Artwork>("artwork"));
        tableCreationYear.setCellFactory(new Callback<TableColumn<Auction, Artwork>, TableCell<Auction, Artwork>>() {
            @Override
            public TableCell<Auction, Artwork> call(TableColumn<Auction, Artwork> param) {
                TableCell<Auction, Artwork> cell = new TableCell<Auction, Artwork>() {
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
                return cell;
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

        tableAvatar.setCellValueFactory(new PropertyValueFactory<User, String>("avatarPath"));
        tableAvatar.setPrefWidth(100);
        tableAvatar.setCellFactory(new Callback<TableColumn<User, String>, TableCell<User, String>>() {
            @Override
            public TableCell<User, String> call(TableColumn<User, String> param) {
                TableCell<User, String> cell = new TableCell<User, String>() {
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
                return cell;
            }
        });

        tableUsername.setCellValueFactory(new PropertyValueFactory<User, String>("username"));
        tableFirstName.setCellValueFactory(new PropertyValueFactory<User, String>("firstname"));
        tableLastName.setCellValueFactory(new PropertyValueFactory<User, String>("lastname"));
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

    private void setTestArt() {
        User creator = new User("ggg", "asas", "kijlkl", "07481173742", new Address(new String[]{"29 Flintstones Avenue", "Ding Dong Street", "UK"}, "PDT 0KL"), "http://pixabay.com/static/img/no_hotlinking.png");

        String description = "The Starry Night is an oil on canvas by the Dutch post-impressionist painter Vincent van Gogh. Painted in June 1889, " +
                "it depicts the view from the east-facing window of his asylum room at Saint-Rémy-de-Provence, just before sunrise, with the addition " +
                "of an idealized village";

        Image artworkImage = new Image("https://www.moma.org/wp/moma_learning/wp-content/uploads/2012/07/Van-Gogh.-Starry-Night-469x376.jpg");
        Painting painting = new Painting("Starry Night", description, artworkImage, "Vincent Van Gogh", 1889, 200, 300);
        Auction auction = new Auction(creator, 7, 10.00, painting);
        this.user.addFavouriteAuction(auction);
    }

    private void setTestUsers() {
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

    //TODO: A user logs in to the program and performs actions which are saved locally to disk on that machine

    @FXML
    private void cancelClick(ActionEvent actionEvent) throws IOException {
        ProfileController profileCon = new ProfileController();
        profileCon.setUser(this.user);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/profile.fxml"));
        loader.setController(profileCon);
        VBox box = loader.load();

        rootBox.getChildren().setAll(box);
    }

    public void avatarClick(ActionEvent actionEvent) throws IOException {
        VBox box = FXMLLoader.load(getClass().getResource("../views/profile.fxml"));
        rootBox.getChildren().setAll(box);
    }

    public void viewAuctionClick(ActionEvent actionEvent) throws IOException {
        VBox box = FXMLLoader.load(getClass().getResource("../views/auctionList.fxml"));
        rootBox.getChildren().setAll(box);
    }

    public void createAuctionClick(ActionEvent actionEvent) throws IOException {
        VBox box = FXMLLoader.load(getClass().getResource("../views/createAuction.fxml"));
        rootBox.getChildren().setAll(box);
    }

    public void logoutClick(ActionEvent actionEvent) throws IOException {
        VBox box = FXMLLoader.load(getClass().getResource("../views/signIn.fxml"));
        rootBox.getChildren().setAll(box);
    }

    public void uploadClick(ActionEvent actionEvent) {
    }

    public void drawAvatarClick(ActionEvent actionEvent) {
    }

    public void submitClick(ActionEvent actionEvent) throws IOException {
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
            for (String line : addressIn.getText().split("\\n")) {
                address.add(line);
            }
            String[] lines = new String[address.size()];
            lines = address.toArray(lines);
            this.user.getAddress().setLines(lines);
        }
        if (postcodeIn.getText() != null && !postcodeIn.getText().isEmpty()) {
            this.user.getAddress().setPostcode(postcodeIn.getText());
        }
        printUser();

        ProfileController profileCon = new ProfileController();
        profileCon.setUser(this.user);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/profile.fxml"));
        loader.setController(profileCon);
        VBox box = loader.load();

        rootBox.getChildren().setAll(box);
    }

    private void printUser() {
        System.out.println("Username: " + this.user.getUsername() + "\nFirst Name: " + this.user.getFirstname() + "\nLast Name: " + this.user.getLastname() + "\nPhone Number: " + this.user.getTelNo() + "\nAddress:");
        for (int i = 0; i < this.user.getAddress().getLines().length; i++) {
            System.out.println(this.user.getAddress().getLine(i + 1));
        }
        System.out.println(this.user.getAddress().getPostcode());
    }
}
