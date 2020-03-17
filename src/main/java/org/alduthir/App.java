package org.alduthir;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {
    private static Scene scene;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("gui/songScreen.fxml"), 800, 400);
        stage.setMinHeight(300);
        stage.setMinWidth(800);
        stage.setScene(scene);
        stage.setTitle("Full Steam Drum Machine");
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    @FXML
    public void notYetImplemented() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        styleButtonDialog(alert);
        alert.setTitle("Not yet implemented");
        alert.setContentText("Deze functionaliteit is nog niet geïmplementeerd.");
        alert.showAndWait();
    }

    protected void styleButtonDialog(Dialog<ButtonType> dialog) {
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getStylesheets().add(
                App.class.getResource("styles.css").toExternalForm());
        dialogPane.getStyleClass().add("fx-dialog");
    }

    protected void styleStringDialog(Dialog<String> dialog) {
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getStylesheets().add(
                App.class.getResource("styles.css").toExternalForm());
        dialogPane.getStyleClass().add("fx-dialog");
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml));
        return fxmlLoader.load();
    }
}