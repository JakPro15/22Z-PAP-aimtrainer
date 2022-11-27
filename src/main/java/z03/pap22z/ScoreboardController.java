package z03.pap22z;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;

public class ScoreboardController extends z03.pap22z.SceneController {
    @FXML
    protected void handleExit(ActionEvent event) {
        try {
            switchToMenu(event);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Exit button pressed.");
    }
}
