package app.Controller;

import app.Other.DialogBox;
import app.Other.Loaders;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuBar;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

public class BorderPaneMainController {
    private static final String VIEW_AFTER_OPENING_APP = "/resources/FXML/League.fxml";
    @FXML
    private MenuBar myMenuBar;
    @FXML
    private Button maxMinWindowButton;
    @FXML
    private app.Controller.MenuButtonController menuButtonController = null;
    @FXML
    private BorderPane borderPane;
    @FXML
    private RadioMenuItem englishRadioItem;
    @FXML
    private RadioMenuItem polishRadioItem;
    private ImageView maxImageView = new ImageView("/icons/max.png");
    private ImageView minImageView = new ImageView("/icons/restore-window.png");

    @FXML
    private void initialize() {
        menuButtonController.setBorderPaneMainController(this);
        this.setCenter(VIEW_AFTER_OPENING_APP); //View after opening the application
        setRadioItem();
    }

    void setCenter(String FxmlPath) {
        borderPane.setCenter(Loaders.getPanefxmlLoader(FxmlPath));
    }

    public void closeApplication() {
        Optional<ButtonType> result = DialogBox.confirmationExitDialog(Loaders.getResourceBundle().getString("exit.title"), Loaders.getResourceBundle().getString("exit.header"));
        if (result.get() == ButtonType.OK) {
            Platform.exit();
            System.exit(0);
        }
    }

    public void setEnglishLanguage() {
        Locale.setDefault(new Locale("en"));
        reloadStageOnLanguageChange();
    }

    public void setPolishLanguage() {
        Locale.setDefault(new Locale("pl"));
        reloadStageOnLanguageChange();
    }

    private void reloadStageOnLanguageChange() {
        Stage stage = (Stage) myMenuBar.getScene().getWindow();
        Pane borderPane = Loaders.getPanefxmlLoader("/resources/FXML/BorderPaneMain.fxml");
        Scene scene = new Scene(Objects.requireNonNull(borderPane));
        scene.getStylesheets().add("/app/Other/css/MainStyle.css");
        stage.setScene(scene);
        menuButtonController.setBorderPaneMainController(this);
    }

    private void setRadioItem() {
        if (Locale.getDefault().toString().compareTo("en") == 0)
            englishRadioItem.setSelected(true);
        else if (Locale.getDefault().toString().compareTo("pl") == 0)
            polishRadioItem.setSelected(true);
    }

    public void about() {
        DialogBox.dialogAboutApplications();
    }


    public void close(MouseEvent mouseEvent) {
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    public void max(MouseEvent mouseEvent) {
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        maxImageView.setFitHeight(20);
        maxImageView.setFitWidth(20);
        minImageView.setFitHeight(20);
        minImageView.setFitWidth(20);

        if (stage.isFullScreen()) {
            maxMinWindowButton.setGraphic(maxImageView);
            stage.setFullScreen(false);
        } else {
            maxMinWindowButton.setGraphic(minImageView);
            stage.setFullScreen(true);
        }

    }

    public void min(MouseEvent mouseEvent) {
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

}



