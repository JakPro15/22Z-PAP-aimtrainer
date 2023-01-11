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

public class BaseSceneController {
    protected static Stage stage;
    protected final static String FXML_PATH = "/z03/pap22z/";

    /**
     * Helper method for use in FXML files as a handler in situations when the
     * only action that an event triggers is playing the default button sound.
     */
    @FXML
    protected void playButtonSound() {
        MusicManager.playButtonSound();
    }

    /**
     * Switches to a new scene. This method searches for a file in the
     * src/main/resources/z03/pap22z/ directory called {scene_name}.fxml.
     * 
     * @param event event which triggered the switch
     * @param sceneName name of the new scene.
     * @throws IOException
     */
    @FXML
    protected void switchToScene(ActionEvent event, String sceneName) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(FXML_PATH + sceneName + ".fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene oldScene = stage.getScene();
        stage.setScene(oldScene == null
                ? new Scene(root, stage.getMinWidth(), stage.getMinHeight())
                : new Scene(root, oldScene.getWidth(), oldScene.getHeight()));
        MusicManager.playButtonSound();
        stage.show();
    }

    /**
     * Helper switch method for use in FXML files.
     * 
     * @param event
     * @throws IOException
     */
    @FXML
    protected void switchToSettings(ActionEvent event) throws IOException {
        switchToScene(event, "Settings");
    }

    /**
     * Helper switch method for use in FXML files.
     * 
     * @param event
     * @throws IOException
     */
    @FXML
    protected void switchToMenu(ActionEvent event) throws IOException {
        switchToScene(event, "MainMenu");
    }

    /**
     * Helper switch method for use in FXML files.
     * 
     * @param event
     * @throws IOException
     */
    @FXML
    protected void switchToGameMenu(ActionEvent event) throws IOException {
        switchToScene(event, "GameMenu");
    }

    /**
     * Helper switch method for use in FXML files.
     * 
     * @param event
     * @throws IOException
     */
    @FXML
    protected void switchToScoreboard(ActionEvent event) throws IOException {
        switchToScene(event, "Scoreboard");
    }
}
