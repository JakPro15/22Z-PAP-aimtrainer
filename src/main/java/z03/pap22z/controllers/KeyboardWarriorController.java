package z03.pap22z.controllers;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
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
import z03.pap22z.KeyboardWarriorLogic;
import z03.pap22z.Settings;
import z03.pap22z.database.Database;
import z03.pap22z.database.SavedResults;

public class KeyboardWarriorController extends z03.pap22z.controllers.SceneController {
    private final int delay_time = 3;

    private final int squareSize = 160;

    private KeyboardWarriorLogic logic = new KeyboardWarriorLogic();

    private int timeLeft = Settings.getGameLength();

    private int spawnDelay = 6;

    private ArrayList<String> letters = new ArrayList<>(Arrays.asList("A", "S", "K", "L"));

    private ArrayList<StackPane> readySquares = new ArrayList<>();

    private LocalDateTime gameEndTime;

    private boolean isScoreSaved = false;

    private Random random = new Random();

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
                logic.toggleGameState();
                gameEndTime = LocalDateTime.now().plusSeconds(timeLeft);
                timeLeftValueLabel.setText(String.format("%d seconds", timeLeft));
                Timeline gameTimeline = new Timeline(new KeyFrame(Duration.seconds(1), event2 -> {
                    timeLeft -= 1;
                    if (timeLeft > 0) {
                    timeLeftValueLabel.setText(String.format("%d seconds", timeLeft));
                    }
                    else {
                        timeLeftValueLabel.setText(String.format("%d seconds", timeLeft));
                        logic.toggleGameState();
                        // remove all rectangles from the playfield
                        playfield.getChildren().removeIf(node -> node instanceof StackPane);
                        finishLine.setVisible(false);
                        messageLabel.setText("GAME OVER");
                    }
                }));
                gameTimeline.setCycleCount(timeLeft);
                gameTimeline.play();

                Timeline rectangleTimeline = new Timeline(new KeyFrame(Duration.millis(spawnDelay * (squareSize + 10)), event2 -> {
                    if(timeLeft > 0) {
                        Rectangle rectangle = new Rectangle(squareSize, squareSize, Color.web("#ff2600"));
                        StackPane stack = new StackPane();
                        stack.getChildren().add(rectangle);
                        Text text = new Text(letters.get(this.random.nextInt(letters.size())).toUpperCase());
                        text.setFont(Font.font("Arial", FontWeight.BOLD, squareSize / 2));
                        stack.getChildren().add(text);
                        stack.setLayoutY(-squareSize);
                        stack.setLayoutX(10 + this.random.nextInt((int)playfield.getWidth() - squareSize - 20));
                        playfield.getChildren().add(stack);

                        // make the rectangle move down a pixel every 6 milliseconds
                        Timeline movementTimeline = new Timeline(new KeyFrame(Duration.millis(spawnDelay), event3 -> {
                            stack.setLayoutY(stack.getLayoutY() + 1);
                            if(stack.getLayoutY() + squareSize == finishLine.getLayoutY()) {
                                RadialGradient gradient = new RadialGradient(
                                0,   // focusAngle
                                0,   // focusDistance
                                0.5, // centerX
                                0.5, // centerY
                                0.5, // radius
                                true, // proportional
                                CycleMethod.NO_CYCLE,
                                new Stop(0, Color.WHITE),
                                new Stop(1, Color.web("#ff2600"))
                                );
                                rectangle.setFill(gradient);
                                readySquares.add(stack);
                            }
                            if(stack.getLayoutY() == finishLine.getLayoutY() + finishLine.getHeight()) {
                                if(readySquares.contains(stack)) {
                                    rectangle.setFill(Color.web("#ff2600"));
                                    readySquares.remove(stack);
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
        countdownTimeline.setCycleCount(delay_time);
        countdownTimeline.play();
    }

    @FXML
    void handleKeyPressed(KeyEvent event) {
        if(logic.getIsGameOn()) {
            String pressedLetter = event.getCode().getName();
            if(letters.contains(pressedLetter)) {
                ArrayList<StackPane> squaresToDiscard = new ArrayList<>();
                boolean hitSomething = false;
                for(StackPane square : readySquares) {
                    String squareText = "";
                    for(Node child : square.getChildren()) {
                        if(child instanceof Text) {
                            squareText = ((Text) child).getText();
                        }
                    }
                    if(squareText.equals(pressedLetter)) {
                        this.logic.registerTargetHit();
                        hitSomething = true;
                        squaresToDiscard.add(square);
                    }
                }
                if(!hitSomething) {
                    this.logic.registerTargetMiss();
                }
                readySquares.removeAll(squaresToDiscard);
                playfield.getChildren().removeAll(squaresToDiscard);
            }
        }
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
