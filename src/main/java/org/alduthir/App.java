package org.alduthir;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

/**
 * JavaFX App
 */
public class App extends Application {
    private static Scene scene;
    private static Connection con;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("gui/songScreen.fxml"), 800, 400);
        stage.setMinHeight(300);
        stage.setMinWidth(800);
        stage.setScene(scene);
        stage.show();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/full-steam-drum-machine",
                    "root",
                    "password"
            );

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from Song");
            while (rs.next()) {
                System.out.println(rs.getString("name") + "  " + rs.getInt("bpm"));
            }
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }


    @FXML
    public void notYetImplemented() throws IOException {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Not yet implemented");
        alert.setContentText("Deze functionaliteit is nog niet geïmplementeerd.");
        alert.showAndWait();
    }
}