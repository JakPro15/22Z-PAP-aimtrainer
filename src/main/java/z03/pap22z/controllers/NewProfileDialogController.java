package z03.pap22z.controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class NewProfileDialogController extends SceneController {
    Stage stage;

    @FXML
    TextField textField;

    @FXML
    void handleEnterPress(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            stage.close();
        }
    }

    @FXML
    void handleOK(ActionEvent event) {
        stage.close();
    }

    @FXML
    void handleCancel(ActionEvent event) {
        textField.setText("");
        stage.close();
    }

    /**
     * Shows a dialog for the user to input the name of the profile to be created.
     * @param parentStage stage that calls the dialog (for modality to work)
     * @return new profile name inputted by the user
     */
    public static String getNewProfile(Stage parentStage) {
        Stage dialogStage = new Stage();
        dialogStage.setTitle("New profile...");
        dialogStage.initOwner(parentStage);
        dialogStage.initModality(Modality.WINDOW_MODAL);

        FXMLLoader loader = new FXMLLoader(
                NewProfileDialogController.class.getResource(FXML_PATH + "NewProfileDialog.fxml"));
        Parent root;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        NewProfileDialogController controller = loader.getController();
        controller.stage = dialogStage;

        dialogStage.setScene(new Scene(root));
        dialogStage.showAndWait();

        String newProfile = controller.textField.getText();
        if (newProfile.equals("")) {
            return null;
        } else {
            return newProfile;
        }
    }
}
