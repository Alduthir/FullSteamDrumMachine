package org.alduthir.repository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.alduthir.model.Song;
import org.alduthir.util.NamedPreparedStatement;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Class SongRepository
 * <p>
 * A database repository class containing query functions for interacting with Songs.
 */
public class SongRepository extends DatabaseInteractionService<Song> implements SongRepositoryInterface {
    /**
     * Call the super constructor attempting to establish a database connection.
     *
     * @throws SQLException           If no connection could be established.
     * @throws ClassNotFoundException If the jdbc Driver could not be found.
     */
    public SongRepository() throws SQLException, ClassNotFoundException {
    }

    /**
     * @inheritDoc
     */
    @Override
    public List<Song> fetchAll() throws SQLException {
        List<Song> songCollection = new ArrayList<>();

        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM Song");
        while (rs.next()) {
            Song song = new Song(
                    rs.getInt("songId"),
                    rs.getString("name"),
                    rs.getInt("bpm")
            );
            songCollection.add(song);
        }
        stmt.close();
        return songCollection;
    }

    /**
     * @inheritDoc
     */
    @Override
    public Song findById(int id) throws SQLException {
        String sql = "SELECT * FROM Song WHERE songId = :songId";
        NamedPreparedStatement stmt = NamedPreparedStatement.prepareStatement(connection, sql);
        stmt.setInt("songId", id);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            Song song = new Song(
                    rs.getInt("songId"),
                    rs.getString("name"),
                    rs.getInt("bpm")
            );
            stmt.close();
            return song;
        }
        rs.close();
        throw new SQLException(String.format("No song found with songId %d.", id));
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
     * Create a new soong with the given name, and the default BPM value of 75;
     *
     * @param songName The given name for the new Song.
     * @throws SQLException If the query throws an exception.
     */
    @Override
    public void createSong(String songName) throws SQLException {
        String sql = "INSERT INTO Song(name, bpm) VALUES(:name, 75)";
        NamedPreparedStatement stmt = NamedPreparedStatement.prepareStatement(connection, sql);
        stmt.setString("name", songName);
        stmt.executeUpdate();
    }

    /**
     * Update the BPM of the given song.
     *
     * @param song The song to update.
     * @throws SQLException If the query throws an exception.
     */
    @Override
    public void updateBpm(Song song, int bpmValue) throws SQLException {
        String sql = "UPDATE Song SET bpm = :bpm WHERE songId = :songId";
        NamedPreparedStatement stmt = NamedPreparedStatement.prepareStatement(connection, sql);
        stmt.setInt("bpm", bpmValue);
        stmt.setInt("songId", song.getId());
        stmt.executeUpdate();
        stmt.close();
    }
}
