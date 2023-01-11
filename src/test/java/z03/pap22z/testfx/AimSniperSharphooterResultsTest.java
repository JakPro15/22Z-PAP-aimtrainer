package z03.pap22z.testfx;

import static org.junit.Assert.assertTrue;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.LabeledMatchers.hasText;
import static org.testfx.matcher.control.TableViewMatchers.containsRowAtIndex;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.testfx.api.FxToolkit;

import javafx.scene.input.MouseButton;
import z03.pap22z.App;
import z03.pap22z.StringResult;
import z03.pap22z.controllers.StatisticsController;
import z03.pap22z.database.Database;
import z03.pap22z.database.Result;
import z03.pap22z.database.SavedResults;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class AimSniperSharphooterResultsTest extends BaseTestFXTest {
    @Before
    public void setup() throws Exception {
        FxToolkit.registerPrimaryStage();
        FxToolkit.setupApplication(App.class);
        Database.resetDatabase();
    }

    @After
    public void cleanup() throws TimeoutException {
        FxToolkit.hideStage();
        Database.resetDatabase();
    }

    @Test
    public void testScoreboardResults() {
        // set settings - length to 5 seconds, sharpshooter to 3 attempts
        clickOn("#settingsButton");
        moveTo("#gameLengthSlider > .thumb");
		press(MouseButton.PRIMARY);
		moveBy(-100, 0);
		release(MouseButton.PRIMARY);
        moveTo("#sharpshooterLengthSlider > .thumb");
		press(MouseButton.PRIMARY);
		moveBy(-100, 0);
		release(MouseButton.PRIMARY);
        clickOn("#saveButton");
        // now do 2 games: AimSniper and Sharpshooter
        clickOn("#exitButton");
        // enter game selection menu
        clickOn("#startButton");
        // enter AimSniper scene
        clickOn("#aimSniperButton");
        verifyThat("#messageLabel", hasText("3"));
        sleep(1, TimeUnit.SECONDS);
        verifyThat("#messageLabel", hasText("2"));
        sleep(1, TimeUnit.SECONDS);
        verifyThat("#messageLabel", hasText("1"));
        sleep(1, TimeUnit.SECONDS);
        sleep(100, TimeUnit.MILLISECONDS);
        verifyThat("#timeLeftValueLabel", hasText("5 seconds"));
        for(int i = 1; i <= 5; i++) {
            clickOn("#circle");
        }
        sleep(5, TimeUnit.SECONDS);
        verifyThat("#messageLabel", hasText("GAME OVER"));
        verifyThat("#scoreValueLabel", hasText("150"));
        verifyThat("#accuracyValueLabel", hasText("100.00%"));
        clickOn("#saveButton");
        verifyThat("#saveButton", hasText("Score saved."));
        // check scoreboard
        clickOn("#exitButton");
        clickOn("#exitButton");
        clickOn("#scoreboardButton");
        LocalDateTime gameEnd = SavedResults.readAllResults().get(0).getGameTime();
        String gameEndFormatted = gameEnd.format(StringResult.dateFormatter);
        verifyThat(
            "#resultsTable",
            containsRowAtIndex(0, gameEndFormatted, "default", "AimSniper",
                               "Normal", "5 seconds", "150", "100.00%")
        );
        clickOn("#exitButton");

        // enter game selection menu
        clickOn("#startButton");
        // enter Sharpshooter scene
        clickOn("#sharpshooterButton");
        verifyThat("#messageLabel", hasText("3"));
        sleep(1, TimeUnit.SECONDS);
        verifyThat("#messageLabel", hasText("2"));
        sleep(1, TimeUnit.SECONDS);
        verifyThat("#messageLabel", hasText("1"));
        sleep(1100, TimeUnit.MILLISECONDS);
        verifyThat("#attemptsLeftLabel", hasText("3"));
        verifyThat("#scoreValueLabel", hasText("0"));
        verifyThat("#accuracyValueLabel", hasText("0.00%"));
        // shoot 3 times, hitting
        for(int i = 1; i <= 3; i++) {
            assertTrue(tryToDo(query->clickOn(query), "#circle", 500, 30));
            verifyThat("#attemptsLeftLabel", hasText(String.format("%d", 3 - i)));
            verifyThat("#accuracyValueLabel", hasText("100.00%"));
        }
        verifyThat("#messageLabel", hasText("GAME OVER"));
        clickOn("#saveButton");
        verifyThat("#saveButton", hasText("Score saved."));
        // check scoreboard
        clickOn("#exitButton");
        clickOn("#exitButton");
        clickOn("#scoreboardButton");
        List<Result> results = SavedResults.readAllResults();
        verifyThat(
            "#resultsTable",
            containsRowAtIndex(
                0, results.get(0).getGameTime().format(StringResult.dateFormatter),
                "default", "Sharpshooter", "Normal", "3 attempts",
                String.format("%d", results.get(0).getScore()), "100.00%"
            )
        );
        verifyThat(
            "#resultsTable",
            containsRowAtIndex(1, gameEndFormatted, "default", "AimSniper",
                               "Normal", "5 seconds", "150", "100.00%")
        );
        // check stats
        clickOn("#statsButton");
        verifyThat("#avgScoreAimSniperLabel", hasText(StatisticsController.INDENT + "Average score: 150.00"));
        verifyThat("#avgAccuracyAimSniperLabel", hasText(StatisticsController.INDENT + "Average accuracy: 100.00%"));
        verifyThat("#numberOfGamesAimSniperLabel", hasText(StatisticsController.INDENT + "Number of games: 1"));
        verifyThat("#avgScoreKeyboardWarriorLabel", hasText(StatisticsController.INDENT + "Average score:"));
        verifyThat("#avgAccuracyKeyboardWarriorLabel", hasText(StatisticsController.INDENT + "Average accuracy:"));
        verifyThat("#numberOfGamesKeyboardWarriorLabel", hasText(StatisticsController.INDENT + "Number of games: 0"));
        verifyThat("#avgScoreSharpshooterLabel", hasText(
            StatisticsController.INDENT + "Average score: " + results.get(0).getScore() + ".00"
        ));
        verifyThat("#avgAccuracySharpshooterLabel", hasText(StatisticsController.INDENT + "Average accuracy: 100.00%"));
        verifyThat("#numberOfGamesSharpshooterLabel", hasText(StatisticsController.INDENT + "Number of games: 1"));
    }

    @Test
    public void testStatistics() {
        clickOn("#scoreboardButton");
        clickOn("#statsButton");
        verifyThat("#avgScoreAimSniperLabel", hasText(StatisticsController.INDENT + "Average score:"));
        verifyThat("#avgAccuracyAimSniperLabel", hasText(StatisticsController.INDENT + "Average accuracy:"));
        verifyThat("#numberOfGamesAimSniperLabel", hasText(StatisticsController.INDENT + "Number of games: 0"));
        verifyThat("#avgScoreKeyboardWarriorLabel", hasText(StatisticsController.INDENT + "Average score:"));
        verifyThat("#avgAccuracyKeyboardWarriorLabel", hasText(StatisticsController.INDENT + "Average accuracy:"));
        verifyThat("#numberOfGamesKeyboardWarriorLabel", hasText(StatisticsController.INDENT + "Number of games: 0"));
        verifyThat("#avgScoreSharpshooterLabel", hasText(StatisticsController.INDENT + "Average score:"));
        verifyThat("#avgAccuracySharpshooterLabel", hasText(StatisticsController.INDENT + "Average accuracy:"));
        verifyThat("#numberOfGamesSharpshooterLabel", hasText(StatisticsController.INDENT + "Number of games: 0"));
        clickOn("#exitButton");
        clickOn("#exitButton");
        // set settings - length to 5 seconds, sharpshooter to 3 attempts
        clickOn("#settingsButton");
        moveTo("#gameLengthSlider > .thumb");
		press(MouseButton.PRIMARY);
		moveBy(-100, 0);
		release(MouseButton.PRIMARY);
        moveTo("#sharpshooterLengthSlider > .thumb");
		press(MouseButton.PRIMARY);
		moveBy(-100, 0);
		release(MouseButton.PRIMARY);
        clickOn("#saveButton");
        // now do 2 games: AimSniper and Sharpshooter
        clickOn("#exitButton");
        // enter game selection menu
        clickOn("#startButton");
        // enter AimSniper scene
        clickOn("#aimSniperButton");
        verifyThat("#messageLabel", hasText("3"));
        sleep(1, TimeUnit.SECONDS);
        verifyThat("#messageLabel", hasText("2"));
        sleep(1, TimeUnit.SECONDS);
        verifyThat("#messageLabel", hasText("1"));
        sleep(1, TimeUnit.SECONDS);
        sleep(100, TimeUnit.MILLISECONDS);
        verifyThat("#timeLeftValueLabel", hasText("5 seconds"));
        for(int i = 1; i <= 5; i++) {
            clickOn("#circle");
        }
        sleep(5, TimeUnit.SECONDS);
        verifyThat("#messageLabel", hasText("GAME OVER"));
        verifyThat("#scoreValueLabel", hasText("150"));
        verifyThat("#accuracyValueLabel", hasText("100.00%"));
        // check scoreboard
        clickOn("#exitButton");
        clickOn("#exitButton");
        clickOn("#scoreboardButton");
        clickOn("#statsButton");
        verifyThat("#avgScoreAimSniperLabel", hasText(StatisticsController.INDENT + "Average score: 150.00"));
        verifyThat("#avgAccuracyAimSniperLabel", hasText(StatisticsController.INDENT + "Average accuracy: 100.00%"));
        verifyThat("#numberOfGamesAimSniperLabel", hasText(StatisticsController.INDENT + "Number of games: 1"));
        verifyThat("#avgScoreKeyboardWarriorLabel", hasText(StatisticsController.INDENT + "Average score:"));
        verifyThat("#avgAccuracyKeyboardWarriorLabel", hasText(StatisticsController.INDENT + "Average accuracy:"));
        verifyThat("#numberOfGamesKeyboardWarriorLabel", hasText(StatisticsController.INDENT + "Number of games: 0"));
        verifyThat("#avgScoreSharpshooterLabel", hasText(StatisticsController.INDENT + "Average score:"));
        verifyThat("#avgAccuracySharpshooterLabel", hasText(StatisticsController.INDENT + "Average accuracy:"));
        verifyThat("#numberOfGamesSharpshooterLabel", hasText(StatisticsController.INDENT + "Number of games: 0"));
        clickOn("#exitButton");
        clickOn("#exitButton");

        // enter game selection menu
        clickOn("#startButton");
        // enter Sharpshooter scene
        clickOn("#sharpshooterButton");
        verifyThat("#messageLabel", hasText("3"));
        sleep(1, TimeUnit.SECONDS);
        verifyThat("#messageLabel", hasText("2"));
        sleep(1, TimeUnit.SECONDS);
        verifyThat("#messageLabel", hasText("1"));
        sleep(1100, TimeUnit.MILLISECONDS);
        verifyThat("#attemptsLeftLabel", hasText("3"));
        verifyThat("#scoreValueLabel", hasText("0"));
        verifyThat("#accuracyValueLabel", hasText("0.00%"));
        // hit three times
        for(int i = 1; i <= 3; i++) {
            assertTrue(tryToDo(query->clickOn(query), "#circle", 500, 30));
            verifyThat("#attemptsLeftLabel", hasText(String.format("%d", 3 - i)));
            verifyThat("#accuracyValueLabel", hasText("100.00%"));
        }
        verifyThat("#messageLabel", hasText("GAME OVER"));
        // check statistics
        clickOn("#exitButton");
        clickOn("#exitButton");
        clickOn("#scoreboardButton");
        clickOn("#statsButton");
        verifyThat("#avgScoreAimSniperLabel", hasText(StatisticsController.INDENT + "Average score: 150.00"));
        verifyThat("#avgAccuracyAimSniperLabel", hasText(StatisticsController.INDENT + "Average accuracy: 100.00%"));
        verifyThat("#numberOfGamesAimSniperLabel", hasText(StatisticsController.INDENT + "Number of games: 1"));
        verifyThat("#avgScoreKeyboardWarriorLabel", hasText(StatisticsController.INDENT + "Average score:"));
        verifyThat("#avgAccuracyKeyboardWarriorLabel", hasText(StatisticsController.INDENT + "Average accuracy:"));
        verifyThat("#numberOfGamesKeyboardWarriorLabel", hasText(StatisticsController.INDENT + "Number of games: 0"));
        // score may vary, don't check it
        verifyThat("#avgAccuracySharpshooterLabel", hasText(StatisticsController.INDENT + "Average accuracy: 100.00%"));
        verifyThat("#numberOfGamesSharpshooterLabel", hasText(StatisticsController.INDENT + "Number of games: 1"));
    }
}
