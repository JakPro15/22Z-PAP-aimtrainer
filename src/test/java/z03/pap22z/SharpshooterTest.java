package z03.pap22z;

import static org.junit.Assert.assertTrue;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.LabeledMatchers.hasText;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.testfx.api.FxRobotException;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;

import javafx.scene.input.MouseButton;
import z03.pap22z.database.Database;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

interface TestFXQueryFunction {
    void run(String query);
}

public class SharpshooterTest extends ApplicationTest {
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

    /**
     * Try to click on a node, up to attempts times with the given delay.
     *
     * @param query query to find the node to click on
     * @param delayMillis delay in milliseconds
     * @param attempts max number of attempts
     * @return true if clicked, false if didn't
     */
    private boolean tryToDo(TestFXQueryFunction runnable, String query, int delayMillis, int attempts) {
        while(attempts > 0) {
            try {
                runnable.run(query);
                return true;
            }
            catch(FxRobotException e) {}
            --attempts;
            sleep(delayMillis);
        }
        return false;
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
        sleep(10, TimeUnit.SECONDS);
        verifyThat("#attemptsLeftLabel", hasText("9"));
        verifyThat("#scoreValueLabel", hasText("0"));
        // verifyThat("#accuracyValueLabel", hasText("0.00%"));
        // click on circle twice
        assertTrue(tryToDo(query->clickOn(query), "#circle", 500, 16));
        verifyThat("#attemptsLeftLabel", hasText("8"));
        // score depends on how fast the robot clicked, don't check it
        // verifyThat("#accuracyValueLabel", hasText("50.00%"));

        assertTrue(tryToDo(query->clickOn(query), "#circle", 500, 16));
        verifyThat("#attemptsLeftLabel", hasText("7"));
        // verifyThat("#accuracyValueLabel", hasText("66.67%"));

        // now miss a click
        assertTrue(tryToDo(query->moveTo(query), "#circle", 500, 16));
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
            assertTrue(tryToDo(query->clickOn(query), "#circle", 500, 16));
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
        assertTrue(tryToDo(query->clickOn(query), "#circle", 500, 16));
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
