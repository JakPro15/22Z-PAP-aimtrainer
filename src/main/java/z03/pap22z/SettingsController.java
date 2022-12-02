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

    public void initialize() {
        System.out.println("initialized");
        volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> source, Number oldValue, Number newValue) {
                volumeValueLabel.textProperty().setValue(String.format("%d%%", (int)volumeSlider.getValue()));
                Settings.setMusicVolume((int)volumeSlider.getValue());
            }
        });
        Settings.setSettingsController(this);
        profileListChanged();
        update();
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
    void handleSave(ActionEvent event) {
        System.out.println("Save pressed");
    }

    @FXML
    void handleNewProfile(ActionEvent event) {
        String newProfile = NewProfileDialogController.getNewProfile(stage);
        if(Settings.getProfileNames().contains(newProfile)) {
            Warning.warn(String.format("Profile %s already exists.", newProfile));
        }
        else {
            Settings.addNewProfile(newProfile);
        }
    }

    @FXML
    void handleDeleteProfile(ActionEvent event) {
        if(Settings.getProfileNames().size() == 1) {
            Warning.warn("You cannot delete the only profile.");
        }
        else {
            Settings.deleteProfile(Settings.getCurrentProfile());
        }
    }

    @FXML
    void profileSelected(ActionEvent event) {
        if (profileComboBox.getValue() != null) {
            Settings.setCurrentProfile(profileComboBox.getValue());
        }
    }

    public void update() {
        volumeSlider.setValue(Settings.getMusicVolume());
    }

    public void profileListChanged() {
        profileComboBox.setItems(FXCollections.observableArrayList(Settings.getProfileNames()));
        profileComboBox.getSelectionModel().select(Settings.getCurrentProfile());
    }
}
