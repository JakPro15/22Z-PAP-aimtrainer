package z03.pap22z;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;

import java.io.IOException;

public class SettingsController extends z03.pap22z.SceneController {
    public void initialize() {
        volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> source, Number oldValue, Number newValue) {
                volumeValueLabel.textProperty().setValue(String.format("%d%%", (int)volumeSlider.getValue()));
            }
        });
        volumeSlider.setValue(Settings.musicVolume);

        profileComboBox.getItems().addAll("default", "other", "another");
        profileComboBox.getSelectionModel().select("default");
    }

    @FXML
    private Slider volumeSlider;

    @FXML
    private Label volumeValueLabel;

    @FXML
    private ComboBox<String> profileComboBox;

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
        System.out.println(newProfile);
    }

    @FXML
    void handleDeleteProfile(ActionEvent event) {
        System.out.println("Delete Profile pressed");
    }

    @FXML
    void volumeSliderChanged(MouseEvent event) {
        System.out.println("Slider changed");
    }

    @FXML
    void profileSelected(ActionEvent event) {
        System.out.println(String.format("a profile has been set: %s", profileComboBox.getValue()));
    }
}
