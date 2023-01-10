package z03.pap22z.controllers;

import java.io.IOException;
import java.util.Map;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import z03.pap22z.MusicManager;
import z03.pap22z.database.Database;
import z03.pap22z.database.SavedStatistics;
import z03.pap22z.database.Statistics;

public class StatisticsController extends z03.pap22z.controllers.SceneController {
    private static final String INDENT = "    ";
    @FXML
    private Text avgScoreAimSniperLabel;
    @FXML
    private Text avgAccuracyAimSniperLabel;
    @FXML
    private Text numberOfGamesAimSniperLabel;
    @FXML
    private Text avgScoreKeyboardWarriorLabel;
    @FXML
    private Text avgAccuracyKeyboardWarriorLabel;
    @FXML
    private Text numberOfGamesKeyboardWarriorLabel;
    @FXML
    private Text avgScoreSharpshooterLabel;
    @FXML
    private Text avgAccuracySharpshooterLabel;
    @FXML
    private Text numberOfGamesSharpshooterLabel;

    /**
     * Initialize the statistics UI.
     */
    public void initialize() {
        Map<String, Statistics> stats = SavedStatistics.readAllStatistics();
        Statistics stat = stats.get("AimSniper");
        if(stat != null) {
            avgScoreAimSniperLabel.setText(
                String.format("%sAverage score: %.2f", INDENT, stat.getAverageScore())
            );
            avgAccuracyAimSniperLabel.setText(
                String.format("%sAverage accuracy: %.2f", INDENT, stat.getAverageAccuracy())
            );
            numberOfGamesAimSniperLabel.setText(
                String.format("%sNumber of games: %d", INDENT, stat.getNumberOfGames())
            );
        }
        else {
            avgScoreAimSniperLabel.setText(String.format("%sAverage score:", INDENT));
            avgAccuracyAimSniperLabel.setText(String.format("%sAverage accuracy:", INDENT));
            numberOfGamesAimSniperLabel.setText(String.format("%sNumber of games: 0", INDENT));
        }
        stat = stats.get("KeyboardWarrior");
        if(stat != null) {
            avgScoreKeyboardWarriorLabel.setText(
                String.format("%sAverage score: %.2f", INDENT, stat.getAverageScore())
            );
            avgAccuracyKeyboardWarriorLabel.setText(
                String.format("%sAverage accuracy: %.2f", INDENT, stat.getAverageAccuracy())
            );
            numberOfGamesKeyboardWarriorLabel.setText(
                String.format("%sNumber of games: %d", INDENT, stat.getNumberOfGames())
            );
        }
        else {
            avgScoreKeyboardWarriorLabel.setText(String.format("%sAverage score:", INDENT));
            avgAccuracyKeyboardWarriorLabel.setText(String.format("%sAverage accuracy:", INDENT));
            numberOfGamesKeyboardWarriorLabel.setText(String.format("%sNumber of games: 0", INDENT));
        }
        stat = stats.get("Sharpshooter");
        if(stat != null) {
            avgScoreSharpshooterLabel.setText(
                String.format("%sAverage score: %.2f", INDENT, stat.getAverageScore())
            );
            avgAccuracySharpshooterLabel.setText(
                String.format("%sAverage accuracy: %.2f", INDENT, stat.getAverageAccuracy())
            );
            numberOfGamesSharpshooterLabel.setText(
                String.format("%sNumber of games: %d", INDENT, stat.getNumberOfGames())
            );
        }
        else {
            avgScoreSharpshooterLabel.setText(String.format("%sAverage score:", INDENT));
            avgAccuracySharpshooterLabel.setText(String.format("%sAverage accuracy:", INDENT));
            numberOfGamesSharpshooterLabel.setText(String.format("%sNumber of games: 0", INDENT));
        }
    }

    @FXML
    protected void handleClearAllStatistics(ActionEvent event) {
        MusicManager.playButtonSound();
        if(Database.isConnected()) {
            SavedStatistics.clearAllStatisticsAndResults();
            // reinitialize
            initialize();
        }
    }

    @FXML
    protected void handleExit(ActionEvent event) {
        try {
            switchToScoreboard(event);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
