package z03.pap22z.controllers;

import z03.pap22z.MusicManager;

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
        MusicManager.playButtonSound();
        text_hint.setText("Game to be implemented!");
        text_hint.setVisible(true);
    }

    @FXML
    protected void handleHint(ActionEvent event) {
        MusicManager.playButtonSound();
        if (event.getSource() == hint_1) {
            text_hint.setText(
                    "AimSniper: hit the target as many times as you can while it teleports on every successful click you make!");
        }
        if (event.getSource() == hint_2) {
            text_hint.setText("KeyboardWarrior: press the buttons on the keyboard in the correct order as soon as they appear on the line!");
        }
        if (event.getSource() == hint_3) {
            text_hint.setText("Sharpshooter: train your reflex by hitting the target as fast as you can, at a random moment and time. You only have 5 tries!");
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