package app;

import app.DAO.HibernateUtil;
import app.Other.Loaders;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Locale;
import java.util.Objects;

public class Main extends Application {

    private static final String FXML_BORDER_PANE_MAIN_FXML = "/resources/FXML/BorderPaneMain.fxml";
    private static final String APP_STYLE = "/app/Other/css/MainStyle.css";

    public static void main(String[] args) {
        HibernateUtil.OpenConnection();
        launch(args);
        HibernateUtil.CloseConnection();
    }

    @Override
    public void start(Stage primaryStage) {
        Locale.setDefault(new Locale("pl"));

        Pane borderPane = Loaders.getPanefxmlLoader(FXML_BORDER_PANE_MAIN_FXML);
        Scene scene = new Scene(Objects.requireNonNull(borderPane));
        scene.getStylesheets().add(APP_STYLE);
        primaryStage.setScene(scene);
        this.setStageStyle(primaryStage);
        primaryStage.show();
    }

    private void setStageStyle(Stage stage) {
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setTitle(Loaders.getResourceBundle().getString("tittle.application"));
        stage.getIcons().add(new Image("/icons/stageicon.png"));
    }
}