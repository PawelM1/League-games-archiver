package app.Other;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextArea;

import java.util.Optional;
import java.util.ResourceBundle;

public class DialogBox {
    private static ResourceBundle resourceBundle = Loaders.getResourceBundle();

    public static void dialogAboutApplications() {
        Alert informationAlert = new Alert(Alert.AlertType.INFORMATION);
        setDialogStyle(informationAlert);
        informationAlert.setTitle(resourceBundle.getString("about.title"));
        informationAlert.setHeaderText(resourceBundle.getString("about.header"));
        informationAlert.setContentText(resourceBundle.getString("about.content"));
        informationAlert.showAndWait();
    }

    public static Optional<ButtonType> confirmationExitDialog(String Title, String header) {
        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        setDialogStyle(confirmationDialog);
        confirmationDialog.setTitle(Title);
        confirmationDialog.setHeaderText(header);
        return confirmationDialog.showAndWait();
    }

    public static void ExceptionDialog(String exception) {
        Alert exceptionAlert = new Alert(Alert.AlertType.ERROR);
        setDialogStyle(exceptionAlert);
        exceptionAlert.setTitle(resourceBundle.getString("exception.title"));
        exceptionAlert.setHeaderText(resourceBundle.getString("exception.header"));
        TextArea tmp = new TextArea(exception);
        exceptionAlert.getDialogPane().setContent(tmp);

        exceptionAlert.showAndWait();
    }

    private static void setDialogStyle(Alert alert) {
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                DialogBox.class.getResource("/app/Other/css/Dialog.css").toExternalForm());
        dialogPane.getStyleClass().add("Dialog");
    }
}
