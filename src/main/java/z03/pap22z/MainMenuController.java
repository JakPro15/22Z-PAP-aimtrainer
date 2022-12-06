package z03.pap22z;

import java.io.IOException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class MainMenuController extends z03.pap22z.SceneController {
    @FXML
    protected void handleStartButtonPress(ActionEvent event) {
        try {
            switchToScene(event, "GameMenu");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Start button pressed.");
    }

    @FXML
    protected void handleSettingsButtonPress(ActionEvent event) {
        try {
            switchToSettings(event);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Settings button pressed.");
    }

    @FXML
    protected void handleScoreboardButtonPress(ActionEvent event) {
        try {
            switchToScoreboard(event);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Scoreboard button pressed.");
    }

    @FXML
    protected void handleExitButtonPress(ActionEvent event) {
        System.out.println("Exit button pressed.");
        Platform.exit();
    }
}
