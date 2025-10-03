import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TransactionHistoryModule {

    public static void printHistory(String accountNumber) {
        String sql = "SELECT tx_id, tx_type, amount, balance_after, tx_time " +
                     "FROM transactions WHERE account_number = ? ORDER BY tx_time DESC";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, accountNumber);
            ResultSet rs = stmt.executeQuery();

            System.out.println("=== Transaction History for account: " + accountNumber + " ===");
            while (rs.next()) {
                int txId = rs.getInt("tx_id");
                String type = rs.getString("tx_type");
                double amount = rs.getDouble("amount");
                double balance = rs.getDouble("balance_after");
                String time = rs.getString("tx_time");

                System.out.printf("[%d] %s | Amount: %.2f | Balance after: %.2f | Time: %s%n",
                        txId, type, amount, balance, time);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
