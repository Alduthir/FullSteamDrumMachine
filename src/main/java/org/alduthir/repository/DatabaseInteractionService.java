package org.alduthir.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Abstract DatabaseInteractionService
 * <p>
 * An abstract implementation of the DataBaseInteractionInterface.
 * The connection parameters are shared throughout each repository so this centralizes them into a single class.
 *
 * @param <T>
 */
public abstract class DatabaseInteractionService<T> implements DatabaseInteractionInterface<T> {

    protected Connection connection;

    /**
     * Upon creation each implementation of this service must attempt to establish a connection with the database.
     *
     * @throws SQLException           If not connection can be established.
     * @throws ClassNotFoundException If the JDBC driver cannot be found.
     */
    public DatabaseInteractionService() throws SQLException, ClassNotFoundException {
        this.connection = getConnection();
    }

    /**
     * Establish a connection to the database. If one already exists, simply return it instead.
     *
     * @return The established Connection object.
     * @throws ClassNotFoundException If the JDBC driver cannot be loaded.
     * @throws SQLException           If the connection cannot be sestablished.
     */
    protected Connection getConnection() throws ClassNotFoundException, SQLException {
        if (connection == null) {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/full-steam-drum-machine?useLegacyDatetimeCode=false&serverTimezone=UTC",
                    "root",
                    "password"
            );
        }
        return connection;
    }
}
