package cs.group11;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import cs.group11.controllers.AuctionListController;
import cs.group11.controllers.CreateAuctionController;
import cs.group11.controllers.EditProfileController;
import cs.group11.controllers.ProfileController;
import cs.group11.controllers.SignInController;
import cs.group11.controllers.SignUpController;
import cs.group11.controllers.ViewAuctionController;
import cs.group11.interfaces.OnAuctionClick;
import cs.group11.interfaces.OnHeaderAction;
import cs.group11.interfaces.OnSubmitClick;
import cs.group11.interfaces.OnUserClick;
import cs.group11.models.Address;
import cs.group11.models.Auction;
import cs.group11.models.Bid;
import cs.group11.models.User;
import cs.group11.models.artworks.Painting;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main {

	public static void main(String[] args) throws IOException {
		MegaDB.load();

		Application.launch(App.class, args);

		// TODO: Remove me before submission
        App.addTestData();

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			try {
				MegaDB.save();// Save on shutdown
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}));
	}


}
