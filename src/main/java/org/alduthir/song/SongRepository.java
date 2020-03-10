package org.alduthir.song;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.alduthir.util.AbstractDatabaseInteractionService;
import org.alduthir.util.NamedPreparedStatement;

import java.sql.*;

public class SongRepository extends AbstractDatabaseInteractionService<Song> {

    @Override
    public ObservableList<Song> fetchAll() throws SQLException, ClassNotFoundException {
        ObservableList<Song> songCollection = FXCollections.observableArrayList();
        Connection connection = openConnection();

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

    @Override
    public Song findById(int id) throws SQLException, ClassNotFoundException {
        Connection connection = openConnection();
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

    @Override
    public void deleteById(int id) throws SQLException, ClassNotFoundException {
        Connection connection = openConnection();
        String sql = "DELETE FROM Song WHERE songId = :songId";
        NamedPreparedStatement stmt = NamedPreparedStatement.prepareStatement(connection, sql);
        stmt.setInt("songId", id);
        stmt.executeUpdate();
        stmt.close();
    }

    public void createSong(Song song) throws SQLException, ClassNotFoundException {
        Connection connection = openConnection();
        String sql = "INSERT INTO Song(name, bpm) VALUES(:name, 75)";
        NamedPreparedStatement stmt = NamedPreparedStatement.prepareStatement(connection, sql);
        stmt.setString("name", song.getName());
        stmt.executeUpdate(stmt.getQuery(), Statement.RETURN_GENERATED_KEYS);

        ResultSet rs = stmt.getGeneratedKeys();
        if (rs != null && rs.next()) {
            song.setId(rs.getInt(1));
        }
    }
}
