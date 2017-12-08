package cs.group11.controllers;

import cs.group11.interfaces.OnViewSubmit;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class BuildInAvatarController {
    private final String AVATAR0_PATH = "http://pixabay.com/static/img/no_hotlinking.png";
    private final String AVATAR1_PATH = "res/avatars/creeper.jpg";
    private final String AVATAR2_PATH = "res/avatars/creeper.jpg";
    private final String AVATAR3_PATH = "res/avatars/creeper.jpg";
    private final String AVATAR4_PATH = "res/avatars/creeper.jpg";
    private final String AVATAR5_PATH = "res/avatars/creeper.jpg";
    @FXML
    private ImageView avatar0;
    @FXML
    private ImageView avatar1;
    @FXML
    private ImageView avatar2;
    @FXML
    private ImageView avatar3;
    @FXML
    private ImageView avatar4;
    @FXML
    private ImageView avatar5;
    private OnViewSubmit onViewSubmit;

    @FXML
    protected void initialize() {

        Image img0 = new Image(AVATAR0_PATH);
        Image img1 = new Image(AVATAR1_PATH);
        Image img2 = new Image(AVATAR2_PATH);
        Image img3 = new Image(AVATAR3_PATH);
        Image img4 = new Image(AVATAR4_PATH);
        Image img5 = new Image(AVATAR5_PATH);

        this.avatar0.setImage(img0);
        this.avatar1.setImage(img1);
        this.avatar2.setImage(img2);
        this.avatar3.setImage(img3);
        this.avatar4.setImage(img4);
        this.avatar5.setImage(img5);

        avatar0.setOnMouseClicked((MouseEvent e) -> {
            System.out.println("0 Clicked!");
            onViewSubmit.submit(AVATAR0_PATH);
            //onAvatarSubmit.onSubmit(AVATAR0_PATH);
        });

        avatar1.setOnMouseClicked((MouseEvent e) -> {
            System.out.println("1 Clicked!");
            onViewSubmit.submit(AVATAR1_PATH);
        });

        avatar2.setOnMouseClicked((MouseEvent e) -> {
            System.out.println("2 Clicked!");
            onViewSubmit.submit(AVATAR2_PATH);
        });

        avatar3.setOnMouseClicked((MouseEvent e) -> {
            System.out.println("3 Clicked!");
            onViewSubmit.submit(AVATAR3_PATH);
        });

        avatar4.setOnMouseClicked((MouseEvent e) -> {
            System.out.println("4 Clicked!");
            onViewSubmit.submit(AVATAR4_PATH);
        });

        avatar5.setOnMouseClicked((MouseEvent e) -> {
            System.out.println("5 Clicked!");
            onViewSubmit.submit(AVATAR5_PATH);
        });
    }

    public void setOnAvatarSubmit(OnViewSubmit onViewSubmit) {
        this.onViewSubmit = onViewSubmit;
    }

    /*public interface OnAvatarSubmit {
        void onSubmit(String avatarPath);
    }*/
}
