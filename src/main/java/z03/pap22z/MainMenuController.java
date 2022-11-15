package z03.pap22z;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;

public class MainMenuController extends z03.pap22z.SceneController {
    @FXML
    protected void handleStartButtonPress(ActionEvent event) {
        System.out.println("Start button pressed.");
    }

    @FXML
    protected void handleSettingsButtonPress(ActionEvent event) {
        try {
            switchToScene(event, "Settings");
//            switchToSettings(event);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void handleScoreboardButtonPress(ActionEvent event) {
        System.out.println("Scoreboard button pressed.");
    }

    @FXML
    protected void handleExitButtonPress(ActionEvent event) {
        System.out.println("Exit button pressed.");
        Platform.exit();
    }
}
