package z03.pap22z.controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class GameMenuController extends z03.pap22z.controllers.SceneController {
    @FXML
    protected void handleExit(ActionEvent event) {
        try {
            switchToMenu(event);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private TextArea text_hint;

    @FXML
    private Button hint_1;

    @FXML
    private Button hint_2;

    @FXML
    private Button hint_3;

    @FXML
    protected void handleToBeImplemented(ActionEvent event) {
        text_hint.setText("Game to be implemented!");
        text_hint.setVisible(true);
    }

    @FXML
    protected void handleHint(ActionEvent event) {
        if (event.getSource() == hint_1) {
            text_hint.setText("How to play game number 1");
        }
        if (event.getSource() == hint_2) {
            text_hint.setText("How to play game number 2. I specially put here longer text to see what happens.");
        }
        if (event.getSource() == hint_3) {
            text_hint.setText("How to play game number 3. I like studying IT :) ");
        }
        text_hint.setVisible(true);
    }

    @FXML
    protected void switchToAimSniper(ActionEvent event) {
        try {
            switchToScene(event, "AimSniper");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}