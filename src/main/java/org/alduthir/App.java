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

    /**
     * The starting point for launching the application. Calls javafx.application.Application.launch();
     * @param args command-line arguments for launching the application.
     */
    public static void main(String[] args) {
        launch();
    }

    /**
     * On a succesfull launch, start the application on the Song screen.
     * @param stage The window in which the application is displayed.
     * @throws IOException if no resource can be loaded for gui/songScreen.fxml.
     */
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

    /**
     * In case functionality for a button is not yet implemented. Display an Alert to the user.
     */
    @FXML
    protected void notYetImplemented() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                App.class.getResource("styles.css").toExternalForm());
        dialogPane.getStyleClass().add("fx-dialog");
        alert.setTitle("Not yet implemented");
        alert.setContentText("Deze functionaliteit is nog niet geïmplementeerd.");
        alert.showAndWait();
    }
}