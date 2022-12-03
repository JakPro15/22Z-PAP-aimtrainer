package z03.pap22z;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;

import java.io.IOException;

public class SettingsController extends z03.pap22z.SceneController {
    @FXML
    private Slider volumeSlider;

    @FXML
    private Label volumeValueLabel;

    @FXML
    private ComboBox<String> profileComboBox;

    /**
     * Initialize the settings UI.
     */
    public void initialize() {
        volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> source, Number oldValue, Number newValue) {
                volumeValueLabel.textProperty().setValue(String.format("%d%%", (int)volumeSlider.getValue()));
                Settings.setMusicVolume((int)volumeSlider.getValue());
            }
        });
        profileListChanged();
    }

    @FXML
    protected void handleExit(ActionEvent event) {
        try {
            switchToMenu(event);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void handleSave(ActionEvent event) {
        System.out.println("Save pressed");
    }

    @FXML
    protected void handleNewProfile(ActionEvent event) {
        String newProfile = NewProfileDialogController.getNewProfile(stage);
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
        volumeSlider.setValue(Settings.getMusicVolume());
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
