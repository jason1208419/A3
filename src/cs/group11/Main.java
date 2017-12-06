package cs.group11;

import cs.group11.controllers.EditProfileController;
import cs.group11.models.Address;
import cs.group11.models.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
    private static Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    private void setPrimaryStage(Stage primStage) {
        primaryStage = primStage;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Address address = new Address(new String[]{"29 Flintstones Avenue", "Ding Dong Street", "UK"}, "PDT 0KL");
        User creator = new User("admin", "Nasir", "Al Jabbouri", "07481173742", address, "res/avatars/creeper.jpg");


//        ProfileController controller = new ProfileController();
//        controller.setUser(creator);
//
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("views/profile.fxml"));
//        loader.setController(controller);
//        Parent root = loader.load();

//        primaryStage.setTitle("Hello World");
//        primaryStage.setScene(new Scene(root, 600, 550));
//        primaryStage.show();
        EditProfileController controller = new EditProfileController();
        controller.setUser(creator);
        controller.setTestArt();
        controller.setTestUsers();


        FXMLLoader loader = new FXMLLoader(getClass().getResource("views/editProfile.fxml"));
        loader.setController(controller);
        Parent root = loader.load();

        setPrimaryStage(primaryStage);
        primaryStage.setTitle("Artatawe");
        primaryStage.setScene(new Scene(root, 750, 650));
        primaryStage.show();
    }
}
