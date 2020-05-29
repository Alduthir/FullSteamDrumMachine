package org.alduthir.repository;

import org.alduthir.model.Measure;
import org.alduthir.model.SongMeasure;
import org.alduthir.model.Song;
import org.alduthir.util.NamedPreparedStatement;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Class MeasureRepository
 * <p>
 * A database repository class containing query functions for interacting with Measures.
 */
public class MeasureRepository extends DatabaseInteractionService<Measure> implements MeasureRepositoryInterface {
    /**
     * Call the super constructor setting the dataSource object.
     *
     * @param dataSource The Connection object for the database.
     */
    public MeasureRepository(DataSource dataSource) {
        super(dataSource);
    }

    /**
     * @inheritDoc
     */
    @Override
    public List<Measure> fetchAll() throws DataRetrievalException {
        List<Measure> measureCollection = new ArrayList<>();

        try {
            Statement stmt = this.getConnection().createStatement();
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

        } catch (SQLException e) {
            throw new DataRetrievalException("Unable to fetch measures", e);
        }
    }

    /**
     * @inheritDoc
     */
    @Override
    public Measure findById(int id) throws DataRetrievalException {
        try {
            String sql = "SELECT * FROM Measure WHERE measureId = :measureId";
            NamedPreparedStatement stmt = NamedPreparedStatement.prepareStatement(this.getConnection(), sql);
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
            throw new DataRetrievalException(String.format("No measure found with measureID %d.", id));
        } catch (SQLException e) {
            throw new DataRetrievalException(String.format("No measure found with measureID %d.", id), e);
        }
    }

    /**
     * @inheritDoc
     */
    @Override
    public void deleteById(int id) throws DataRemovalException {
        try {
            String sql = "DELETE FROM Measure WHERE measureId = :measureId";
            NamedPreparedStatement stmt = NamedPreparedStatement.prepareStatement(this.getConnection(), sql);
            stmt.setInt("measureId", id);
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            throw new DataRemovalException("UNable to remove measure", e);
        }

    }

    /**
     * Create a new measure and return a new object with the newly inserted Id.
     *
     * @param measure The measure to be inserted into the database.
     * @return The newly inserted measure including it's Id.
     */
    @Override
    public Measure createMeasure(Measure measure) throws DataPersistanceException {
        try {
            String sql = "INSERT INTO Measure(name, beatUnit, beatsInMeasure) VALUES(:name, :beatUnit, :beatsInMeasure)";
            NamedPreparedStatement stmt = NamedPreparedStatement.prepareStatement(this.getConnection(), sql);
            stmt.setString("name", measure.getName());
            stmt.setInt("beatUnit", measure.getBeatUnit());
            stmt.setInt("beatsInMeasure", measure.getBeatsInMeasure());
            stmt.executeUpdate(stmt.getQuery(), Statement.RETURN_GENERATED_KEYS);

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs != null && rs.next()) {
                return findById(rs.getInt(1));
            }
            throw new DataPersistanceException("Attempted to create new Measure, but no generated key returned.");
        } catch (SQLException | DataRetrievalException e) {
            throw new DataPersistanceException("Unable to create new measure", e);
        }
    }

    /**
     * Fetch a hydrated list of SongMeasures for the given Song.
     *
     * @param song The Song for which to retrieve all SongMeasures.
     * @return A hydrated list of SongMeasures containing both hydrated Song and Measure objects.
     */
    @Override
    public List<SongMeasure> fetchForSong(Song song) throws DataRetrievalException {
        List<SongMeasure> songMeasureCollection = new ArrayList<>();

        try {
            String sql = "SELECT * FROM SongMeasure sm WHERE sm.songId = :songId ORDER BY sm.sequence";
            NamedPreparedStatement stmt = NamedPreparedStatement.prepareStatement(this.getConnection(), sql);
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
        } catch (SQLException e) {
            throw new DataRetrievalException("SongMeasures for song could not be retrieved", e);
        }
    }

    /**
     * Create a new SongMeasure linking the given measure to the given Song and set it's Sequence to the given value.
     *
     * @param measure  The Measure to be added to a Song.
     * @param song     The Song to which the Measure will be added.
     * @param sequence The position of this new Measure in the song's order.
     */
    @Override
    public void addToSong(Measure measure, Song song, int sequence) throws DataPersistanceException {
        try {
            String sql = "INSERT INTO SongMeasure(songId, measureId, sequence) VALUES(:songId, :measureId, :sequence)";
            NamedPreparedStatement stmt = NamedPreparedStatement.prepareStatement(this.getConnection(), sql);
            stmt.setInt("songId", song.getId());
            stmt.setInt("measureId", measure.getId());
            stmt.setInt("sequence", sequence);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataPersistanceException("Failed to create new SongMeasure", e);
        }
    }

    /**
     * Delete a given SongMeasure, effectively removing that instance from the Song. Deletes a single SongMeasure
     * instead of removing by combination of Song and Measure. As it is possible to have the same measure occur in
     * different places of the song. And we don't want to remove all of them in one go.
     *
     * @param songMeasure The songMeasure to be removed.
     */
    @Override
    public void removeFromSong(SongMeasure songMeasure) throws DataRemovalException {
        try {
            String sql = "DELETE FROM SongMeasure WHERE songMeasureId = :songMeasureId";
            NamedPreparedStatement stmt = NamedPreparedStatement.prepareStatement(this.getConnection(), sql);
            stmt.setInt("songMeasureId", songMeasure.getSongMeasureId());
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            throw new DataRemovalException("Unable to delete songMeasure", e);
        }
    }
}
