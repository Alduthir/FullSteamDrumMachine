package org.alduthir.repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Interface DatabaseInteractionInterface
 * <p>
 * An interface defining the basic methods that are of import for each repository.
 *
 * @param <T>
 */
public interface DatabaseInteractionInterface<T> {
    /**
     * Retrieve every record from the specified table and hydrate them into an ObservableList of objects
     *
     * @return An ObservableList of objects for the expected type.
     * @throws SQLException If the query raises an exception.
     */
    List<T> fetchAll() throws SQLException;

    /**
     * Fetch a single databaseRecord and hydrate it into an object of the expected Type.
     *
     * @param id the id for which to fetch an object.
     * @return A hydrated object of the expected type.
     * @throws SQLException If the query raises an exception. Or no record for the given Id is found.
     */
    T findById(int id) throws SQLException;

    /**
     * Delete the database record for the given Id.
     *
     * @param id the Id for which to delete the record.
     * @throws SQLException if the query raises an exception. For instance because a record could not be deleted.
     */
    void deleteById(int id) throws SQLException;
}
