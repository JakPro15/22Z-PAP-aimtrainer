package z03.pap22z.controllers;

import java.time.LocalDateTime;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import z03.pap22z.SharpshooterLogic;

public class SharpshooterController extends z03.pap22z.controllers.BaseAimGameController {
    private int attemptsLeft = 5;

    @FXML
    private Label attemptsLeftLabel;

    private Timeline attemptTimeline;

    static {
        SharpshooterController.GAME_NAME = "Sharpshooter";
        SharpshooterController.NORMAL_RADIUS = 24;
        SharpshooterController.RADIUS_OFFSET = 8;
    }

    @Override
    public void initialize() {
        this.logic = new SharpshooterLogic();
        super.initialize();
    }

    @Override
    public void playGame() {
        circle.setVisible(false);
        messageLabel.setText(String.format("%d", DELAY_TIME));

        // ready period before a game
        Timeline countdownTimeline = new Timeline(new KeyFrame(Duration.seconds(1), event1 -> {
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

    private void playTimeline() {
        attemptsLeftLabel.setText(Integer.toString(attemptsLeft));
        double waitDuration = 2.0 + random.nextDouble() * 8.0;
        attemptTimeline = new Timeline(
                new KeyFrame(
                        Duration.seconds(waitDuration), event2 -> {
                            System.out.println("1st frame ended");
                            teleportCircle();
                            circle.setVisible(true);
                            logic.toggleGameState();
                        }),
                new KeyFrame(Duration.seconds(3 + waitDuration), event2 -> {
                    System.out.println("2nd frame ended");
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

    @Override
    @FXML
    protected void handlePlayfieldClick(MouseEvent event) {
        if (logic.getIsGameOn()) {
            if (isInCircle(event)) {
                this.attemptTimeline.stop();
                finishAttempt();
                this.logic.registerTargetHit();
            } else {
                this.logic.registerTargetMiss();
            }
        }
    }
}
