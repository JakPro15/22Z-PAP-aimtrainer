package z03.pap22z.controllers;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import z03.pap22z.MusicManager;
import z03.pap22z.Settings;
import z03.pap22z.database.Database;
import z03.pap22z.database.SavedResults;
import z03.pap22z.logics.GameLogic;

public class SharpshooterController extends z03.pap22z.controllers.BaseAimGameController {
    private final double TARGET_TIME = 1.5;

    @Override
    protected void initializeStatics() {
        SharpshooterController.GAME_NAME = "Sharpshooter";
        SharpshooterController.NORMAL_RADIUS = 20;
        SharpshooterController.RADIUS_OFFSET = 5;
    }

    private int attemptsLeft = Settings.getSharpshooterLength();

    @FXML
    private Label attemptsLeftLabel;

    private Timeline attemptTimeline, countdownTimeline;

    private LocalDateTime start;

    @Override
    public void initializeMainBlock() {
        this.logic = new GameLogic();
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
                MusicManager.playThirdGameTheme();
                messageLabel.setText("");
                playTimeline();
            }
        }));
        countdownTimeline.setCycleCount(DELAY_TIME);
        countdownTimeline.play();
    }

    @Override
    @FXML
    protected void handlePlayfieldClick(MouseEvent event) {
        if (logic.getIsGameOn()) {
            MusicManager.playRevolverShot();
            if (isInCircle(event)) {
                this.logic.addPoints(calculatePoints(event));
                this.logic.registerTargetHit();
            } else {
                this.logic.registerTargetMiss();
            }
            this.attemptTimeline.stop();
            finishAttempt();
        }
    }

    private void playTimeline() {
        attemptsLeftLabel.setText(Integer.toString(attemptsLeft));
        double waitDuration = 2.0 + random.nextDouble() * 6.0;
        attemptTimeline = new Timeline(
                new KeyFrame(
                        Duration.seconds(waitDuration), event2 -> {
                            teleportCircle();
                            circle.setVisible(true);
                            start = LocalDateTime.now();
                            logic.toggleGameState();
                        }),
                new KeyFrame(Duration.seconds(TARGET_TIME + waitDuration), event2 -> {
                    this.logic.registerTargetMiss();
                    finishAttempt();
                }));
        attemptTimeline.play();
    }

    private void finishAttempt() {
        circle.setVisible(false);
        logic.toggleGameState();
        --attemptsLeft;
        if (attemptsLeft > 0) {
            playTimeline();
        } else {
            MusicManager.stopAnyGameTheme();
            MusicManager.playGameOverSound();
            attemptsLeftLabel.setText(Integer.toString(attemptsLeft));
            messageLabel.setText("GAME OVER");
            if (Database.isConnected()) {
                unsavedResult = SavedResults.writeStatResult(
                        logic.getPoints(), logic.getAccuracy(), GAME_NAME);
            }
            gameEndTime = LocalDateTime.now();
        }
    }

    private int calculatePoints(MouseEvent event) {
        double distanceRatio = 1.0 - this.clickToCenterDistance(event) / this.circleRadius.doubleValue();
        double timeRatio = 500.0 / ChronoUnit.MILLIS.between(start, LocalDateTime.now());
        return (int) (500 * distanceRatio * timeRatio);
    }

    @Override
    protected void terminateTimelines() {
        terminateTimeline(attemptTimeline);
        terminateTimeline(countdownTimeline);
    }
}