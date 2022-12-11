package z03.pap22z;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.LabeledMatchers.hasText;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;

import javafx.scene.input.MouseButton;
import z03.pap22z.database.Database;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AimSniperTest extends ApplicationTest {
    @Before
    public void setup() throws Exception {
        FxToolkit.registerPrimaryStage();
        FxToolkit.setupApplication(App.class);
        Database.closeConnection();  // no changes to the database
    }

    @After
    public void cleanup() throws TimeoutException {
        FxToolkit.hideStage();
    }

    @Test
    public void testCircleClicking() {
        // settings are not modified
        // enter game selection menu
        clickOn("#startButton");
        // enter AimSniper scene
        clickOn("#aimSniperButton");
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
        // click on circle twice
        clickOn("#circle");
        verifyThat("#scoreValueLabel", hasText("10"));
        verifyThat("#accuracyValueLabel", hasText("100.00%"));
        clickOn("#circle");
        verifyThat("#scoreValueLabel", hasText("30"));
        verifyThat("#accuracyValueLabel", hasText("100.00%"));
        // now miss a click
        moveTo("#circle");
        moveBy(0, 16);
        press(MouseButton.PRIMARY);
        release(MouseButton.PRIMARY);
        verifyThat("#scoreValueLabel", hasText("30"));
        verifyThat("#accuracyValueLabel", hasText("66.67%"));
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
        // enter AimSniper scene
        clickOn("#aimSniperButton");
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
        // enter AimSniper scene
        clickOn("#aimSniperButton");
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
        // click on circle once
        clickOn("#circle");
        verifyThat("#scoreValueLabel", hasText("10"));
        verifyThat("#accuracyValueLabel", hasText("100.00%"));
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
