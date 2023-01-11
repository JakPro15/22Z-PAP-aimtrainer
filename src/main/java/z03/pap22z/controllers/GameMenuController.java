package z03.pap22z.controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import z03.pap22z.MusicManager;

public class GameMenuController extends z03.pap22z.controllers.BaseSceneController {
    @FXML
    protected void handleExit(ActionEvent event) {
        try {
            switchToMenu(event);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private TextArea hintText;

    @FXML
    private Button hint1;

    @FXML
    private Button hint2;

    @FXML
    private Button hint3;

    @FXML
    protected void handleHint(ActionEvent event) {
        MusicManager.playButtonSound();
        if (event.getSource() == hint1) {
            hintText.setText(
                    "AimSniper: hit the target as many times as you can while it teleports on every successful click you make!");
        }
        if (event.getSource() == hint2) {
            hintText.setText(
                    "KeyboardWarrior: press the buttons on the keyboard in the correct order as soon as they appear on the line!");
        }
        if (event.getSource() == hint3) {
            hintText.setText(
                    "Sharpshooter: train your reflex by hitting the target as fast as you can, at a random moment and time. You only have 5 tries!");
        }
        hintText.setVisible(true);
    }

    @FXML
    protected void switchToAimSniper(ActionEvent event) {
        try {
            switchToScene(event, "AimSniper");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void switchToKeyboardWarrior(ActionEvent event) {
        try {
            switchToScene(event, "KeyboardWarrior");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void switchToSharpshooter(ActionEvent event) {
        try {
            switchToScene(event, "Sharpshooter");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}