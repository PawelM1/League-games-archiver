package app.Controller;

import javafx.fxml.FXML;

public class MenuButtonController {
    private static final String LEAGUE_FXML = "/resources/FXML/League.fxml";
    private static final String Manage_FXML = "/resources/FXML/Manage.fxml";
    private static final String GAMES_FXML = "/resources/FXML/Games.fxml";
    private BorderPaneMainController borderPaneMainController;

    @FXML
    public void openLeagueView() {
        borderPaneMainController.setCenter(LEAGUE_FXML);
    }

    @FXML
    public void openManageView() {
        borderPaneMainController.setCenter(Manage_FXML);
    }

    @FXML
    void openGamesView() {
        borderPaneMainController.setCenter(GAMES_FXML);
    }

    void setBorderPaneMainController(BorderPaneMainController borderPaneMainController) {
        this.borderPaneMainController = borderPaneMainController;
    }
}
