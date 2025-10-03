import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DepositModule {
    public static void main(String[] args) {
        String accNo = "123456"; // tài khoản ví dụ
        String pin = "2512";
        double amount = 300000.00; // số tiền nạp

        try (Connection conn = DatabaseConnector.getConnection()) {
            // kiểm tra PIN
            String check = "SELECT COUNT(*) FROM accounts WHERE account_number=? AND pin=?";
            PreparedStatement psCheck = conn.prepareStatement(check);
            psCheck.setString(1, accNo);
            psCheck.setString(2, pin);
            var rs = psCheck.executeQuery();
            rs.next();
            int exists = rs.getInt(1);

            if (exists == 1) {
                String sql = "UPDATE accounts SET balance = balance + ? WHERE account_number=?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setDouble(1, amount);
                ps.setString(2, accNo);
                ps.executeUpdate();
                System.out.println("Deposit success");
            } else {
                System.out.println("Invalid PIN");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
