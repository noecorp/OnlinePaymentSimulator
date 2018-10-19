
package onlinepaymentsimulator.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import onlinepaymentsimulator.domain.Account;
import onlinepaymentsimulator.domain.Balance;
import onlinepaymentsimulator.domain.CurrencyType;
import onlinepaymentsimulator.domain.PINCode;
import onlinepaymentsimulator.domain.User;


public class PINCodeDao {
    private final Connection conn;

    public PINCodeDao(Connection conn) {
        this.conn = conn;
    }
    
    private List<PINCode> getUsersPINCodes(User user) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT bank_code, customer_code FROM PINCode WHERE user_id = ?");
        stmt.setInt(1, user.getId());
        
        ResultSet result = stmt.executeQuery();
        
        List<PINCode> pincodes = new ArrayList<>();
        
        while(result.next()) {
            pincodes.add(new PINCode(result.getInt("bank_code"), result.getInt("customer_code"), user));
        }
        
        return pincodes;
    }
    
    public PINCode getRandomPIN(User user) throws SQLException {
        
        // Get user's pin codes
        List<PINCode> pincodes = getUsersPINCodes(user);
        
        // Get random PIN
        Random random = new Random();
        int index = random.nextInt(pincodes.size());
        
        return pincodes.get(index);
    }
    
    public boolean checkPIN(Integer bankPIN, Integer pinInput, User user) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT bank_code, customer_code FROM PINCode WHERE user_id = ? AND bank_code = ? AND customer_code = ? LIMIT 1");
        stmt.setInt(1, user.getId());
        stmt.setInt(2, bankPIN);
        stmt.setInt(3, pinInput);
        
        return stmt.executeQuery().next();
    }
}
