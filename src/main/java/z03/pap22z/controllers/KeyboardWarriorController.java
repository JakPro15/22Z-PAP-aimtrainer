package z03.pap22z.controllers;

import java.time.LocalDateTime;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;
import z03.pap22z.MusicManager;
import z03.pap22z.Settings;
import z03.pap22z.database.Database;
import z03.pap22z.database.SavedResults;

public class KeyboardWarriorController extends z03.pap22z.controllers.BaseSquareGameController {
    @Override
    protected void initializeStatics() {
        KeyboardWarriorController.GAME_NAME = "KeyboardWarrior";
    }

    @Override
    public void initializeMainBlock() {

        squareSize = (int) (80 * (1 - 0.1 * (Settings.getGameDifficulty() - 2)));
        spawnDelay = 6 - Settings.getGameDifficulty() + 2;
        timeLeft = Settings.getGameLength();

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

    protected void spawnSquares() {
        rectangleTimeline = new Timeline(
            new KeyFrame(Duration.millis(spawnDelay * (squareSize + 10)), event2 -> {
                if (timeLeft > 0) {
                    Rectangle rectangle = new Rectangle(squareSize, squareSize, Color.web("#ff2600"));
                    rectangle.getStyleClass().add("rectangle");
                    StackPane stack = new StackPane();
                    stack.getChildren().add(rectangle);
                    Text text = new Text(letters.get(random.nextInt(letters.size())).toUpperCase());
                    text.setFont(Font.font("Arial", FontWeight.BOLD, squareSize / 2));
                    stack.getChildren().add(text);
                    stack.setLayoutY(-squareSize);
                    stack.setLayoutX(
                            10 + random.nextInt((int) playfield.getWidth() - squareSize - 20));
                    playfield.getChildren().add(stack);

                    movementTimeline = new Timeline(
                            new KeyFrame(Duration.millis(spawnDelay), event3 -> {
                                stack.setLayoutY(stack.getLayoutY() + 1);
                                if (stack.getLayoutY() + squareSize == finishLine.getLayoutY()) {
                                    rectangle.pseudoClassStateChanged(ON_TARGET, true);
                                    readySquares.add(stack);
                                }
                                if (stack.getLayoutY() == finishLine.getLayoutY()
                                        + finishLine.getHeight()) {
                                    if (readySquares.contains(stack)) {
                                        rectangle.pseudoClassStateChanged(ON_TARGET, false);
                                        readySquares.remove(stack);
                                        if (this.logic.getIsGameOn()) {
                                            this.logic.registerTargetMiss();
                                        }
                                    }
                                }
                            }));
                    movementTimeline.setCycleCount(Timeline.INDEFINITE);
                    movementTimeline.play();
                }
            }));
        rectangleTimeline.setCycleCount(1000 * timeLeft / (spawnDelay * (squareSize + 10)));
        rectangleTimeline.play();
    }
}
