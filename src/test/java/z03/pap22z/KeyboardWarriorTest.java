package z03.pap22z;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.LabeledMatchers.hasText;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;

import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import z03.pap22z.database.Database;

public class KeyboardWarriorTest extends ApplicationTest{
    @Before
    public void setup() throws Exception {
        FxToolkit.registerPrimaryStage();
        FxToolkit.setupApplication(App.class);
        Database.closeConnection();  // no changes to the database
    }

    @After
    public void cleanup() throws TimeoutException {
        Settings.initialize();
        FxToolkit.hideStage();
    }

    @Test
    public void testSquarePressing() {
        // settings are not modified
        // enter game selection menu
        clickOn("#startButton");
        // enter KeyboardWarrior scene
        clickOn("#keyboardWarriorButton");
        verifyThat("#messageLabel", hasText("3"));
        sleep(1, TimeUnit.SECONDS);
        verifyThat("#messageLabel", hasText("2"));
        sleep(1, TimeUnit.SECONDS);
        verifyThat("#messageLabel", hasText("1"));
        sleep(1100, TimeUnit.MILLISECONDS);
        verifyThat("#timeLeftValueLabel", hasText("20 seconds"));
        sleep(1, TimeUnit.SECONDS);
        verifyThat("#timeLeftValueLabel", hasText("19 seconds"));
        verifyThat("#scoreValueLabel", hasText("0"));
        verifyThat("#accuracyValueLabel", hasText("0.00%"));
        // wait until square falls down
        sleep(1800, TimeUnit.MILLISECONDS);
        // press all options twice
        press(KeyCode.A, KeyCode.S, KeyCode.K, KeyCode.L);
        release(KeyCode.A, KeyCode.S, KeyCode.K, KeyCode.L);
        verifyThat("#scoreValueLabel", hasText("10"));
        verifyThat("#accuracyValueLabel", hasText("25.00%"));
        sleep(250, TimeUnit.MILLISECONDS);
        press(KeyCode.A, KeyCode.S, KeyCode.K, KeyCode.L);
        release(KeyCode.A, KeyCode.S, KeyCode.K, KeyCode.L);
        verifyThat("#scoreValueLabel", hasText("20"));
        verifyThat("#accuracyValueLabel", hasText("25.00%"));
        // miss a square
        sleep(1100, TimeUnit.MILLISECONDS);
        verifyThat("#scoreValueLabel", hasText("20"));
        verifyThat("#accuracyValueLabel", hasText("22.22%"));
    }

    @Test
    public void testShortTime() {
        // set settings - length to 5 seconds
        clickOn("#settingsButton");
        moveTo("#gameLengthSlider > .thumb");
		press(MouseButton.PRIMARY);
		moveBy(-100, 0);
		release(MouseButton.PRIMARY);
        clickOn("#saveButton");
        clickOn("#exitButton");
        // enter game selection menu
        clickOn("#startButton");
        // enter KeyboardWarrior scene
        clickOn("#keyboardWarriorButton");
        verifyThat("#messageLabel", hasText("3"));
        sleep(1, TimeUnit.SECONDS);
        verifyThat("#messageLabel", hasText("2"));
        sleep(1, TimeUnit.SECONDS);
        verifyThat("#messageLabel", hasText("1"));
        sleep(1100, TimeUnit.MILLISECONDS);
        verifyThat("#timeLeftValueLabel", hasText("5 seconds"));
        for(int i = 1; i <= 5; i++) {
            sleep(1, TimeUnit.SECONDS);
            verifyThat("#timeLeftValueLabel", hasText(String.format("%d seconds", 5 - i)));
        }
        verifyThat("#messageLabel", hasText("GAME OVER"));
    }

    @Test
    public void testNewGame() {
        // set settings - length to 5 seconds
        clickOn("#settingsButton");
        moveTo("#gameLengthSlider > .thumb");
		press(MouseButton.PRIMARY);
		moveBy(-100, 0);
		release(MouseButton.PRIMARY);
        clickOn("#saveButton");
        clickOn("#exitButton");
        // enter game selection menu
        clickOn("#startButton");
        // enter KeyboardWarrior scene
        clickOn("#keyboardWarriorButton");
        verifyThat("#messageLabel", hasText("3"));
        sleep(1, TimeUnit.SECONDS);
        verifyThat("#messageLabel", hasText("2"));
        sleep(1, TimeUnit.SECONDS);
        verifyThat("#messageLabel", hasText("1"));
        sleep(1100, TimeUnit.MILLISECONDS);
        verifyThat("#timeLeftValueLabel", hasText("5 seconds"));
        sleep(1, TimeUnit.SECONDS);
        verifyThat("#timeLeftValueLabel", hasText("4 seconds"));
        verifyThat("#scoreValueLabel", hasText("0"));
        verifyThat("#accuracyValueLabel", hasText("0.00%"));
        // wait until square falls down
        sleep(1800, TimeUnit.MILLISECONDS);
        // press all options twice
        press(KeyCode.A, KeyCode.S, KeyCode.K, KeyCode.L);
        release(KeyCode.A, KeyCode.S, KeyCode.K, KeyCode.L);
        verifyThat("#scoreValueLabel", hasText("10"));
        verifyThat("#accuracyValueLabel", hasText("25.00%"));
        // reset the game
        clickOn("#newGameButton");
        verifyThat("#messageLabel", hasText("3"));
        sleep(1, TimeUnit.SECONDS);
        verifyThat("#messageLabel", hasText("2"));
        sleep(1, TimeUnit.SECONDS);
        verifyThat("#messageLabel", hasText("1"));
        sleep(1100, TimeUnit.MILLISECONDS);
        verifyThat("#timeLeftValueLabel", hasText("5 seconds"));
        verifyThat("#scoreValueLabel", hasText("0"));
        verifyThat("#accuracyValueLabel", hasText("0.00%"));
    }
}
