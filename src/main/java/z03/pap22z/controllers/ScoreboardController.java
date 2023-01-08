package z03.pap22z.controllers;

import java.io.IOException;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import z03.pap22z.MusicManager;
import z03.pap22z.StringResult;
import z03.pap22z.database.Database;
import z03.pap22z.database.Result;
import z03.pap22z.database.SavedResults;

public class ScoreboardController extends z03.pap22z.controllers.SceneController {
    @FXML
    private TableView<StringResult> resultsTable;

    /**
     * Initialize the scoreboard UI.
     */
    public void initialize() {
        resultsTable.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("gameTime"));
        resultsTable.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("user"));
        resultsTable.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("gameType"));
        resultsTable.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("difficulty"));
        resultsTable.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("length"));
        resultsTable.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("score"));
        resultsTable.getColumns().get(6).setCellValueFactory(new PropertyValueFactory<>("accuracy"));
        if(Database.isConnected()) {
            setResults(SavedResults.readAllResults());
        }
    }

    @FXML
    protected void handleViewAllResults(ActionEvent event) {
        MusicManager.playButtonSound();
        if(Database.isConnected()) {
            setResults(SavedResults.readAllResults());
        }
    }

    @FXML
    protected void handleViewYourResults(ActionEvent event) {
        MusicManager.playButtonSound();
        if(Database.isConnected()) {
            setResults(SavedResults.readCurrentProfileResults());
        }
    }

    @FXML
    protected void handleExit(ActionEvent event) {
        try {
            switchToMenu(event);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets the TableView of the scoreboard scene to the given results.
     * @param results results to be set
     */
    private void setResults(List<Result> results) {
        resultsTable.getItems().clear();
        if (results != null) {
            for (Result result : results) {
                resultsTable.getItems().add(new StringResult(result));
            }
        }
    }
}
