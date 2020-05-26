package org.alduthir.repository;

import javax.sql.DataSource;
import java.sql.Connection;
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
    private DataSource dataSource;

    /**
     * @param dataSource the datasource containing the connection information.
     */
    public DatabaseInteractionService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Establish a connection to the database. If one already exists, simply return it instead.
     *
     * @return The established Connection object.
     */
    protected Connection getConnection() {
        if (connection == null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = dataSource.getConnection();
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
                System.out.println("No database connection could be established. Application will shut down.");
            }
        }
        return connection;
    }
}
