package onlinepaymentsimulator.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import onlinepaymentsimulator.domain.Account;
import onlinepaymentsimulator.domain.Balance;
import onlinepaymentsimulator.domain.CurrencyType;
import onlinepaymentsimulator.domain.Customer;
import onlinepaymentsimulator.domain.PersonalCustomer;
import onlinepaymentsimulator.domain.User;

public class CustomerDao {

    private final Connection conn;

    public CustomerDao(Connection conn) {
        this.conn = conn;
    }
    
    public PersonalCustomer singlePersonalCustomer(Integer id) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM PersonalCustomer LEFT JOIN Customer ON PersonalCustomer.customer_id = Customer.id WHERE Customer.id = ? LIMIT 1");
        stmt.setInt(1, id);
        
        ResultSet rs = stmt.executeQuery();
        rs.next();
        
        
        return new PersonalCustomer(rs.getString("firstname"), rs.getString("lastname"), rs.getString("social_security_number"), "", rs.getString("phone"), rs.getString("email"), getCustomersUsers(id));
    }
    
    private List<User> getCustomersUsers(Integer id) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT id, name FROM User WHERE customer_id = ?");
        stmt.setInt(1, id);
        
        ResultSet result = stmt.executeQuery();
        
        // Users found let's create User objects
        List<User> users = new ArrayList<>();
        
        while(result.next()) {
            users.add(new User(result.getInt("id"), result.getInt("name")));
        }
        
        
        return users;
    }
    
}
