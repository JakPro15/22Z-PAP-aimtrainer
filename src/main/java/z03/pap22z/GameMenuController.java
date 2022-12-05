package z03.pap22z;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class GameMenuController extends z03.pap22z.SceneController {
    @FXML
    private ListView<String> game_list;

    @FXML
    protected void handleExit(ActionEvent event) {
        try {
            switchToMenu(event);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}