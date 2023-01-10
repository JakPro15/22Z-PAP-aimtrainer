package z03.pap22z;

import static org.junit.Assert.assertTrue;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.LabeledMatchers.hasText;
import static org.testfx.matcher.control.TableViewMatchers.containsRowAtIndex;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;

import javafx.scene.input.MouseButton;
import z03.pap22z.database.Database;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ResultSavingTest extends ApplicationTest {
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
    public void testAimSniperSharpshooter() {
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
        sleep(1, TimeUnit.SECONDS);
        LocalDateTime gameEnd = LocalDateTime.now().plusSeconds(5);
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
        // round now to full seconds
        if(gameEnd.getNano() >= 500_000_000) {
            gameEnd = gameEnd.plusSeconds(1);
        }
        gameEnd = gameEnd.withNano(0);
        clickOn("#exitButton");
        clickOn("#exitButton");
        clickOn("#scoreboardButton");
        String gameEndFormatted = gameEnd.format(StringResult.dateFormatter);
        verifyThat(
            "#resultsTable",
            containsRowAtIndex(0, gameEndFormatted, "default", "AimSniper",
                               "Normal", "5 seconds", "150", "100.00%")
        );
    }
}
