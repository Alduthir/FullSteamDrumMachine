package org.alduthir.repository;

import org.alduthir.model.Instrument;
import org.alduthir.model.Measure;
import org.alduthir.util.NamedPreparedStatement;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Class InstrumentRepository
 * <p>
 * A database repository class containing query functions for interacting with Instruments.
 */
public class InstrumentRepository extends DatabaseInteractionService<Instrument> implements InstrumentRepositoryInterface {
    /**
     * Call the super constructor setting the dataSource object.
     *
     * @param dataSource The Connection object for the database.
     */
    public InstrumentRepository(DataSource dataSource) {
        super(dataSource);
    }

    /**
     * @inheritDoc
     */
    @Override
    public List<Instrument> fetchAll() throws DataRetrievalException {
        List<Instrument> instrumentCollection = new ArrayList<>();

        try {
            Statement stmt = this.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Instrument");
            while (rs.next()) {
                Instrument instrument = new Instrument(
                        rs.getInt("instrumentId"),
                        rs.getString("name"),
                        rs.getInt("midiNumber")
                );
                instrumentCollection.add(instrument);
            }
            stmt.close();
            return instrumentCollection;
        } catch (SQLException e) {
            throw new DataRetrievalException("Unable to retrieve instruments", e);
        }

    }

    /**
     * @inheritDoc
     */
    @Override
    public Instrument findById(int id) throws DataRetrievalException {
        try {
            String sql = "SELECT * FROM Instrument WHERE instrumentId = :instrumentId";
            NamedPreparedStatement stmt = NamedPreparedStatement.prepareStatement(this.getConnection(), sql);
            stmt.setInt("instrumentId", id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Instrument instrument = new Instrument(
                        rs.getInt("instrumentId"),
                        rs.getString("name"),
                        rs.getInt("midiNumber")
                );
                stmt.close();
                return instrument;
            }
            rs.close();
        } catch (SQLException e) {
            throw new DataRetrievalException(String.format("No measure found with measureId %d.", id), e);
        }
        throw new DataRetrievalException(String.format("No song found with measureId %d.", id));
    }

    /**
     * @inheritDoc
     */
    @Override
    public void deleteById(int id) throws DataRemovalException {
        try {
            String sql = "DELETE FROM Instrument WHERE instrumentId = :instrumentId";
            NamedPreparedStatement stmt = NamedPreparedStatement.prepareStatement(this.getConnection(), sql);
            stmt.setInt("instrumentId", id);
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            throw new DataRemovalException("Deleting instrument from failed", e);
        }
    }

