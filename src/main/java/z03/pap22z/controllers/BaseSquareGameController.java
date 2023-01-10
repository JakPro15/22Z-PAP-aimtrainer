package z03.pap22z.controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import z03.pap22z.MusicManager;
import z03.pap22z.database.Database;
import z03.pap22z.database.SavedResults;
import z03.pap22z.logics.ComboGameLogic;

public abstract class BaseSquareGameController extends z03.pap22z.controllers.BaseGameController{
    protected static PseudoClass ON_TARGET = PseudoClass.getPseudoClass("onTarget");

    protected int squareSize;

    protected int spawnDelay;

    protected int timeLeft;

    protected ArrayList<String> letters = new ArrayList<>(Arrays.asList("A", "S", "K", "L"));
    
    protected ArrayList<StackPane> readySquares = new ArrayList<>();

    protected Timeline countdownTimeline;
    protected Timeline gameTimeline;
    protected Timeline rectangleTimeline;
    protected Timeline movementTimeline;

    @FXML
    protected Rectangle finishLine;

    @FXML
    protected Label timeLeftValueLabel;

    @Override
    public void initializeMainBlock() {

        this.finishLine.widthProperty().bind(playfield.widthProperty());
        this.logic = new ComboGameLogic();

        super.initializeMainBlock();
    }

    public void playGame() {
        finishLine.setVisible(false);
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
                MusicManager.playSecondGameTheme();
                messageLabel.setText("");
                finishLine.setVisible(true);
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
                        // remove all rectangles from the playfield
                        playfield.getChildren().removeIf(node -> node instanceof StackPane);
                        finishLine.setVisible(false);
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
                spawnSquares();
            }

        }));
        countdownTimeline.setCycleCount(DELAY_TIME);
        countdownTimeline.play();
    }

    protected abstract void spawnSquares();

    @FXML
    void handleKeyPressed(KeyEvent event) {
        if (logic.getIsGameOn()) {
            MusicManager.playHitMarkerSound();
            String pressedLetter = event.getCode().getName();
            if (letters.contains(pressedLetter)) {
                ArrayList<StackPane> squaresToDiscard = new ArrayList<>();
                boolean hitSomething = false;
                for (StackPane square : readySquares) {
                    String squareText = "";
                    for (Node child : square.getChildren()) {
                        if (child instanceof Text) {
                            squareText = ((Text) child).getText();
                        }
                    }
                    if (squareText.equals(pressedLetter)) {
                        this.logic.registerTargetHit();
                        hitSomething = true;
                        squaresToDiscard.add(square);
                    }
                }
                if (!hitSomething) {
                    this.logic.registerTargetMiss();
                }
                readySquares.removeAll(squaresToDiscard);
                playfield.getChildren().removeAll(squaresToDiscard);
            }
        }
    }

    @Override
    protected void terminateTimelines() {
        terminateTimeline(movementTimeline);
        terminateTimeline(rectangleTimeline);
        terminateTimeline(gameTimeline);
        terminateTimeline(countdownTimeline);
    }

}
