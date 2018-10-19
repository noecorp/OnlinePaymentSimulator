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
import onlinepaymentsimulator.domain.User;


public class AccountDao implements Dao<Account>{
    
    private final Connection conn;

    public AccountDao(Connection conn) {
        this.conn = conn;
    }
    
    public List<Account> findAccountsByUser(int id) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Account WHERE user_id = ?");
        stmt.setInt(1, id);
        
        ResultSet result = stmt.executeQuery();
        
        // Accounts found let's create Account objects
        List<Account> accounts = new ArrayList<>();
        
        while(result.next()) {
            Balance balance = new Balance(result.getInt("eur"), result.getInt("cent"), CurrencyType.EUR);
            User user = new UserDao(conn).findById(result.getInt("user_id"));
            
            accounts.add(new Account(result.getInt("id"), balance, result.getString("name"), user, result.getString("iban")));
        }
        
        
        return accounts;
    }

    public Account findByIdWithNoUser(Integer id) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Account WHERE id = ? LIMIT 1");
        stmt.setInt(1, id);
        
        ResultSet rs = stmt.executeQuery();
        rs.next();
        
        CurrencyType currType = (!rs.getString("currency_type").equals("?")) ? CurrencyType.valueOf(rs.getString("currency_type")) : CurrencyType.EUR;
        
        
        return new Account(rs.getInt("id"), new Balance(rs.getInt("eur"), rs.getInt("cent"), currType), rs.getString("name"), null, rs.getString("iban"));
    }
    
    @Override
    public Account single(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Account> all() throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT id, name, user_id, eur, cent, currency_type FROM Account");
        
        ResultSet result = stmt.executeQuery();
        
        // Accounts found let's create Account objects
        List<Account> accounts = new ArrayList<>();
        
        while(result.next()) {
            Balance balance = new Balance(result.getInt("eur"), result.getInt("cent"), CurrencyType.EUR);
            User user = new UserDao(conn).findById(result.getInt("user_id"));
            
            accounts.add(new Account(result.getInt("id"), balance, result.getString("name"), user, result.getString("iban")));
        }
        
        
        return accounts;
    }
    
}
