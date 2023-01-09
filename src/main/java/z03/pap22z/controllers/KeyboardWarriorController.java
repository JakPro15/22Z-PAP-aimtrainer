package z03.pap22z.controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;
import z03.pap22z.MusicManager;
import z03.pap22z.Settings;
import z03.pap22z.database.Database;
import z03.pap22z.database.SavedResults;
import z03.pap22z.logics.ComboGameLogic;

public class KeyboardWarriorController extends z03.pap22z.controllers.BaseGameController {
    private final int squareSize = 80;

    private int timeLeft = Settings.getGameLength();

    private int spawnDelay = 6;

    private ArrayList<String> letters = new ArrayList<>(Arrays.asList("A", "S", "K", "L"));

    private ArrayList<StackPane> readySquares = new ArrayList<>();

    private Timeline countdownTimeline;
    private Timeline gameTimeline;
    private Timeline rectangleTimeline;
    private Timeline movementTimeline;

    @FXML
    private Rectangle finishLine;

    @FXML
    private Label timeLeftValueLabel;

    @Override
    protected void initializeStatics() {
        KeyboardWarriorController.GAME_NAME = "KeyboardWarrior";
    }

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

                rectangleTimeline = new Timeline(
                        new KeyFrame(Duration.millis(spawnDelay * (squareSize + 10)), event2 -> {
                            if (timeLeft > 0) {
                                Rectangle rectangle = new Rectangle(squareSize, squareSize, Color.web("#ff2600"));
                                StackPane stack = new StackPane();
                                stack.getChildren().add(rectangle);
                                Text text = new Text(letters.get(random.nextInt(letters.size())).toUpperCase());
                                text.setFont(Font.font("Arial", FontWeight.BOLD, squareSize / 2));
                                stack.getChildren().add(text);
                                stack.setLayoutY(-squareSize);
                                stack.setLayoutX(
                                        10 + random.nextInt((int) playfield.getWidth() - squareSize - 20));
                                playfield.getChildren().add(stack);

                                // make the rectangle move down a pixel every 6 milliseconds
                                movementTimeline = new Timeline(
                                        new KeyFrame(Duration.millis(spawnDelay), event3 -> {
                                            stack.setLayoutY(stack.getLayoutY() + 1);
                                            if (stack.getLayoutY() + squareSize == finishLine.getLayoutY()) {
                                                RadialGradient gradient = new RadialGradient(
                                                        0, // focusAngle
                                                        0, // focusDistance
                                                        0.5, // centerX
                                                        0.5, // centerY
                                                        0.5, // radius
                                                        true, // proportional
                                                        CycleMethod.NO_CYCLE,
                                                        new Stop(0, Color.WHITE),
                                                        new Stop(1, Color.web("#ff2600")));
                                                rectangle.setFill(gradient);
                                                readySquares.add(stack);
                                            }
                                            if (stack.getLayoutY() == finishLine.getLayoutY()
                                                    + finishLine.getHeight()) {
                                                if (readySquares.contains(stack)) {
                                                    rectangle.setFill(Color.web("#ff2600"));
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

        }));
        countdownTimeline.setCycleCount(DELAY_TIME);
        countdownTimeline.play();
    }

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
