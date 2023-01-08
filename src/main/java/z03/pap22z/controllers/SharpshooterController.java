package z03.pap22z.controllers;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import z03.pap22z.logics.GameLogic;

public class SharpshooterController extends z03.pap22z.controllers.BaseAimGameController {
    @Override
    protected void initializeStatics() {
        SharpshooterController.GAME_NAME = "Sharpshooter";
        SharpshooterController.NORMAL_RADIUS = 24;
        SharpshooterController.RADIUS_OFFSET = 8;
    }

    private int attemptsLeft = 5;

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

        // ready period before a game
        countdownTimeline = new Timeline(new KeyFrame(Duration.seconds(1), event1 -> {
            int countdownTime = Integer.parseInt(messageLabel.getText()) - 1;
            if (countdownTime > 0) {
                messageLabel.setText(String.format("%d", countdownTime));
            } else {
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
        double waitDuration = 2.0 + random.nextDouble() * 8.0;
        attemptTimeline = new Timeline(
                new KeyFrame(
                        Duration.seconds(waitDuration), event2 -> {
                            teleportCircle();
                            circle.setVisible(true);
                            start = LocalDateTime.now();
                            logic.toggleGameState();
                        }),
                new KeyFrame(Duration.seconds(3 + waitDuration), event2 -> {
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
            attemptsLeftLabel.setText(Integer.toString(attemptsLeft));
            messageLabel.setText("GAME OVER");
            gameEndTime = LocalDateTime.now();
        }
    }

    private int calculatePoints(MouseEvent event) {
        double distanceRatio = Math.pow(
                1.0 - this.clickToCenterDistance(event) / this.circleRadius.doubleValue(), 2);
        double timeRatio = 500.0 / ChronoUnit.MILLIS.between(start, LocalDateTime.now());
        return (int) (500 * distanceRatio * timeRatio);
    }

    private void terminateTimelines() {
        if (this.attemptTimeline != null) {
            this.attemptTimeline.stop();
        }
        countdownTimeline.stop();
    }

    @Override
    protected void handleNewGame(ActionEvent event) {
        terminateTimelines();
        super.handleNewGame(event);
    }

    @Override
    protected void handleExit(ActionEvent event) {
        terminateTimelines();
        super.handleExit(event);
    }
}
