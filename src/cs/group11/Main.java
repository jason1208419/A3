package cs.group11;

import cs.group11.controllers.AuctionListController;
import cs.group11.controllers.EditProfileController;
import cs.group11.controllers.SignInController;
import cs.group11.controllers.SignUpController;
import cs.group11.interfaces.OnAction;
import cs.group11.interfaces.OnSubmitClick;
import cs.group11.models.Address;
import cs.group11.models.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    private static Stage primaryStage;

    private FXMLLoader signUpLoader;
    private FXMLLoader signInLoader;
    private FXMLLoader auctionListLoader;

    public static void main(String[] args) throws IOException {
        MegaDB.load();

        Address address = new Address(new String[]{"29 Flintstones Avenue", "Ding Dong Street", "UK"}, "PDT 0KL");
        User creator = new User("admin", "Nasir", "Al Jabbouri", "07481173742", address, "res/avatars/creeper.jpg");

        launch(args);
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    private void setPrimaryStage(Stage primStage) {
        primaryStage = primStage;
    }

    private void setupSigninPage() {
        SignInController signInController = new SignInController();

        signInController.setOnSubmitClick((Object o) -> {
            User user = (User) o;

            try {
                Parent root = auctionListLoader.load();
                Scene mainScreen = new Scene(root, 600, 500);
                primaryStage.setScene(mainScreen);
            } catch (IOException e) {
                System.out.println("Failed to load fxml file");
                e.printStackTrace();
            }
        });

        signInController.setOnSignupClick((Object o) -> {
            try {
                Parent root = signUpLoader.load();
                Scene mainScreen = new Scene(root, 600, 500);
                primaryStage.setScene(mainScreen);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        signInLoader = new FXMLLoader(getClass().getResource("views/signIn.fxml"));
        signInLoader.setController(signInController);
    }

    private void setupSignupPage() {
        SignUpController signUpController = new SignUpController();
        signUpLoader = new FXMLLoader(getClass().getResource("views/signUp.fxml"));
        signUpLoader.setController(signUpController);
    }

    private void setupAuctionListPage() {
        AuctionListController auctionListController = new AuctionListController();
        auctionListLoader = new FXMLLoader(getClass().getResource("views/auctionList.fxml"));
        auctionListLoader.setController(auctionListController);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        setupAuctionListPage();
        setupSigninPage();
        setupSignupPage();

        Parent root = signInLoader.load();
        setPrimaryStage(primaryStage);

        primaryStage.setTitle("Artatawe");
        primaryStage.setScene(new Scene(root, 750, 650));
        primaryStage.show();
    }
}
