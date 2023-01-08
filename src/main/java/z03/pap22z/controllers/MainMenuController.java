package z03.pap22z.controllers;

import java.io.IOException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;


public class MainMenuController extends z03.pap22z.controllers.SceneController {
    @FXML
    protected void handleStartButtonPress(ActionEvent event) {
        try {
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
