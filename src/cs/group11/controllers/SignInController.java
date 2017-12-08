package cs.group11.controllers;

import cs.group11.Main;
import cs.group11.MegaDB;
import cs.group11.interfaces.OnAction;
import cs.group11.interfaces.OnCancelClick;
import cs.group11.interfaces.OnSubmitClick;
import cs.group11.models.Address;
import cs.group11.models.User;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SignInController {

    @FXML
    private Button shutDownBtn;
    @FXML
    private Button signInBtn;
    @FXML
    private TextField usernameTXT;

    private List<User> users;

    private OnSubmitClick onSubmitClick;
    private OnAction onSignupClick;

    @FXML
    protected void initialize() {
        users = MegaDB.getUsers();
    }

    public void signupClick() {
        onSignupClick.call(null);
    }

    public void loginSubmit() {
        String username = usernameTXT.getText();
        User user = MegaDB.login(username);

        if (user != null) {
            onSubmitClick.submit(user);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("User login error");
            alert.setHeaderText("User could not be found.");
            alert.setContentText("Please make sure you typed in your username correctly and that you have " +
                    "registered for an account.");

            alert.showAndWait();
        }
    }

    public void shutdown() {
        System.exit(0);
    }

    public void setOnSubmitClick(OnSubmitClick onSubmitClick) {
        this.onSubmitClick = onSubmitClick;
    }

    public void setOnSignupClick(OnAction onSignupClick) {
        this.onSignupClick = onSignupClick;
    }
}