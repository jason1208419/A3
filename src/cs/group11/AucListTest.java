package cs.group11;

import cs.group11.controllers.AuctionListController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

// A temporary class to test out functionality of AuctionListController, AuctionCellController and linking it with
// ViewAuctionController

public class AucListTest extends Application {
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("views/auctionList.fxml"));
        Parent root = loader.load();
        AuctionListController controller = loader.getController();
        setPrimaryStage(primaryStage);
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 600, 550));
        primaryStage.show();
    }
}
