package z03.pap22z.controllers;

import java.io.IOException;
import java.io.File;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.net.URISyntaxException;
import java.net.URL;


public class MainMenuController extends z03.pap22z.controllers.SceneController {
    @FXML
    protected void handleStartButtonPress(ActionEvent event) {
        try {
            String soundFile = getClass().getResource("EventChoice.mp3").toExternalForm();
            Media media = new Media(soundFile);
            MediaPlayer player = new MediaPlayer(media);
            player.play();

            switchToScene(event, "GameMenu");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void handleSettingsButtonPress(ActionEvent event) {
        try {
            switchToSettings(event);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void handleScoreboardButtonPress(ActionEvent event) {
        try {
            switchToScoreboard(event);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void handleExitButtonPress(ActionEvent event) {
        Platform.exit();
    }
}
