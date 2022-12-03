package z03.pap22z;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;

public class Alerts extends Alert {
    private Alerts(AlertType alertType, String contentText, ButtonType... buttons) {
        super(alertType, contentText, buttons);
        DialogPane dialogPane = this.getDialogPane();
        dialogPane.getStylesheets().add(
            getClass().getResource("style.css").toExternalForm()
        );
    }

    /**
     * Show a warning dialog.
     *
     * @param text text to be shown on the dialog.
     */
    public static void warn(String text) {
        Alerts alert = new Alerts(AlertType.WARNING, text, ButtonType.OK);
        alert.showAndWait();
    }

    /**
     * Show a dialog asking the user to confirm.
     *
     * @param text text to be shown on the dialog.
     *
     * @return the user's answer (OK - true, Cancel - false)
     */
    public static boolean confirm(String text) {
        Alerts alert = new Alerts(AlertType.CONFIRMATION, text, ButtonType.OK, ButtonType.CANCEL);
        alert.showAndWait();
        return alert.getResult() == ButtonType.OK;
    }
}
