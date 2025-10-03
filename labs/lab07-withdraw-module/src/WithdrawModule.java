import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WithdrawModule {

    // Kết nối database
    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/atm_db";
        String user = "root";
        String password = "123456"; // đúng mật khẩu bạn đã ALTER USER
        return DriverManager.getConnection(url, user, password);
    }

    // Xác thực PIN
    public static boolean verifyPin(String accountNumber, String pin) {
        String sql = "SELECT * FROM accounts WHERE account_number = ? AND pin = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, accountNumber);
            stmt.setString(2, pin);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Thực hiện rút tiền
    public static void withdraw(String accountNumber, double amount) throws Exception {
        String selectSql = "SELECT balance FROM accounts WHERE account_number = ?";
        String updateSql = "UPDATE accounts SET balance = ? WHERE account_number = ?";
        String insertTx = "INSERT INTO transactions(account_number, tx_type, amount, balance_after) VALUES (?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement selectStmt = conn.prepareStatement(selectSql);
             PreparedStatement updateStmt = conn.prepareStatement(updateSql);
             PreparedStatement txStmt = conn.prepareStatement(insertTx)) {

            conn.setAutoCommit(false);

            // Kiểm tra số dư
            selectStmt.setString(1, accountNumber);
            ResultSet rs = selectStmt.executeQuery();
            if (!rs.next()) {
                throw new Exception("Account not found");
            }

            double balance = rs.getDouble("balance");
            if (balance < amount) {
                throw new Exception("Insufficient funds");
            }

            double newBalance = balance - amount;

            // Cập nhật số dư
            updateStmt.setDouble(1, newBalance);
            updateStmt.setString(2, accountNumber);
            updateStmt.executeUpdate();

            // Lưu giao dịch
            txStmt.setString(1, accountNumber);
            txStmt.setString(2, "WITHDRAW");
            txStmt.setDouble(3, amount);
            txStmt.setDouble(4, newBalance);
            txStmt.executeUpdate();

            conn.commit();

            System.out.println("Withdraw success. New balance: " + newBalance);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // Chạy thử
    public static void main(String[] args) {
        String accountNumber = "123456";
        String pin = "2512";
        double amount = 200000.0;

        if (verifyPin(accountNumber, pin)) {
            try {
                withdraw(accountNumber, amount);
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        } else {
            System.out.println("Invalid PIN");
        }
    }
}
