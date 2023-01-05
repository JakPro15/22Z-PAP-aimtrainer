package z03.pap22z.controllers;

import java.io.IOException;
import java.time.LocalDateTime;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import z03.pap22z.KeyboardWarriorLogic;
import z03.pap22z.Settings;
import z03.pap22z.database.Database;
import z03.pap22z.database.SavedResults;

public class KeyboardWarriorController extends z03.pap22z.controllers.SceneController {
    private final int delay_time = 3;

    private KeyboardWarriorLogic logic = new KeyboardWarriorLogic();

    private int timeLeft = Settings.getGameLength();

    private LocalDateTime gameEndTime;

    private boolean isScoreSaved = false;

    @FXML
    private Rectangle finishLine;
    
    @FXML
    private AnchorPane playfield;

    @FXML
    private Label timeLeftTextLabel;

    @FXML
    private Button exitButton;

    @FXML
    private Label scoreValueLabel;

    @FXML
    private Label accuracyValueLabel;

    @FXML
    private Label timeLeftValueLabel;

    @FXML
    private Label accuracyTextLabel;

    @FXML
    private Button saveButton;

    @FXML
    private Label messageLabel;

    @FXML
    private Label scoreTextLabel;

    @FXML
    private Button newGameButton;

    /**
     * Initializes all needed properties and starts the game.
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
    public void playGame() {
        finishLine.setVisible(false);
        messageLabel.setText(String.format("%d", delay_time));

        // ready period before a game
        Timeline countdownTimeline = new Timeline(new KeyFrame(Duration.seconds(1), event1 -> {
            int countdownTime = Integer.parseInt(messageLabel.getText()) - 1;
            if (countdownTime > 0) {
                messageLabel.setText(String.format("%d", countdownTime));
            }
            else {
                messageLabel.setText("");
                finishLine.setVisible(true);
                logic.switchGameState();
                gameEndTime = LocalDateTime.now().plusSeconds(timeLeft);
                timeLeftValueLabel.setText(String.format("%d seconds", timeLeft));
                Timeline gameTimeline = new Timeline(new KeyFrame(Duration.seconds(1), event2 -> {
                    timeLeft -= 1;
                    if (timeLeft > 0) {
                    timeLeftValueLabel.setText(String.format("%d seconds", timeLeft));
                    }
                    else {
                        timeLeftValueLabel.setText(String.format("%d seconds", timeLeft));
                        logic.switchGameState();
                        finishLine.setVisible(false);
                        messageLabel.setText("GAME OVER");
                    }
                }));
                gameTimeline.setCycleCount(timeLeft);
                gameTimeline.play();
            }
        }));
        countdownTimeline.setCycleCount(delay_time);
        countdownTimeline.play();
    }

    @FXML
    void handleSave(ActionEvent event) {
        if (!logic.getIsGameOn() && gameEndTime != null && !isScoreSaved && Database.isConnected()) {
            SavedResults.writeResult(
                    logic.getPoints(), logic.getAccuracy(), gameEndTime, "KeyboardWarrior");
            saveButton.setText("Score saved.");
            isScoreSaved = true;
        }
    }

    @FXML
    void handleNewGame(ActionEvent event) {
        try {
            switchToScene(event, "KeyboardWarrior");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void handleExit(ActionEvent event) {
        try {
            switchToGameMenu(event);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
