package onlinepaymentsimulator.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class Database {
    private final String connectionString;

    /**
     * Initializes this helper class
     * @param connectionString A valid connection string for a driver manager
     */
    public Database(String connectionString) {
        this.connectionString = connectionString;
    }
    
    /**
     * Get the Connection object
     * @return Connection object
     * @throws SQLException 
     */
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(connectionString);
    }
}
