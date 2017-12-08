package cs.group11.controllers;

import cs.group11.helpers.Validator;
import cs.group11.interfaces.OnCancelClick;
import cs.group11.interfaces.OnSubmitClick;
import cs.group11.models.Address;
import cs.group11.models.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SignUpController {

    @FXML private ImageView avatarImg;

    @FXML private Button uploadButton;
    @FXML private Button drawAvatarButton;
    @FXML private Button selectAvatarButton;

    @FXML private TextField usernameInput;
    @FXML private TextField firstnameInput;
    @FXML private TextField lastnameInput;
    @FXML private TextField phoneNoInput;
    @FXML private TextField addressInput;
    @FXML private TextField postcodeInput;

    @FXML private Button submitButton;
    @FXML private Button cancelButton;

    private OnCancelClick onCancelClick;
    private OnSubmitClick onSubmitClick;

    private String avatarPath = null;

    private static final FileChooser.ExtensionFilter IMAGE_FILE_EXTENTIONS = new FileChooser.ExtensionFilter("Image Files", ".png", ".gif", ".jpeg", ".jpg");

    @FXML
    public void initialize() {

    }

    public void setOnCancelClick(OnCancelClick onCancelClick) {
        this.onCancelClick = onCancelClick;
    }

    public void setOnSubmitClick(OnSubmitClick onSubmitClick) {
        this.onSubmitClick = onSubmitClick;
    }

    public void uploadClick() {
        avatarPath = userSelectImage();

        try {
            Image image = new Image(avatarPath);
            this.avatarImg.setImage(image);
        } catch (NullPointerException e) {
            System.out.println("Path not specified");
        }
    }

    public void drawAvatarClick() {
        System.out.println("draw avatar click");
    }

    public void cancelClick() {
        onCancelClick.cancel(null);
    }

    public void submitClick() {
        String username = usernameInput.getText();
        String firstname = firstnameInput.getText();
        String lastname = lastnameInput.getText();
        String phoneNo = phoneNoInput.getText();
        String postcode = postcodeInput.getText();

        List<String> addressLines = new ArrayList<>();
        Collections.addAll(addressLines, addressInput.getText().split("\\n"));
        String[] lines = new String[addressLines.size()];
        lines = addressLines.toArray(lines);

        Address address = new Address(lines, postcode);
        User user = new User(username, firstname, lastname, phoneNo, address, avatarPath);

        onSubmitClick.submit(user);
    }

    /**
     * Pop up a window to let the user choose one of the build in avatar as their own avatar
     * @throws IOException Error trace for unsuccessful call of profile fxml or controller
     */
    public void builtInAvatarClick() throws IOException {
        Stage stage = new Stage();

        OnSubmitClick onAvatarSubmit = avatarPath -> {
            this.avatarPath = (String) avatarPath;

            stage.close();
            Image image = new Image(this.avatarPath);
            this.avatarImg.setImage(image);
        };

        BuildInAvatarController buildInAvatarController = new BuildInAvatarController();
        buildInAvatarController.setOnAvatarSubmit(onAvatarSubmit);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../views/buildInAvatar.fxml"));
        fxmlLoader.setController(buildInAvatarController);

        Parent root1 = fxmlLoader.load();
        stage.setTitle("Build In Avatar");
        stage.setScene(new Scene(root1));
        stage.show();
    }

    private String userSelectImage() {
        FileChooser chooser = new FileChooser();
        chooser.setSelectedExtensionFilter(IMAGE_FILE_EXTENTIONS);
        chooser.setTitle("Select Image");
        File in = chooser.showOpenDialog(null);

        //return path if a file chosen
        if (Validator.isFileValid(in)) {
            return in.toURI().toString();
        }
        return null;
    }
}
