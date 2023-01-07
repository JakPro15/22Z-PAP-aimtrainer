package z03.pap22z.controllers;

import java.time.LocalDateTime;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.util.Duration;
import z03.pap22z.AimSniperLogic;
import z03.pap22z.Settings;

public class AimSniperController extends z03.pap22z.controllers.BaseAimGameController {
    private int timeLeft = Settings.getGameLength();

    @FXML
    private Label timeLeftValueLabel;

    static {
        AimSniperController.GAME_NAME = "AimSniper";
        AimSniperController.NORMAL_RADIUS = 15;
        AimSniperController.RADIUS_OFFSET = 2.5;
    }

    @Override
    public void initialize() {
        this.logic = new AimSniperLogic();
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
                circle.setVisible(true);
                logic.toggleGameState();
                gameEndTime = LocalDateTime.now().plusSeconds(timeLeft);
                timeLeftValueLabel.setText(String.format("%d seconds", timeLeft));
                Timeline gameTimeline = new Timeline(new KeyFrame(Duration.seconds(1), event2 -> {
                    timeLeft -= 1;
                    if (timeLeft > 0) {
                        timeLeftValueLabel.setText(String.format("%d seconds", timeLeft));
                    } else {
                        timeLeftValueLabel.setText(String.format("%d seconds", timeLeft));
                        logic.toggleGameState();
                        circle.setVisible(false);
                        messageLabel.setText("GAME OVER");
                    }
                }));
                gameTimeline.setCycleCount(timeLeft);
                gameTimeline.play();
            }
        }));
        countdownTimeline.setCycleCount(DELAY_TIME);
        countdownTimeline.play();
    }

}
