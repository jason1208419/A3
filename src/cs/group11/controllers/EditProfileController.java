package cs.group11.controllers;

import java.io.IOException;

import cs.group11.models.Address;
import cs.group11.models.Artwork;
import cs.group11.models.User;
import cs.group11.models.artworks.Painting;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
    private TableView<Artwork> removeFavouriteArtworks;
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
    private TableColumn<Artwork, Artwork> tableRemoveArt = new TableColumn<>("Remove");

    private User user;

    private ObservableList<User> favouriteUsersList;
    private ObservableList<Artwork> favouriteArtworkList;

    @FXML
    protected void initialize() {
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

    //TODO: make table


    public void setUser(User user) {
        this.user = user;
    }

    private void setupFavouriteArtTable() {
        favouriteArtworkList = FXCollections.observableArrayList(this.user.getFavouriteArtworks());

        tablePic.setCellValueFactory(new PropertyValueFactory<Artwork, Image>("image"));
        tablePic.setPrefWidth(100);
        tablePic.setCellFactory(new Callback<TableColumn<Artwork, Image>, TableCell<Artwork, Image>>() {
            @Override
            public TableCell<Artwork, Image> call(TableColumn<Artwork, Image> param) {
                TableCell<Artwork, Image> cell = new TableCell<Artwork, Image>() {
                    @Override
                    public void updateItem(Image image, boolean empty) {
                        super.updateItem(image, empty);

                        if (empty) {
                            setGraphic(null);
                        } else {
                            ImageView node = new ImageView();

                            node.setImage(image);
                            node.setFitWidth(100);
                            node.setPreserveRatio(true);
                            setGraphic(node);
                        }
                    }
                };
                System.out.println(cell.getIndex());
                return cell;
            }
        });

        tableName.setCellValueFactory(new PropertyValueFactory<Artwork, String>("name"));
        tableArtist.setCellValueFactory(new PropertyValueFactory<Artwork, Double>("artist"));
        tableCreationYear.setCellValueFactory(new PropertyValueFactory<Artwork, Integer>("creationYear"));
        tableRemoveArt.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue())
        );
        tableRemoveArt.setCellFactory(param -> new TableCell<Artwork, Artwork>() {
            private final Button remove = new Button("Remove");

            @Override
            protected void updateItem(Artwork art, boolean empty) {
                super.updateItem(art, empty);
                if (art == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(remove);
                remove.setOnAction(
                        event -> {
                            getTableView().getItems().remove(art);
                            user.removeFavouriteArtwork(art);
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
                System.out.println(cell.getIndex());
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
                            getTableView().getItems().remove(user1);
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
        this.user.addFavouriteArtwork(painting);
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
        for (int i = 0; i < user.getFavouriteArtworks().size(); i++) {
            System.out.println(user.getFavouriteArtworks().get(i).getName());
        }
    }
}
