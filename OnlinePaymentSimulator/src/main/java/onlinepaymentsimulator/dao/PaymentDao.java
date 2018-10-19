package onlinepaymentsimulator.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import onlinepaymentsimulator.domain.Balance;
import onlinepaymentsimulator.domain.Payment;


public class PaymentDao {
    private final Connection conn;

    public PaymentDao(Connection conn) {
        this.conn = conn;
    }
    
    public void savePaymentToDatabase(Payment payment) throws SQLException {
        try(PreparedStatement stmt = conn.prepareStatement("INSERT INTO Payment (created, eur, cent, account_id, toIBAN, ref, msg) VALUES (?, ?, ?, ?, ?, ?, ?)")) {
            Balance amount = payment.getSum();

            long now = new java.util.Date().getTime();

            stmt.setDate(1, new Date(now));
            stmt.setInt(2, amount.getEur());
            stmt.setInt(3, amount.getCent());
            stmt.setInt(4, payment.getFrom().getId());
            stmt.setString(5, payment.getToIBAN());
            stmt.setInt(6, payment.getRef());
            stmt.setString(7, payment.getMessage());

            stmt.executeUpdate();
        }
    }
}
