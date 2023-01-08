package z03.pap22z.controllers;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Random;

import javafx.animation.Timeline;
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
     * Initializes classes' static members. Should be used instead of the
     * static block.
     */
    protected abstract void initializeStatics();

    /**
     * Main initialization block.
     */
    protected void initializeMainBlock() {

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
    }

    /**
     * Initializes all needed properties and starts the game. The base
     * initialize method calls three methods which are, in order:
     * initializeStatics(), initializeMainBlock() and playGame(). Any changes
     * to controller initialization should be done through the
     * initializeStatics() and initializeMainBlock() methods to ensure correct
     * order of initialization.
     */
    public void initialize() {

        initializeStatics();
        initializeMainBlock();
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

    protected abstract void terminateTimelines();

    protected static void terminateTimeline(Timeline timeline) {
        if (timeline != null) {
            timeline.stop();
        }
    }

    @FXML
    protected void handleNewGame(ActionEvent event) {
        terminateTimelines();
        try {
            switchToScene(event, GAME_NAME);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void handleExit(ActionEvent event) {
        terminateTimelines();
        try {
            switchToGameMenu(event);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}