package z03.pap22z;

import static java.lang.Math.round;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;

import java.io.IOException;
import java.util.Arrays;

public class SettingsController extends z03.pap22z.SceneController {
    @FXML
    private Slider musicVolumeSlider;
    @FXML
    private Label musicVolumeValueLabel;
    @FXML
    private Slider sfxVolumeSlider;
    @FXML
    private Label sfxVolumeValueLabel;
    @FXML
    private Slider gameSpeedSlider;
    @FXML
    private Label gameSpeedValueLabel;
    @FXML
    private Slider gameLengthSlider;
    @FXML
    private Label gameLengthValueLabel;
    @FXML
    private ComboBox<String> profileComboBox;

    /**
     * Initialize the settings UI.
     */
    public void initialize() {
        // Bind the slider labels to the sliders
        musicVolumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> source, Number oldValue, Number newValue) {
                musicVolumeValueLabel.textProperty().setValue(String.format("%d%%", (int)musicVolumeSlider.getValue()));
                Settings.setMusicVolume((int)musicVolumeSlider.getValue());
            }
        });
        sfxVolumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> source, Number oldValue, Number newValue) {
                sfxVolumeValueLabel.textProperty().setValue(String.format("%d%%", (int)sfxVolumeSlider.getValue()));
                Settings.setSfxVolume((int)sfxVolumeSlider.getValue());
            }
        });
        gameSpeedSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> source, Number oldValue, Number newValue) {
                Double gameSpeed = Settings.VALID_GAME_SPEEDS[(int) round(gameSpeedSlider.getValue())];
                gameSpeedValueLabel.textProperty().setValue(String.format("%.2fx", gameSpeed));
                Settings.setGameSpeed(gameSpeed);
            }
        });
        gameLengthSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> source, Number oldValue, Number newValue) {
                Integer gameLength = (int) round(gameLengthSlider.getValue());
                gameLengthValueLabel.textProperty().setValue(String.format("%d seconds", gameLength));
                Settings.setGameLength(gameLength);
            }
        });
        profileListChanged();
    }

    @FXML
    protected void handleExit(ActionEvent event) {
        Settings.readFromDatabase();
        try {
            switchToMenu(event);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void handleSave(ActionEvent event) {
        Settings.writeToDatabase();
    }

    @FXML
    protected void handleNewProfile(ActionEvent event) {
        String newProfile = NewProfileDialogController.getNewProfile(stage);
        if(newProfile.length() > 40) {
            Alerts.warn(String.format("Profile name %s is too long.", newProfile));
        }
        if(Settings.getProfileNames().contains(newProfile)) {
            Alerts.warn(String.format("Profile %s already exists.", newProfile));
        }
        else {
            Settings.addNewProfile(newProfile);
            profileListChanged();
        }
    }

    @FXML
    protected void handleDeleteProfile(ActionEvent event) {
        if(Settings.getProfileNames().size() == 1) {
            Alerts.warn("You cannot delete the only profile.");
        }
        else {
            if(Alerts.confirm(String.format("Are you sure you want to delete %s profile?",
                                            Settings.getCurrentProfile()))) {
                Settings.deleteProfile(Settings.getCurrentProfile());
                profileListChanged();
            }
        }
    }

    @FXML
    protected void profileSelected(ActionEvent event) {
        if (profileComboBox.getValue() != null) {
            Settings.setCurrentProfile(profileComboBox.getValue());
            update();
        }
    }

    /**
     * Updates the settings UI with the current Settings values.
     */
    private void update() {
        musicVolumeSlider.setValue(Settings.getMusicVolume());
        sfxVolumeSlider.setValue(Settings.getSfxVolume());
        gameSpeedSlider.setValue(Arrays.asList(Settings.VALID_GAME_SPEEDS).indexOf(Settings.getGameSpeed()));
        gameLengthSlider.setValue(Settings.getGameLength());
    }

    /**
     * Updates the settings UI to the current list of profiles. Also calls update().
     */
    private void profileListChanged() {
        profileComboBox.setItems(FXCollections.observableArrayList(Settings.getProfileNames()));
        profileComboBox.getSelectionModel().select(Settings.getCurrentProfile());
        update();
    }
}
