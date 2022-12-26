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

    private Boolean is_game_on = false;

    private LocalDateTime gameEndTime;

    private boolean is_score_saved = false;

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
    private Button save_button;

    private SimpleDoubleProperty play_width = new SimpleDoubleProperty();
    private SimpleDoubleProperty play_height = new SimpleDoubleProperty();
    private SimpleDoubleProperty circle_radius = new SimpleDoubleProperty();

    /**
     * Initializes all needed properties and starts the game.
     */
    public void initialize() {
        this.random = new Random();

        circle.setRadius(this.calculateCircleRadius());

        circle_radius.bind(this.circle.radiusProperty());
        play_width.bind(this.playfield.widthProperty());
        play_height.bind(this.playfield.heightProperty());

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
            int time_left = Integer.parseInt(messageLabel.getText()) - 1;
            if (time_left > 0) {
                messageLabel.setText(String.format("%d", time_left));
            }
            else {
                messageLabel.setText("");
                circle.setVisible(true);
                is_game_on = true;
                gameEndTime = LocalDateTime.now().plusSeconds(timeLeft);
                timeLeftValueLabel.setText(String.format("%d seconds", timeLeft));
                Timeline gameTimeline = new Timeline(new KeyFrame(Duration.seconds(1), event2 -> {
                    timeLeft -= 1;
                    if (timeLeft > 0) {
                    timeLeftValueLabel.setText(String.format("%d seconds", timeLeft));
                    }
                    else {
                        timeLeftValueLabel.setText(String.format("%d seconds", timeLeft));
                        is_game_on = false;
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
        double new_x = generateCircleCoord(this.play_width.getValue());
        double new_y = generateCircleCoord(this.play_height.getValue());

        circle.setCenterX(new_x);
        circle.setCenterY(new_y);
    }

    /**
     * Generates a random valid circle center coordinate given span - length of
     * a single dimension of a field. This method accounts for the radius of
     * the circle to make sure the circle doesn't clip the edges of the field.
     * @return double
     */
    public double generateCircleCoord(double span) {
        double radius = circle_radius.getValue();
        double allowed_span = span - 2 * radius;
        return random.nextDouble() * allowed_span + radius;
    }

    /**
     * Returns the circle radius length calculated based on current game
     * difficulty setting (the harder the game, the smaller the radius).
     * @return the calculated radius
     */
    public double calculateCircleRadius() {
        int game_diff = Settings.getGameDifficulty();
        double new_radius = DEFAULT_RADIUS - (game_diff - 2) * 2.5;
        return new_radius;
    }

    @FXML
    void handleSave(ActionEvent event) {
        if (!is_game_on && gameEndTime != null && !is_score_saved && Database.isConnected()) {
            SavedResults.writeResult(
                    logic.getPoints(), logic.getAccuracy(), gameEndTime, "AimSniper");
            save_button.setText("Score saved.");
            is_score_saved = true;
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
        if (this.is_game_on) {
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
        double pos_x = event.getX();
        double pos_y = event.getY();
        double circle_x = this.circle.getCenterX();
        double circle_y = this.circle.getCenterY();
        double center_distance = calculateDistance(pos_x, pos_y, circle_x, circle_y);
        return center_distance <= this.circle.getRadius();
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
        double delta_x = x2 - x1;
        double delta_y = y2 - y1;

        return Math.sqrt(delta_x * delta_x + delta_y * delta_y);

    }

}
