package z03.pap22z.controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import z03.pap22z.MusicManager;

public class SceneController {
    protected Stage stage;
    protected final static String FXML_PATH = "/z03/pap22z/";

    @FXML
    protected void playButtonSound() {
        MusicManager.playButtonSound();
    }

    @FXML
    protected void switchToScene(ActionEvent event, String scene_name) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(FXML_PATH + scene_name + ".fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene oldScene = stage.getScene();
        stage.setScene(oldScene == null
                ? new Scene(root, stage.getMinWidth(), stage.getMinHeight())
                : new Scene(root, oldScene.getWidth(), oldScene.getHeight()));
        MusicManager.playButtonSound();
        stage.show();
    }

    @FXML
    protected void switchToSettings(ActionEvent event) throws IOException {
        switchToScene(event, "Settings");
    }

    @FXML
    protected void switchToMenu(ActionEvent event) throws IOException {
        switchToScene(event, "MainMenu");
    }

    @FXML
    protected void switchToGameMenu(ActionEvent event) throws IOException {
        switchToScene(event, "GameMenu");
    }

    @FXML
    protected void switchToScoreboard(ActionEvent event) throws IOException {
        switchToScene(event, "Scoreboard");
    }
}
