package org.alduthir;

import com.mysql.cj.jdbc.MysqlDataSource;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.alduthir.repository.*;
import org.alduthir.service.*;

import javax.sound.midi.MidiUnavailableException;
import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {
    protected static InstrumentManageServiceInterface instrumentManageServiceInterface;
    protected static MeasureManageServiceInterface measureManageServiceInterface;
    protected static SongManageServiceInterface songManageServiceInterface;
    protected static InstrumentRepositoryInterface instrumentRepositoryInterface;
    protected static MeasureRepositoryInterface measureRepositoryInterface;
    protected static SongRepositoryInterface songRepositoryInterface;
    protected static MusicPlayerInterface musicPlayerInterface;

    /**
     * The starting point for launching the application. Calls javafx.application.Application.launch();
     *
     * @param args command-line arguments for launching the application.
     */
    public static void main(String[] args) {
        try {
            MysqlDataSource dataSource = new MysqlDataSource();
            dataSource.setUrl("jdbc:mysql://localhost:3306/full-steam-drum-machine?useLegacyDatetimeCode=false&serverTimezone=UTC");
            dataSource.setUser("root");
            dataSource.setPassword("password");

            songRepositoryInterface = new SongRepository(dataSource);
            measureRepositoryInterface = new MeasureRepository(dataSource);
            instrumentRepositoryInterface = new InstrumentRepository(dataSource);

            musicPlayerInterface = new MidiPlayer(instrumentRepositoryInterface, measureRepositoryInterface);
            songManageServiceInterface = new SongManageService(
                    measureRepositoryInterface,
                    songRepositoryInterface,
                    musicPlayerInterface
            );
            measureManageServiceInterface = new MeasureManageService(measureRepositoryInterface, musicPlayerInterface);
            instrumentManageServiceInterface = new InstrumentManageService(
                    instrumentRepositoryInterface,
                    musicPlayerInterface
            );
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
            System.exit(1);
        }

        launch();
    }

    /**
     * On a succesfull launch, start the application on the Song screen. If no database connection can be established
     * the application will exit;
     *
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
}