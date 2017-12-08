package cs.group11;

import cs.group11.controllers.EditProfileController;
import cs.group11.controllers.SignInController;
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
    private static User currentUser;

    public static void main(String[] args) throws IOException {
        MegaDB.load();
        launch(args);
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    private void setPrimaryStage(Stage primStage) {
        primaryStage = primStage;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User curUser) {
        currentUser = curUser;
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("views/signIn.fxml"));
        Parent root = loader.load();

        setPrimaryStage(primaryStage);
        primaryStage.setTitle("Artatawe");
        primaryStage.setScene(new Scene(root, 750, 650));

        primaryStage.show();
    }
}
