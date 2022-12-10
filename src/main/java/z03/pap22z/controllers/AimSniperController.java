package z03.pap22z.controllers;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
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
import z03.pap22z.AimSniperLogic;
import z03.pap22z.Settings;
import z03.pap22z.database.SavedResults;

// TODO: make the game calculate points differently depending on circle radius
public class AimSniperController extends z03.pap22z.controllers.SceneController {
    private final int delay_time = 3;
    private final int DEFAULT_RADIUS = 15;

    private AimSniperLogic logic = new AimSniperLogic();

    private Timer timer = new Timer();

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

    public void playGame() {
        circle.setVisible(false);
        messageLabel.setText(String.format("%d", delay_time));
        synchronized (this.timer) {
            // ready period before a game
            this.timer.scheduleAtFixedRate(new TimerTask() {
                int time_left = delay_time;

                @Override
                public void run() {

                    is_game_on = false;
                    System.out.println(time_left);
                    Platform.runLater(() -> messageLabel.setText(String.format("%d", time_left + 1)));
                    time_left -= 1;
                    if (time_left < 0) {
                        Platform.runLater(() -> messageLabel.setText(""));
                        circle.setVisible(true);
                        this.cancel();
                    }

                }
            }, 0, 1000);

            this.timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            is_game_on = true;
                            timeLeftValueLabel.setText(String.format("%d seconds", timeLeft));
                            timeLeft -= 1;
                            if (timeLeft == -1) {
                                circle.setVisible(false);
                                messageLabel.setText("GAME OVER");
                                timer.cancel();
                                is_game_on = false;
                                gameEndTime = LocalDateTime.now();
                            }
                        }
                    });
                }
            }, delay_time * 1000, 1000);
        }
    }

    public void teleportCircle() {
        double new_x = generateCircleCoord(this.play_width.getValue());
        double new_y = generateCircleCoord(this.play_height.getValue());

        circle.setCenterX(new_x);
        circle.setCenterY(new_y);
    }

    public double generateCircleCoord(double span) {
        double radius = circle_radius.getValue();
        double allowed_span = span - 2 * radius;
        return random.nextDouble() * allowed_span + radius;
    }

    public void initializeCircle() {
        teleportCircle();
    }

    public double calculateCircleRadius() {
        int game_diff = Settings.getGameDifficulty();
        double new_radius = DEFAULT_RADIUS - (game_diff - 2) * 2.5;
        return new_radius;
    }

    @FXML
    void handleSave(ActionEvent event) {
        if (!is_game_on && gameEndTime != null && !is_score_saved) {
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
            timer.cancel();
            switchToMenu(event);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void handlePlayfieldClick(MouseEvent event) {
        if (this.is_game_on) {
            if (isInCircle(event)) {
                System.out.println("Hit.");
                teleportCircle();
                this.logic.registerTargetHit();
            } else {
                System.out.println("Miss.");
                this.logic.registerTargetMiss();
            }
        } else {
            System.out.println("Game not on.");
        }
    }

    private boolean isInCircle(MouseEvent event) {
        double pos_x = event.getX();
        double pos_y = event.getY();
        double circle_x = this.circle.getCenterX();
        double circle_y = this.circle.getCenterY();
        double center_distance = calculateDistance(pos_x, pos_y, circle_x, circle_y);
        System.out.println(center_distance);
        return center_distance <= this.circle.getRadius();
    }

    private double calculateDistance(double x1, double y1, double x2, double y2) {
        double delta_x = x2 - x1;
        double delta_y = y2 - y1;

        return Math.sqrt(delta_x * delta_x + delta_y * delta_y);

    }

}
