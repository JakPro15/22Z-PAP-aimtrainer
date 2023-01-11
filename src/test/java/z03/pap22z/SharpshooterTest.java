package z03.pap22z;

import static org.junit.Assert.assertTrue;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.LabeledMatchers.hasText;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.testfx.api.FxToolkit;

import javafx.scene.input.MouseButton;
import z03.pap22z.database.Database;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class SharpshooterTest extends BaseTestFXTest {
    @Before
    public void setup() throws Exception {
        FxToolkit.registerPrimaryStage();
        FxToolkit.setupApplication(App.class);
        Database.closeConnection();  // no changes to the database
    }

    @After
    public void cleanup() throws TimeoutException {
        FxToolkit.hideStage();
        Settings.initialize();
    }

    @Test
    public void testCircleClicking() {
        // settings are not modified
        // enter game selection menu
        clickOn("#startButton");
        // enter AimSniper scene
        clickOn("#sharpshooterButton");
        verifyThat("#messageLabel", hasText("3"));
        sleep(1, TimeUnit.SECONDS);
        verifyThat("#messageLabel", hasText("2"));
        sleep(1, TimeUnit.SECONDS);
        verifyThat("#messageLabel", hasText("1"));
        sleep(1100, TimeUnit.MILLISECONDS);
        verifyThat("#attemptsLeftTextLabel", hasText("Attempts left:"));
        verifyThat("#attemptsLeftLabel", hasText("10"));

        // miss the circle once
        assertTrue(tryToDo(query->moveTo(query), "#circle", 500, 30));
        sleep(4, TimeUnit.SECONDS);
        verifyThat("#attemptsLeftLabel", hasText("9"));
        verifyThat("#scoreValueLabel", hasText("0"));
        // verifyThat("#accuracyValueLabel", hasText("0.00%"));
        // click on circle twice
        assertTrue(tryToDo(query->clickOn(query), "#circle", 500, 30));
        verifyThat("#attemptsLeftLabel", hasText("8"));
        // score depends on how fast the robot clicked, don't check it
        // verifyThat("#accuracyValueLabel", hasText("50.00%"));

        assertTrue(tryToDo(query->clickOn(query), "#circle", 500, 30));
        verifyThat("#attemptsLeftLabel", hasText("7"));
        // verifyThat("#accuracyValueLabel", hasText("66.67%"));

        // now miss a click
        assertTrue(tryToDo(query->moveTo(query), "#circle", 500, 30));
        moveBy(0, 16);
        press(MouseButton.PRIMARY);
        release(MouseButton.PRIMARY);
        verifyThat("#attemptsLeftLabel", hasText("6"));
        // verifyThat("#accuracyValueLabel", hasText("50.00%"));
    }

    @Test
    public void test3Attempts() {
        // set settings - length to 5 seconds
        clickOn("#settingsButton");
        moveTo("#sharpshooterLengthSlider > .thumb");
		press(MouseButton.PRIMARY);
		moveBy(-100, 0);
		release(MouseButton.PRIMARY);
        clickOn("#saveButton");
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
        for(int i = 1; i <= 3; i++) {
            assertTrue(tryToDo(query->clickOn(query), "#circle", 500, 30));
            verifyThat("#attemptsLeftLabel", hasText(String.format("%d", 3 - i)));
            verifyThat("#accuracyValueLabel", hasText("100.00%"));
        }
        verifyThat("#messageLabel", hasText("GAME OVER"));
    }

    @Test
    public void testNewGame() {
        // set settings - length to 5 seconds
        clickOn("#settingsButton");
        moveTo("#sharpshooterLengthSlider > .thumb");
		press(MouseButton.PRIMARY);
		moveBy(-100, 0);
		release(MouseButton.PRIMARY);
        clickOn("#saveButton");
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
        // click on circle once
        assertTrue(tryToDo(query->clickOn(query), "#circle", 500, 30));
        verifyThat("#attemptsLeftLabel", hasText("2"));
        verifyThat("#accuracyValueLabel", hasText("100.00%"));
        // reset the game
        clickOn("#newGameButton");
        verifyThat("#messageLabel", hasText("3"));
        sleep(1, TimeUnit.SECONDS);
        verifyThat("#messageLabel", hasText("2"));
        sleep(1, TimeUnit.SECONDS);
        verifyThat("#messageLabel", hasText("1"));
        sleep(1100, TimeUnit.MILLISECONDS);
        verifyThat("#attemptsLeftLabel", hasText("3"));
        verifyThat("#scoreValueLabel", hasText("0"));
        verifyThat("#accuracyValueLabel", hasText("0.00%"));
    }
}
