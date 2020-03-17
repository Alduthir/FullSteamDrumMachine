package org.alduthir;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("gui/songScreen.fxml"));
        Scene scene = new Scene(loader.load(), 800, 400);
        stage.setMinHeight(300);
        stage.setMinWidth(800);
        stage.setScene(scene);
        stage.setTitle("Full Steam Drum Machine");
        stage.show();
    }

    @FXML
    protected void notYetImplemented() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        styleDialog(alert);
        alert.setTitle("Not yet implemented");
        alert.setContentText("Deze functionaliteit is nog niet geïmplementeerd.");
        alert.showAndWait();
    }

    protected void styleDialog(Dialog dialog) {
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getStylesheets().add(
                App.class.getResource("styles.css").toExternalForm());
        dialogPane.getStyleClass().add("fx-dialog");
    }
}