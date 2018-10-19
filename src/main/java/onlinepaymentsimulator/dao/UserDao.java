package onlinepaymentsimulator.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import onlinepaymentsimulator.domain.Token;
import onlinepaymentsimulator.domain.User;
import org.mindrot.jbcrypt.BCrypt;

public class UserDao implements Dao<User> {

    private final Connection conn;

    public UserDao(Connection connection) {
        this.conn = connection;
    }

    /**
     * Login with a username and a password
     *
     * @param username The username
     * @param password The password
     * @return Success: a token, error: null
     */
    public Token login(Integer username, String password) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT password FROM \"User\" WHERE name = ?");

        // Add username to query and execute query
        stmt.setInt(1, username);
        ResultSet result = stmt.executeQuery();

        // If cannot find user return null                
        if (!result.next()) {
            return null;
        }

        // If password matches
        boolean passwordsMatches = BCrypt.checkpw(password, result.getString("password"));
        if (!passwordsMatches) {
            return null;
        }

        // Create a token
        long expireTime = new Date().getTime();
        expireTime += 15 * 60 * 1000;

        Token token = new Token(new Date(expireTime));

        return token;
    }

    @Override
    public User single(Integer key) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT name, customer_id FROM \"User\" WHERE name = ?");
        stmt.setInt(1, key);
        
        ResultSet result = stmt.executeQuery();
        
        // User found let's create a User object
        if(result.next()) {
            return new User(0, result.getInt("name"), new CustomerDao(conn).singlePersonalCustomer(result.getInt("customer_id")));
        }
        
        return null;
    }

    @Override
    public List<User> all() throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT name, customer_id FROM \"User\"");
        
        ResultSet result = stmt.executeQuery();
        
        // Users found let's create User objects
        List<User> users = new ArrayList<>();
        
        while(result.next()) {
            users.add(new User(result.getInt("id"), result.getInt("name"), new CustomerDao(conn).singlePersonalCustomer(result.getInt("customer_id"))));
        }
        
        return users;
    }

    public User findById(int id) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT name, customer_id FROM \"User\" WHERE id = ?");
        stmt.setInt(1, id);
        
        ResultSet result = stmt.executeQuery();
        
        // User found let's create a User object
        if(result.next()) {
            return new User(result.getInt("id"), result.getInt("name"), new CustomerDao(conn).singlePersonalCustomer(result.getInt("customer_id")));
        }
        
        return null;
    }
}
