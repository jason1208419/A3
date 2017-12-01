package cs.group11.controllers;

import java.io.IOException;

import cs.group11.models.Address;
import cs.group11.models.Auction;
import cs.group11.models.Bid;
import cs.group11.models.User;
import cs.group11.models.artworks.Painting;
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
    private TableView<Auction> removeFavouriteAuctions;
    @FXML
    private TableView<User> removeFavouriteUsers;
    @FXML
    private TableColumn tablePic;
    @FXML
    private TableColumn tableName;
    @FXML
    private TableColumn tablePrice;
    @FXML
    private TableColumn tableRemoveArt;
    @FXML
    private TableColumn tableAvatar;
    @FXML
    private TableColumn tableUsername;
    @FXML
    private TableColumn tableFirstName;
    @FXML
    private TableColumn tableLastName;
    @FXML
    private TableColumn tableRemoveUser;

    private User user;

    private ObservableList<User> favouriteUsersList;
    private ObservableList<Auction> favouriteAuctionsList;

    @FXML
    protected void initialize() {
        Image avatarImage = new Image(user.getAvatarPath());
        this.avatar.setImage(avatarImage);
        this.avatar1.setImage(avatarImage);
        this.username1.setText(user.getUsername());
        this.username2.setText(user.getUsername());

        final ObservableList<User> data = FXCollections.observableArrayList(
                new User("admin", "Nasir", "Al Jabbouri", "07481173742", new Address(new String[]{"29 Flintstones Avenue", "Ding Dong Street", "UK"}, "PDT 0KL"), "res/avatars/creeper.jpg")
        );

        tableAvatar.setCellValueFactory(
                new PropertyValueFactory<User, String>("avatarPath")
        );
        tableAvatar.setCellFactory(new Callback<TableColumn<, User>, TableCell<User, String>>() {
            @Override
            public TableCell<User, String> call(TableColumn<User, String> param) {
                TableCell<User, String> cell = new TableCell<User, String>() {
                    @Override
                    public void updateItem(User user, boolean empty) {
                        super.updateItem(user, empty);

                        if (empty) {
                            setGraphic(null);
                        } else {
                            ImageView node = new ImageView();

                            Image image = new Image(user.getAvatarPath());
                            node.setImage(image);
                            setGraphic(node);
                        }
                    }
                };
                System.out.println(cell.getIndex());
                return cell;
            }
        });

        tableUsername.setCellValueFactory(
                new PropertyValueFactory<User, String>("username")
        );
        tableFirstName.setCellValueFactory(
                new PropertyValueFactory<User, String>("firstname")
        );
        tableLastName.setCellValueFactory(
                new PropertyValueFactory<User, String>("lastname")
        );
        removeFavouriteUsers.setItems(data);
//        removeFavouriteUsers.getColumns().addAll(tableAvatar,tableUsername,tableFirstName,tableLastName);

//        favouriteUsersList.addAll(user.getFavouriteUsers());
//        favouriteAuctionsList.addAll(user.getFavouriteAuctions());
    }

    //TODO: make table


    public void setUser(User user) {
        this.user = user;
    }
/*
    public class ImageListCell extends TableCell {

        private ImageView element = new ImageView();

        @Override
        protected void updateItem(Object object, boolean empty) {
            super.updateItem(object, empty);

            if (empty) {
                setGraphic(null);
            } else {
                User u = (User) object;

                Image image = new Image(u.getAvatarPath());
                element.setImage(image);
                setGraphic(element);
            }
        }
    }*/
}
