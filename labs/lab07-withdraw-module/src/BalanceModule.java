import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BalanceModule {
    public static void main(String[] args) {
        String accNo = "123456"; // tài khoản ví dụ
        String pin = "2512";

        try (Connection conn = DatabaseConnector.getConnection()) {
            String sql = "SELECT balance FROM accounts WHERE account_number=? AND pin=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, accNo);
            ps.setString(2, pin);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                double balance = rs.getDouble("balance");
                System.out.println("Current balance: " + balance);
            } else {
                System.out.println("Invalid PIN");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
