package z03.pap22z;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import z03.pap22z.database.Database;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            Database.connect();
        }
        catch (Exception e) {
            System.out.println("Failed to connect to the MySQL database.");
            System.out.println("Will use default profile settings.");
            e.printStackTrace();
        }

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
            Database.closeConnection();
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop(){
        Database.closeConnection();
        System.exit(0);
    }
}
