package z03.pap22z;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.input.KeyCode;

public class Alerts extends Alert {
    KeyCode lastKeyPressed = null;

    /**
     * Creates a JavaFX alert with style loaded from style.css.
     * 
     * @param alertType   the alert type
     * @param contentText the content text
     * @param buttons     the button types
     */
    private Alerts(AlertType alertType, String contentText, ButtonType... buttons) {
        super(alertType, contentText, buttons);
        DialogPane dialogPane = this.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("style.css").toExternalForm());
    }

    /**
     * Show a warning dialog.
     * 
     * @param text text to be shown on the dialog.
     */
    public static void warn(String text) {
        Alerts alert = new Alerts(AlertType.WARNING, text, ButtonType.OK);
        alert.showAndWait();
        MusicManager.playButtonSound();
    }

    /**
     * Show a dialog asking the user to confirm.
     * 
     * @param text text to be shown on the dialog.
     * @return the user's answer (OK - true, Cancel - false)
     */
    public static boolean confirm(String text) {
        Alerts alert = new Alerts(AlertType.CONFIRMATION, text, ButtonType.OK, ButtonType.CANCEL);
        alert.showAndWait();
        boolean result = alert.getResult() == ButtonType.OK;
        if (!result) {
            MusicManager.playButtonSound();
        }
        return result;
    }

    public static KeyCode getNewKey(String text) {
        Alerts alert = new Alerts(AlertType.CONFIRMATION, text, ButtonType.CANCEL);
        alert.getDialogPane().getScene().setOnKeyPressed(e -> {
            if (e.getCode().getName().length() == 1) {
                alert.lastKeyPressed = e.getCode();
                alert.close();
            }
        });
        alert.showAndWait();
        MusicManager.playButtonSound();
        return alert.lastKeyPressed;
    }
}
