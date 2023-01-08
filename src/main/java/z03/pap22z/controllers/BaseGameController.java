package z03.pap22z.controllers;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Random;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import z03.pap22z.database.Database;
import z03.pap22z.database.SavedResults;
import z03.pap22z.logics.GameLogic;

public abstract class BaseGameController extends z03.pap22z.controllers.SceneController {
    protected static final int DELAY_TIME = 3;
    protected static String GAME_NAME;

    protected GameLogic logic;

    protected LocalDateTime gameEndTime;

    protected boolean isScoreSaved = false;

    protected static Random random = new Random();

    @FXML
    protected AnchorPane playfield;

    @FXML
    protected Label scoreValueLabel;

    @FXML
    protected Label accuracyValueLabel;

    @FXML
    protected Button saveButton;

    @FXML
    protected Label messageLabel;

    /**
     * Initializes all needed properties and starts the game. The parent class
     * initialize() method must be called at the end of the inherited classes'
     * method because the super class calls playGame() at the end of it's
     * routine.
     */
    public void initialize() {

        this.logic.pointsProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> source, Number oldValue, Number newValue) {
                scoreValueLabel.textProperty().setValue(String.format("%d", logic.getPoints()));
            }
        });
        this.logic.accuracyProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> source, Number oldValue, Number newValue) {
                accuracyValueLabel.textProperty().setValue(String.format("%.2f%%", logic.getAccuracy()));
            }

        });
        playGame();
    }

    /**
     * Main game method. Launches all events in correct order: countdown, game
     * itself, game over screen.
     */
    public abstract void playGame();

    @FXML
    protected void handleSave(ActionEvent event) {
        if (!logic.getIsGameOn() && gameEndTime != null && !isScoreSaved && Database.isConnected()) {
            SavedResults.writeResult(
                    logic.getPoints(), logic.getAccuracy(), gameEndTime, GAME_NAME);
            saveButton.setText("Score saved.");
            isScoreSaved = true;
        }
    }

    @FXML
    protected void handleNewGame(ActionEvent event) {
        try {
            switchToScene(event, GAME_NAME);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void handleExit(ActionEvent event) {
        try {
            switchToGameMenu(event);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}