package app.Other;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.ResourceBundle;

public class Loaders {

    public static FXMLLoader getFXMLloader(String fxmlPath) {
        FXMLLoader loader = new FXMLLoader(Loaders.class.getResource(fxmlPath));
        loader.setResources(getResourceBundle());
        return loader;
    }

    public static Pane getPanefxmlLoader(String fxmlPath) {
        FXMLLoader loader = new FXMLLoader(Loaders.class.getResource(fxmlPath));
        loader.setResources(getResourceBundle());
        try {
            return loader.load();
        } catch (IOException e) {
            DialogBox.ExceptionDialog(e.getMessage());
        }
        return null;
    }

    public static ResourceBundle getResourceBundle() {
        return ResourceBundle.getBundle("resources/bundles/texts");
    }

}
