package z03.pap22z.controllers;

import java.io.IOException;
import java.io.File;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.net.URISyntaxException;


public class MainMenuController extends z03.pap22z.controllers.SceneController {
    @FXML
    private MediaView mediaView;

    @FXML
    protected void handleStartButtonPress(ActionEvent event) {
        try {
            //if (mediaView.getMediaPlayer() == null) {
                //try {
                    //String filename = getClass().getResource("/audios/simpleButton.mp3").toURI().toString();
                    File filename = new File("/audios/simpleButton.mp3");
                    Media media = new Media(filename.toURI().toString());
                    MediaPlayer player = new MediaPlayer(media);
                    mediaView.setMediaPlayer(player);
                //}
                // catch (URISyntaxException ee) {
                //     ee.printStackTrace();
                // }
            //}
            //mediaView.getMediaPlayer().seek(mediaView.getMediaPlayer().getStartTime());
            mediaView.getMediaPlayer().play();
            //player.play();

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
