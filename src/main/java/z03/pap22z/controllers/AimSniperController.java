package z03.pap22z.controllers;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Random;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import z03.pap22z.AimSniperLogic;
import z03.pap22z.Settings;
import z03.pap22z.database.Database;
import z03.pap22z.database.SavedResults;


public class AimSniperController extends z03.pap22z.controllers.SceneController {
    private final int delay_time = 3;
    private final int DEFAULT_RADIUS = 15;

    private AimSniperLogic logic = new AimSniperLogic();

    private int timeLeft = Settings.getGameLength();

    private Random random;

    private LocalDateTime gameEndTime;

    private boolean isScoreSaved = false;

    @FXML
    private AnchorPane playfield;

    @FXML
    private Circle circle;

    @FXML
    private Label scoreValueLabel;

    @FXML
    private Label accuracyValueLabel;

    @FXML
    private Label timeLeftValueLabel;

    @FXML
    private Label messageLabel;

    @FXML
    private Button saveButton;

    private SimpleDoubleProperty playWidth = new SimpleDoubleProperty();
    private SimpleDoubleProperty playHeight = new SimpleDoubleProperty();
    private SimpleDoubleProperty circleRadius = new SimpleDoubleProperty();

    /**
     * Initializes all needed properties and starts the game.
     */
    public void initialize() {
        this.random = new Random();

        circle.setRadius(this.calculateCircleRadius());

        circleRadius.bind(this.circle.radiusProperty());
        playWidth.bind(this.playfield.widthProperty());
        playHeight.bind(this.playfield.heightProperty());

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
        circle.setVisible(false);
        messageLabel.setText(String.format("%d", delay_time));

        // ready period before a game
        Timeline countdownTimeline = new Timeline(new KeyFrame(Duration.seconds(1), event1 -> {
            int countdownTime = Integer.parseInt(messageLabel.getText()) - 1;
            if (countdownTime > 0) {
                messageLabel.setText(String.format("%d", countdownTime));
            }
            else {
                messageLabel.setText("");
                circle.setVisible(true);
                logic.switchGameState();
                gameEndTime = LocalDateTime.now().plusSeconds(timeLeft);
                timeLeftValueLabel.setText(String.format("%d seconds", timeLeft));
                Timeline gameTimeline = new Timeline(new KeyFrame(Duration.seconds(1), event2 -> {
                    timeLeft -= 1;
                    if (timeLeft > 0) {
                    timeLeftValueLabel.setText(String.format("%d seconds", timeLeft));
                    }
                    else {
                        timeLeftValueLabel.setText(String.format("%d seconds", timeLeft));
                        logic.switchGameState();
                        circle.setVisible(false);
                        messageLabel.setText("GAME OVER");
                    }
                }));
                gameTimeline.setCycleCount(timeLeft);
                gameTimeline.play();
            }
        }));
        countdownTimeline.setCycleCount(delay_time);
        countdownTimeline.play();
    }

    /**
     * Teleports the circle object to random coordinates within the playfield
     * object.
     */
    public void teleportCircle() {
        double newX = generateCircleCoord(this.playWidth.getValue());
        double newY = generateCircleCoord(this.playHeight.getValue());

        circle.setCenterX(newX);
        circle.setCenterY(newY);
    }

    /**
     * Generates a random valid circle center coordinate given span - length of
     * a single dimension of a field. This method accounts for the radius of
     * the circle to make sure the circle doesn't clip the edges of the field.
     * @return double
     */
    public double generateCircleCoord(double span) {
        double radius = circleRadius.getValue();
        double allowedSpan = span - 2 * radius;
        return random.nextDouble() * allowedSpan + radius;
    }

    /**
     * Returns the circle radius length calculated based on current game
     * difficulty setting (the harder the game, the smaller the radius).
     * @return the calculated radius
     */
    public double calculateCircleRadius() {
        int gameDiff = Settings.getGameDifficulty();
        double newRadius = DEFAULT_RADIUS - (gameDiff - 2) * 2.5;
        return newRadius;
    }

    @FXML
    void handleSave(ActionEvent event) {
        if (!logic.getIsGameOn() && gameEndTime != null && !isScoreSaved && Database.isConnected()) {
            SavedResults.writeResult(
                    logic.getPoints(), logic.getAccuracy(), gameEndTime, "AimSniper");
            saveButton.setText("Score saved.");
            isScoreSaved = true;
        }
    }

    @FXML
    void handleNewGame(ActionEvent event) {
        try {
            switchToScene(event, "AimSniper");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void handleExit(ActionEvent event) {
        try {
            switchToGameMenu(event);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void handlePlayfieldClick(MouseEvent event) {
        if (logic.getIsGameOn()) {
            if (isInCircle(event)) {
                teleportCircle();
                this.logic.registerTargetHit();
            } else {
                this.logic.registerTargetMiss();
            }
        }
    }

    /**
     * @param event mouse click event
     * @return whether the click occured within the circle
     */
    private boolean isInCircle(MouseEvent event) {
        double posX = event.getX();
        double posY = event.getY();
        double circleX = this.circle.getCenterX();
        double circleY = this.circle.getCenterY();
        double centerDistance = calculateDistance(posX, posY, circleX, circleY);
        return centerDistance <= this.circle.getRadius();
    }

    /**
     * Calculates distance between two points.
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return the distance between points (x1, y1) and (x2, y2)
     */
    private double calculateDistance(double x1, double y1, double x2, double y2) {
        double deltaX = x2 - x1;
        double deltaY = y2 - y1;

        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);

    }

}
