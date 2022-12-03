package z03.pap22z;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import z03.pap22z.database.Results;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println(Results.ENTITY_MANAGER_FACTORY);
        try {
            Parent root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
            primaryStage.setMinHeight(600);
            primaryStage.setMinWidth(800);
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

    @Override
    public void stop(){
        System.exit(0);
    }
}
