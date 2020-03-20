package org.alduthir.measure;

import com.jfoenix.controls.JFXCheckBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.alduthir.instrument.Instrument;
import org.alduthir.song.Song;
import org.alduthir.util.AbstractDatabaseInteractionService;
import org.alduthir.util.NamedPreparedStatement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MeasureRepository extends AbstractDatabaseInteractionService<Measure> {

    public MeasureRepository() throws SQLException, ClassNotFoundException {
    }

    @Override
    public ObservableList<Measure> fetchAll() throws SQLException {
        ObservableList<Measure> measureCollection = FXCollections.observableArrayList();

        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM Measure");
        while (rs.next()) {
            Measure measure = new Measure(
                    rs.getInt("measureId"),
                    rs.getString("name"),
                    rs.getInt("beatUnit"),
                    rs.getInt("beatsInMeasure")
            );
            measureCollection.add(measure);
        }
        stmt.close();
        return measureCollection;
    }

    @Override
    public Measure findById(int id) throws SQLException {
        String sql = "SELECT * FROM Measure WHERE measureId = :measureId";
        NamedPreparedStatement stmt = NamedPreparedStatement.prepareStatement(connection, sql);
        stmt.setInt("measureId", id);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            Measure measure = new Measure(
                    rs.getInt("measureId"),
                    rs.getString("name"),
                    rs.getInt("beatUnit"),
                    rs.getInt("beatsInMeasure")
            );
            stmt.close();
            return measure;
        }
        rs.close();
        throw new SQLException(String.format("No measure found with measureID %d.", id));
    }

    @Override
    public void deleteById(int id) throws SQLException {
        String sql = "DELETE FROM Song WHERE songId = :songId";
        NamedPreparedStatement stmt = NamedPreparedStatement.prepareStatement(connection, sql);
        stmt.setInt("songId", id);
        stmt.executeUpdate();
        stmt.close();
    }

    public void createMeasure(Measure measure) throws SQLException {
        String sql = "INSERT INTO Measure(name, beatUnit, beatsInMeasure) VALUES(:name, :beatUnit, :beatsInMeasure)";
        NamedPreparedStatement stmt = NamedPreparedStatement.prepareStatement(connection, sql);
        stmt.setString("name", measure.getName());
        stmt.setInt("beatUnit", measure.getBeatUnit());
        stmt.setInt("beatsInMeasure", measure.getBeatsInMeasure());
        stmt.executeUpdate(stmt.getQuery(), Statement.RETURN_GENERATED_KEYS);

        ResultSet rs = stmt.getGeneratedKeys();
        if (rs != null && rs.next()) {
            measure.setId(rs.getInt(1));
        }
    }

    public ObservableList<Measure> fetchForSong(Song song) throws SQLException {
        ObservableList<Measure> instrumentCollection = FXCollections.observableArrayList();

        String sql = "SELECT * FROM Measure m JOIN SongMeasure sm ON m.measureId = sm.measureId WHERE sm.songId = :songId ORDER BY sm.sequence";
        NamedPreparedStatement stmt = NamedPreparedStatement.prepareStatement(connection, sql);
        stmt.setInt("songId", song.getId());
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Measure measure = new Measure(
                    rs.getInt("measureId"),
                    rs.getString("name"),
                    rs.getInt("beatUnit"),
                    rs.getInt("beatsInMeasure")
            );
            instrumentCollection.add(measure);
        }
        stmt.close();
        return instrumentCollection;
    }

    public void addToSong(Measure measure, Song song, int sequence) throws SQLException {
        String sql = "INSERT INTO SongMeasure(songId, measureId, sequence) VALUES(:songId, :measureId, :sequence)";
        NamedPreparedStatement stmt = NamedPreparedStatement.prepareStatement(connection, sql);
        stmt.setInt("songId", song.getId());
        stmt.setInt("measureId", measure.getId());
        stmt.setInt("sequence", sequence);
        stmt.executeUpdate();
    }

    public void removeFromMeasure(Measure measure, Song song) throws SQLException {
        String sql = "DELETE FROM SongMeasure WHERE measureId = :measureId AND songId = :songId";
        NamedPreparedStatement stmt = NamedPreparedStatement.prepareStatement(connection, sql);
        stmt.setInt("measureId", measure.getId());
        stmt.setInt("songId", song.getId());
        stmt.executeUpdate();
        stmt.close();
    }

    public void updateSequence(Measure measure, Song song, ObservableList<JFXCheckBox> checkBoxCollection) {
    }
}
