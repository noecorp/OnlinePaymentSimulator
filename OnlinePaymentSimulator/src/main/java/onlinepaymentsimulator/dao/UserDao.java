package onlinepaymentsimulator.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import onlinepaymentsimulator.db.Database;


public class UserDao {
    private final Database db;

    public UserDao(Database db) {
        this.db = db;
    }
    
    /**
     * Login with a username and a password
     * @param username The plain username
     * @param password The hashed password
     * @return Success: a token, error: null
     */
    public String login(Integer username, String password) throws SQLException {
        try (Connection conn = db.getConnection();
                PreparedStatement stmt = conn.prepareStatement("SELECT name FROM User WHERE name = ? AND password = ?");
                ResultSet result = stmt.executeQuery()) {
            
            // If cannot find user return null
            if(!result.next()) {
                return null;
            }
            
            // Create a token
            
        }
        
        return null;
    }
}
