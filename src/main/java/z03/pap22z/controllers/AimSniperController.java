package z03.pap22z.controllers;

import java.time.LocalDateTime;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.util.Duration;
import z03.pap22z.MusicManager;
import z03.pap22z.Settings;
import z03.pap22z.database.Database;
import z03.pap22z.database.SavedResults;
import z03.pap22z.logics.ComboGameLogic;

public class AimSniperController extends z03.pap22z.controllers.BaseAimGameController {
    private int timeLeft = Settings.getGameLength();

    @FXML
    private Label timeLeftValueLabel;
    private Timeline gameTimeline;
    private Timeline countdownTimeline;

    @Override
    protected void initializeStatics() {
        AimSniperController.GAME_NAME = "AimSniper";
        AimSniperController.NORMAL_RADIUS = 15;
        AimSniperController.RADIUS_OFFSET = 2.5;
    }

    @Override
    public void initializeMainBlock() {
        this.logic = new ComboGameLogic();
        super.initializeMainBlock();
    }

    @Override
    public void playGame() {
        circle.setVisible(false);
        messageLabel.setText(String.format("%d", DELAY_TIME));
        unsavedResult = null;

        // ready period before a game
        MusicManager.stopMenuTheme();
        MusicManager.playCountdownSound();
        countdownTimeline = new Timeline(new KeyFrame(Duration.seconds(1), event1 -> {
            int countdownTime = Integer.parseInt(messageLabel.getText()) - 1;
            if (countdownTime > 0) {
                messageLabel.setText(String.format("%d", countdownTime));
            } else {
                MusicManager.playFirstGameTheme();
                messageLabel.setText("");
                circle.setVisible(true);
                logic.toggleGameState();
                gameEndTime = LocalDateTime.now().plusSeconds(timeLeft);
                timeLeftValueLabel.setText(String.format("%d seconds", timeLeft));
                gameTimeline = new Timeline(new KeyFrame(Duration.seconds(1), event2 -> {
                    timeLeft -= 1;
                    if (timeLeft > 0) {
                        timeLeftValueLabel.setText(String.format("%d seconds", timeLeft));
                    } else {
                        MusicManager.stopAnyGameTheme();
                        MusicManager.playGameOverSound();
                        timeLeftValueLabel.setText(String.format("%d seconds", timeLeft));
                        logic.toggleGameState();
                        circle.setVisible(false);
                        messageLabel.setText("GAME OVER");
                        if(Database.isConnected()) {
                            unsavedResult = SavedResults.writeStatResult(
                                logic.getPoints(), logic.getAccuracy(), GAME_NAME
                            );
                        }
                    }
                }));
                gameTimeline.setCycleCount(timeLeft);
                gameTimeline.play();
            }
        }));
        countdownTimeline.setCycleCount(DELAY_TIME);
        countdownTimeline.play();
    }

    @Override
    protected void terminateTimelines() {
        terminateTimeline(gameTimeline);
        terminateTimeline(countdownTimeline);
    }

}
