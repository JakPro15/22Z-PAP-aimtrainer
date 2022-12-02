package z03.pap22z;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;

public class Warning extends Alert {
    public Warning(String text) {
        super(AlertType.WARNING, text, ButtonType.OK);
        DialogPane dialogPane = this.getDialogPane();
        dialogPane.getStylesheets().add(
            getClass().getResource("style.css").toExternalForm()
        );
    }

    public static void warn(String text) {
        Warning warning = new Warning(text);
        warning.showAndWait();
    }
}
