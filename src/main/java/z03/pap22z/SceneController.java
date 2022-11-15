package z03.pap22z;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneController {
    protected Stage stage;
    protected Scene scene;
    protected Parent root;

    @FXML
    protected void switchToScene(ActionEvent event, String scene_name) throws IOException {
        root = FXMLLoader.load(getClass().getResource(scene_name + ".fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    protected void switchToSettings(ActionEvent event) throws IOException {
        switchToScene(event, "Settings");
    }

    @FXML
    protected void switchToMenu(ActionEvent event) throws IOException {
        switchToScene(event, "MainMenu");
    }
}
