package z03.pap22z.controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
import z03.pap22z.Settings;
import z03.pap22z.database.Database;
import z03.pap22z.database.SavedResults;
import z03.pap22z.logics.ComboGameLogic;

public abstract class BaseFallingShapeGameController extends z03.pap22z.controllers.BaseGameController{
    protected static PseudoClass ON_TARGET = PseudoClass.getPseudoClass("onTarget");
    protected int spawnDelay;
    protected int timeLeft;

    /**
     * Returns a list of keys chosen by the user in Settings to be used in all
     * falling-shape type games
     * @return list of character representations of chosen keys
     */
    protected List<String> getLetters() {
        return Settings.getKeys();
    }

    protected ArrayList<StackPane> readyShapes = new ArrayList<>();
    protected Timeline countdownTimeline;
    protected Timeline gameTimeline;
    protected Timeline shapeTimeline;
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

    @Override
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
                        // remove all shapes from the playfield
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
                spawnShapes();
            }

        }));
        countdownTimeline.setCycleCount(DELAY_TIME);
        countdownTimeline.play();
    }

    /**
     * This method is responsible for creating the two timelines
     * in which subsequent shapes are created and moved respectively
     * checking when they reach the finish line
     * and adding them to and removing them from
     * the list of valid shapes
     * (valid means such that points will be awarded upon hitting the correct key) 
     */
    protected abstract void spawnShapes();

    @FXML
    void handleKeyPressed(KeyEvent event) {
        if (logic.getIsGameOn()) {
            MusicManager.playHitMarkerSound();
            String pressedLetter = event.getCode().getName();
            if (getLetters().contains(pressedLetter)) {
                ArrayList<StackPane> shapesToDiscard = new ArrayList<>();
                boolean hitSomething = false;
                for (StackPane shape : readyShapes) {
                    String shapeText = "";
                    for (Node child : shape.getChildren()) {
                        if (child instanceof Text) {
                            shapeText = ((Text) child).getText();
                        }
                    }
                    if (shapeText.equals(pressedLetter)) {
                        this.logic.registerTargetHit();
                        hitSomething = true;
                        shapesToDiscard.add(shape);
                    }
                }
                if (!hitSomething) {
                    this.logic.registerTargetMiss();
                }
                readyShapes.removeAll(shapesToDiscard);
                playfield.getChildren().removeAll(shapesToDiscard);
            }
        }
    }

    @Override
    protected void terminateTimelines() {
        terminateTimeline(movementTimeline);
        terminateTimeline(shapeTimeline);
        terminateTimeline(gameTimeline);
        terminateTimeline(countdownTimeline);
    }

}
