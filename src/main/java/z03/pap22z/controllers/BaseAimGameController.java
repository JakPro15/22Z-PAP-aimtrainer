package z03.pap22z.controllers;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Random;

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
import z03.pap22z.GameLogic;
import z03.pap22z.Settings;
import z03.pap22z.database.Database;
import z03.pap22z.database.SavedResults;

public abstract class BaseAimGameController extends z03.pap22z.controllers.SceneController {
    protected final int DELAY_TIME = 3;
    protected static float NORMAL_RADIUS;
    protected static float RADIUS_OFFSET;
    protected static String GAME_NAME;

    protected GameLogic logic;

    protected static Random random = new Random();

    protected LocalDateTime gameEndTime;

    protected boolean isScoreSaved = false;

    @FXML
    protected AnchorPane playfield;

    @FXML
    protected Circle circle;

    @FXML
    protected Label scoreValueLabel;

    @FXML
    protected Label accuracyValueLabel;

    @FXML
    protected Label messageLabel;

    @FXML
    protected Button saveButton;

    protected SimpleDoubleProperty playWidth = new SimpleDoubleProperty();
    protected SimpleDoubleProperty playHeight = new SimpleDoubleProperty();
    protected SimpleDoubleProperty circleRadius = new SimpleDoubleProperty();

    /**
     * Initializes all needed properties and starts the game.
     */
    public void initialize() {
        circle.setRadius(BaseAimGameController.calculateCircleRadius());

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
    abstract public void playGame();

    /**
     * Teleports the circle object to random coordinates within the playfield
     * object.
     */
    protected void teleportCircle() {
        double newX = generateCircleCoord(this.playWidth.getValue());
        double newY = generateCircleCoord(this.playHeight.getValue());

        circle.setCenterX(newX);
        circle.setCenterY(newY);
    }

    /**
     * Generates a random valid circle center coordinate given span - length of
     * a single dimension of a field. This method accounts for the radius of
     * the circle to make sure the circle doesn't clip the edges of the field.
     * 
     * @return double
     */
    protected double generateCircleCoord(double span) {
        double radius = circleRadius.getValue();
        double allowedSpan = span - 2 * radius;
        return random.nextDouble() * allowedSpan + radius;
    }

    /**
     * Returns the circle radius length calculated based on current game
     * difficulty setting (the harder the game, the smaller the radius).
     * 
     * @return the calculated radius
     */
    protected static double calculateCircleRadius() {
        int gameDiff = Settings.getGameDifficulty();
        double newRadius = NORMAL_RADIUS - (gameDiff - 2) * RADIUS_OFFSET;
        return newRadius;
    }

    @FXML
    void handleSave(ActionEvent event) {
        if (!logic.getIsGameOn() && gameEndTime != null && !isScoreSaved && Database.isConnected()) {
            SavedResults.writeResult(
                    logic.getPoints(), logic.getAccuracy(), gameEndTime, GAME_NAME);
            saveButton.setText("Score saved.");
            isScoreSaved = true;
        }
    }

    @FXML
    void handleNewGame(ActionEvent event) {
        try {
            switchToScene(event, GAME_NAME);
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
    protected boolean isInCircle(MouseEvent event) {
        double posX = event.getX();
        double posY = event.getY();
        double circleX = this.circle.getCenterX();
        double circleY = this.circle.getCenterY();
        double centerDistance = calculateDistance(posX, posY, circleX, circleY);
        return centerDistance <= this.circle.getRadius();
    }

    /**
     * Calculates distance between two points.
     * 
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return the distance between points (x1, y1) and (x2, y2)
     */
    protected static double calculateDistance(double x1, double y1, double x2, double y2) {
        double deltaX = x2 - x1;
        double deltaY = y2 - y1;

        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);

    }

}
