package z03.pap22z;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            primaryStage.setResizable(false);
            Parent root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
            Scene scene = new Scene(root, 800, 600);
            primaryStage.setTitle("AimTrainer");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
