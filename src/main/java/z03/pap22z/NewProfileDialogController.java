package z03.pap22z;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class NewProfileDialogController {
    Stage stage;

    @FXML
    TextField textField;

    @FXML
    void handleOK(ActionEvent event) {
        stage.close();
    }

    @FXML
    void handleCancel(ActionEvent event) {
        textField.setText("");
        stage.close();
    }

    public static String getNewProfile(Stage parentStage) {
        Stage dialogStage = new Stage();
        dialogStage.initOwner(parentStage);
        dialogStage.initModality(Modality.WINDOW_MODAL);

        FXMLLoader loader = new FXMLLoader(NewProfileDialogController.class.getResource("NewProfileDialog.fxml"));
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
        if(newProfile.equals("")) {
            return null;
        }
        else {
            return newProfile;
        }
    }
}