    /**
     * Insert a new Instrument into the database and update the given Instrument object with the newly inserted id.
     *
     * @param name       The name given for the new Instrument.
     * @param midiNumber the number corresponding to a key in the midiPlayer channel. (the sound to be played)
     * @return The newly inserted Instrument
     */
    @Override
    public Instrument createInstrument(String name, int midiNumber) throws DataPersistanceException {
        try {
            String sql = "INSERT INTO Instrument(name, midiNumber) VALUES(:name, :midiNumber)";
            NamedPreparedStatement stmt = NamedPreparedStatement.prepareStatement(this.getConnection(), sql);
            stmt.setString("name", name);
            stmt.setInt("midiNumber", midiNumber);
            stmt.executeUpdate(stmt.getQuery(), Statement.RETURN_GENERATED_KEYS);

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs != null && rs.next()) {
                return findById(rs.getInt(1));
            }

            throw new DataPersistanceException("Unable to create new Instrument");
        } catch (SQLException | DataRetrievalException e) {
            throw new DataPersistanceException("Unable to create new Instrument", e);
        }
    }

    /**
     * Fetch all Instruments for a given Measure.
     *
     * @param measure The measure for which to fetch all linked Instruments.
     * @return An ObservableList of hydrated InstrumentObjects including their beat within the current measure.
     */
    @Override
    public List<Instrument> fetchForMeasure(Measure measure) throws DataRetrievalException {
        List<Instrument> instrumentCollection = new ArrayList<>();

        try {
            String sql = "SELECT i.instrumentId, i.name, i.midiNumber, mi.beat FROM Instrument i JOIN MeasureInstrument mi ON i.instrumentId = mi.instrumentId WHERE mi.measureId = :measureId";
            NamedPreparedStatement stmt = NamedPreparedStatement.prepareStatement(this.getConnection(), sql);
            stmt.setInt("measureId", measure.getId());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Instrument instrument = new Instrument(
                        rs.getInt("instrumentId"),
                        rs.getString("name"),
                        rs.getInt("midiNumber"),
                        rs.getString("beat")
                );
                instrumentCollection.add(instrument);
            }
            stmt.close();
            return instrumentCollection;
        } catch (SQLException e) {
            throw new DataRetrievalException(String.format("Unable to retrieve instruments for measure %s", measure.getName()), e);
        }
    }

    /**
     * Fetch a list of Instruments which are not currently included in the given Measure.
     *
     * @param measure The measure from which to exclude Instruments.
     * @return A hydrated list of Instruments.
     */
    @Override
    public List<Instrument> fetchReuseOptionCollection(Measure measure) throws DataRetrievalException {
        List<Instrument> instrumentCollection = new ArrayList<>();

        try {
            String sql = "SELECT * FROM Instrument i LEFT JOIN MeasureInstrument mi ON i.instrumentId = mi.instrumentId WHERE mi.measureId <> :measureId";
            NamedPreparedStatement stmt = NamedPreparedStatement.prepareStatement(this.getConnection(), sql);
            stmt.setInt("measureId", measure.getId());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Instrument instrument = new Instrument(
                        rs.getInt("instrumentId"),
                        rs.getString("name"),
                        rs.getInt("midiNumber")
                );
                instrumentCollection.add(instrument);
            }
            stmt.close();
            return instrumentCollection;
        } catch (SQLException e) {
            throw new DataRetrievalException("Unable to retrieve reuse option collection", e);
        }
    }

    /**
     * Add the given Instrument to the Measure.
     *
     * @param instrument The instrument to add to the Measure.
     * @param measure    The measure to which the instrument must be added.
     */
    @Override
    public void addToMeasure(Instrument instrument, Measure measure) throws DataPersistanceException {
        try {
            String sql = "INSERT INTO MeasureInstrument(measureId, instrumentId) VALUES(:measureId, :instrumentId)";
            NamedPreparedStatement stmt = NamedPreparedStatement.prepareStatement(this.getConnection(), sql);
            stmt.setInt("measureId", measure.getId());
            stmt.setInt("instrumentId", instrument.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataPersistanceException("Instrument could not be added to measure", e);
        }
    }

    /**
     * Update the beat for the MeasureInstrument linking the given Measure and Instrument.
     *
     * @param measure     the Measure for which to update the beat.
     * @param instrument  the Instrument for which to update the beat.
     * @param encodedBeat A 16 character string containing 0's and 1's.
     */
    @Override
    public void updateBeat(Measure measure, Instrument instrument, String encodedBeat) throws DataPersistanceException {
        try {
            String sql = "UPDATE MeasureInstrument SET beat = :beat WHERE measureId = :measureId AND instrumentId = :instrumentId";
            NamedPreparedStatement stmt = NamedPreparedStatement.prepareStatement(this.getConnection(), sql);
            stmt.setString("beat", encodedBeat);
            stmt.setInt("measureId", measure.getId());
            stmt.setInt("instrumentId", instrument.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataPersistanceException("The beat could not be saved", e);
        }
    }

    /**
     * Remove the given Instrument from the Measure.
     *
     * @param measure    The Measure from which to remove the Instrument
     * @param instrument The instrument to be removed. It is not deleted, only decoupled from the measure.
     */
    @Override
    public void removeFromMeasure(Measure measure, Instrument instrument) throws DataRemovalException {
        try {
            String sql = "DELETE FROM MeasureInstrument WHERE measureId = :measureId AND instrumentId = :instrumentId";
            NamedPreparedStatement stmt = NamedPreparedStatement.prepareStatement(this.getConnection(), sql);
            stmt.setInt("measureId", measure.getId());
            stmt.setInt("instrumentId", instrument.getId());
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            throw new DataRemovalException("Instrument could not be removed from measure", e);
        }
    }
}
