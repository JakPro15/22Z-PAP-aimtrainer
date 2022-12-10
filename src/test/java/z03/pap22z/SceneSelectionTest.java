package z03.pap22z;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.LabeledMatchers.hasText;

import java.util.concurrent.TimeoutException;

import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SceneSelectionTest extends ApplicationTest {
    @Before
    public void setup() throws Exception {
        FxToolkit.registerPrimaryStage();
        FxToolkit.setupApplication(App.class);
    }

    @After
    public void cleanup() throws TimeoutException {
        FxToolkit.hideStage();
    }

    @Test
    public void test_select_game() {
        // enter game selection menu
        verifyThat("#startButton", hasText("Play"));
        clickOn("#startButton");
        verifyThat("#aimSniperButton", hasText("Aim Sniper"));
        // enter AimSniper scene
        clickOn("#aimSniperButton");
        verifyThat("#timeLeftTextLabel", hasText("Time left:"));
        verifyThat("#scoreTextLabel", hasText("Score:"));
        verifyThat("#accuracyTextLabel", hasText("Accuracy:"));
        verifyThat("#exitButton", hasText("Back to game selection"));
        // go back to scene selection
        clickOn("#exitButton");
        verifyThat("#exitButton", hasText("Back"));
        // go back to main menu
        clickOn("#exitButton");
        verifyThat("#startButton", hasText("Play"));
    }

    @Test
    public void test_scoreboard() {
        verifyThat("#scoreboardButton", hasText("Scoreboard"));
        clickOn("#scoreboardButton");
        // enter scoreboard scene
        verifyThat("#allResultsButton", hasText("View all results"));
        verifyThat("#myResultsButton", hasText("View your results"));
        verifyThat("#exitButton", hasText("Exit"));
        // go back to main menu
        clickOn("#exitButton");
        verifyThat("#scoreboardButton", hasText("Scoreboard"));
    }

    @Test
    public void test_settings() {
        verifyThat("#settingsButton", hasText("Settings"));
        clickOn("#settingsButton");
        // enter settings scene
        verifyThat("#newProfileButton", hasText("New Profile"));
        verifyThat("#deleteProfileButton", hasText("Delete Profile"));
        verifyThat("#saveButton", hasText("Save"));
        verifyThat("#exitButton", hasText("Exit"));
        // go back to main menu
        clickOn("#exitButton");
        verifyThat("#settingsButton", hasText("Settings"));
    }
}
