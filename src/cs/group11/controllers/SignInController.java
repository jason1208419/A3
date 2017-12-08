package cs.group11.controllers;

import cs.group11.Main;
import cs.group11.MegaDB;
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


    @FXML
    protected void initialize() {

        users = MegaDB.getUsers();

        /*
        String imagePath = "https://media-cdn.tripadvisor.com/media/photo-s/0d/90/b1/d5/las-vegas-welcome-sign.jpg";
        Address address1 = new Address(new String[]{"313 Presli", "Singleton Park", "Swansea"}, "SA1 4PU");
        User user1 = new User("Admin", "Kieran", "Phillips", "075828471938", address1, imagePath);

        users.add(user1);

        Address address2 = new Address(new String[]{"43 Kings Road", "Singleton Park", "Cardiff"}, "CF33 6GH");
        User user2 = new User("Kings head", "Oliver", "Bourne", "032502353325", address2, imagePath);

        users.add(user2);

        Address address3 = new Address(new String[]{"313 Presli", "fafasfafds", "Swadsfsddsnsea"}, "SA1 4PU");
        User user3 = new User("User", "Kieransdfsfsd", "Phillipssdfsf", "07582847193843664", address1, imagePath);

        users.add(user3);
        */

        EventHandler<ActionEvent> onShutDown = event -> System.exit(0);

        shutDownBtn.setOnAction(onShutDown);

        EventHandler<ActionEvent> onLoginClick = new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                String username = usernameTXT.getText();

                boolean userFound = false;

                int index = -1;
                for (int i = 0; i < users.size(); i++) {
                    System.out.println(i);
                    if (Objects.equals(username, users.get(i).getUsername())) {
                        userFound = true;
                        index = i;
                    }
                }

                if (userFound) {
                    System.out.println("Welcome user");
                    Main.setCurrentUser(users.get(index));
                    loginSuccess();
                } else {
                    System.out.println("User not found");
                }
            }


            private void loginSuccess() {
                try {

                    AuctionListController controller = new AuctionListController();


                    FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/auctionList.fxml"));
                    loader.setController(controller);
                    Parent root = loader.load();
                    Scene mainScreen = new Scene(root, 600, 500);
                    Stage primaryStage = Main.getPrimaryStage();
                    primaryStage.setScene(mainScreen);

                } catch (IOException e) {
                    System.out.println("Failed to load fxml file");
                    e.printStackTrace();
                }

            }
        };
        signInBtn.setOnAction(onLoginClick);


    }
}