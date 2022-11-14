package z03.pap22z;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class MainMenuController {
    @FXML
    protected void handleStartButtonPress(ActionEvent event) {
        System.out.println("Start button pressed.");
    }

    @FXML
    protected void handleSettingsButtonPress(ActionEvent event) {
        System.out.println("Settings button pressed.");
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
