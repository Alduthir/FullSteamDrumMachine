package org.alduthir.repository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.alduthir.model.Measure;
import org.alduthir.model.SongMeasure;
import org.alduthir.model.Song;
import org.alduthir.util.NamedPreparedStatement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Class MeasureRepository
 * <p>
 * A database repository class containing query functions for interacting with Measures.
 */
public class MeasureRepository extends DatabaseInteractionService<Measure> {
    /**
     * Call the super constructor attempting to establish a database connection.
     *
     * @throws SQLException           If no connection could be established.
     * @throws ClassNotFoundException If the jdbc Driver could not be found.
     */
    public MeasureRepository() throws SQLException, ClassNotFoundException {
    }

    /**
     * @inheritDoc
     */
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

    /**
     * @inheritDoc
     */
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

    /**
     * @inheritDoc
     */
    @Override
    public void deleteById(int id) throws SQLException {
        String sql = "DELETE FROM Song WHERE songId = :songId";
        NamedPreparedStatement stmt = NamedPreparedStatement.prepareStatement(connection, sql);
        stmt.setInt("songId", id);
        stmt.executeUpdate();
        stmt.close();
    }

    /**
     * Create a new measure and return a new object with the newly inserted Id.
     *
     * @param measure The measure to be inserted into the database.
     * @return The newly inserted measure including it's Id.
     * @throws SQLException If the query throws an exception.
     */
    public Measure createMeasure(Measure measure) throws SQLException {
        String sql = "INSERT INTO Measure(name, beatUnit, beatsInMeasure) VALUES(:name, :beatUnit, :beatsInMeasure)";
        NamedPreparedStatement stmt = NamedPreparedStatement.prepareStatement(connection, sql);
        stmt.setString("name", measure.getName());
        stmt.setInt("beatUnit", measure.getBeatUnit());
        stmt.setInt("beatsInMeasure", measure.getBeatsInMeasure());
        stmt.executeUpdate(stmt.getQuery(), Statement.RETURN_GENERATED_KEYS);

        ResultSet rs = stmt.getGeneratedKeys();
        if (rs != null && rs.next()) {
            return findById(rs.getInt(1));
        }

        return measure;
    }

    /**
     * Fetch a hydrated list of SongMeasures for the given Song.
     *
     * @param song The Song for which to retrieve all SongMeasures.
     * @return A hydrated list of SongMeasures containing both hydrated Song and Measure objects.
     * @throws SQLException If the query raises an exception.
     */
    public ObservableList<SongMeasure> fetchForSong(Song song) throws SQLException {
        ObservableList<SongMeasure> songMeasureCollection = FXCollections.observableArrayList();

        String sql = "SELECT * FROM SongMeasure sm WHERE sm.songId = :songId ORDER BY sm.sequence";
        NamedPreparedStatement stmt = NamedPreparedStatement.prepareStatement(connection, sql);
        stmt.setInt("songId", song.getId());
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            SongMeasure songMeasure = new SongMeasure(
                    rs.getInt("songMeasureId"),
                    song,
                    findById(rs.getInt("measureId"))
            );
            songMeasureCollection.add(songMeasure);
        }
        stmt.close();
        return songMeasureCollection;
    }

    /**
     * Create a new SongMeasure linking the given measure to the given Song and set it's Sequence to the given value.
     *
     * @param measure  The Measure to be added to a Song.
     * @param song     The Song to which the Measure will be added.
     * @param sequence The position of this new Measure in the song's order.
     * @throws SQLException If the query raises an exception.
     */
    public void addToSong(Measure measure, Song song, int sequence) throws SQLException {
        String sql = "INSERT INTO SongMeasure(songId, measureId, sequence) VALUES(:songId, :measureId, :sequence)";
        NamedPreparedStatement stmt = NamedPreparedStatement.prepareStatement(connection, sql);
        stmt.setInt("songId", song.getId());
        stmt.setInt("measureId", measure.getId());
        stmt.setInt("sequence", sequence);
        stmt.executeUpdate();
    }

    /**
     * Delete a given SongMeasure, effectively removing that instance from the Song. Deletes a single SongMeasure
     * instead of removing by combination of Song and Measure. As it is possible to have the same measure occur in
     * different places of the song. And we don't want to remove all of them in one go.
     *
     * @param songMeasure The songMeasure to be removed.
     * @throws SQLException If the query raises an Exception.
     */
    public void removeFromSong(SongMeasure songMeasure) throws SQLException {
        String sql = "DELETE FROM SongMeasure WHERE songMeasureId = :songMeasureId";
        NamedPreparedStatement stmt = NamedPreparedStatement.prepareStatement(connection, sql);
        stmt.setInt("songMeasureId", songMeasure.getSongMeasureId());
        stmt.executeUpdate();
        stmt.close();
    }

    /**
     * Update All SongMeasures based on their index in the collection.
     * ToDo (COULD HAVE)
     *
     * @param songMeasureCollection a list of SongMeasures organised in the UI.
     */
    public void updateSequence(ObservableList<SongMeasure> songMeasureCollection) {
    }
}