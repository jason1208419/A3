package cs.group11.controllers;

import java.io.IOException;

import cs.group11.models.Address;
import cs.group11.models.Auction;
import cs.group11.models.User;
import cs.group11.models.artworks.Painting;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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

    private User user;

    private ObservableList<User> favouriteUsersList;
    private ObservableList<Auction> favouriteAuctionsList;

    @FXML
    protected void initialize() {
        favouriteUsersList = FXCollections.observableArrayList();
        favouriteAuctionsList = FXCollections.observableArrayList();

        removeFavouriteUsers.setItems(favouriteUsersList);
        removeFavouriteAuctions.setItems(favouriteAuctionsList);

        //removeFavouriteUsers.setCellFactory(param -> null); // FIXME
        //removeFavouriteAuctions.setCellFactory(param -> null); // FIXME
    }

    public void setUser(User user) {
        this.user = user;
        this.avatar.setImage(user.getAvatar());
        this.avatar1.setImage(user.getAvatar());
        this.username1.setText(user.getUsername());
        this.username2.setText(user.getUsername());

        favouriteUsersList.addAll(user.getFavouriteUsers());
        favouriteAuctionsList.addAll(user.getFavouriteAuctions());
    }

    public void createAuctionsClick(ActionEvent actionEvent) throws Exception {
        ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("views/createAuction.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Create Auction");
        stage.show();
    }

    public void browseAuctionsClick(ActionEvent actionEvent) throws Exception {
        ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("views/auctionList.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Auction");
        stage.show();
    }

    public void logoutClick(ActionEvent actionEvent) throws Exception {
        ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("views/signIn.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Login");
        stage.show();
    }

    public void profileClick(ActionEvent actionEvent) throws Exception {
        ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("views/profile.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Profile");
        stage.show();
    }

    public void uploadClick(ActionEvent actionEvent) throws Exception {
    }

    public void drawAvatarClick(ActionEvent actionEvent) throws Exception {
    }

    public void submitClick(ActionEvent actionEvent) throws Exception {
    }

    public void cancelClick(ActionEvent actionEvent) throws Exception {
        ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("views/profile.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Profile");
        stage.show();
    }
}
