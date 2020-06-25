package app.Controller;

import app.Other.DialogBox;
import app.Other.Loaders;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

import static app.Controller.LeagueController.MODAL_WINDOWS_STYLE;

public class ManageController {
    private static final String MODIFY_LEAGUE_FXML = "/resources/FXML/LeagueOperation.fxml";
    private static final String MODIFY_SEASON_FXML = "/resources/FXML/SeasonOperation.fxml";
    private static final String MODIFY_COUNTRY_FXML = "/resources/FXML/CountryOperation.fxml";

    private void openModifyDialog(String FXML_DIALOG_PATH, String stageTitle) {
        FXMLLoader fxmlLoader = Loaders.getFXMLloader(FXML_DIALOG_PATH);
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            DialogBox.ExceptionDialog(e.getMessage());
        }
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        Objects.requireNonNull(scene).getStylesheets().add(MODAL_WINDOWS_STYLE);
        stage.setScene(scene);
        stage.setTitle(stageTitle);
        stage.showAndWait();
    }

    @FXML
    private void openLeagueModifyDialog() {
        this.openModifyDialog(MODIFY_LEAGUE_FXML, Loaders.getResourceBundle().getString("Manage.League.StageTitle"));
    }

    @FXML
    private void openSeasonModifyDialog() {
        this.openModifyDialog(MODIFY_SEASON_FXML, Loaders.getResourceBundle().getString("Manage.Season.StageTitle"));
    }

    @FXML
    private void openCountryModifyDialog() {
        this.openModifyDialog(MODIFY_COUNTRY_FXML, Loaders.getResourceBundle().getString("Manage.Country.StageTitle"));
    }
}
