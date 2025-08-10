package pl.kalinowski.riotaccountmanager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static pl.kalinowski.riotaccountmanager.DatabaseUtil.connect;

public class SQLiteAccountRepository implements AccountRepository {
    @Override
    public void addAccount(String username, String encryptedPassword) {
        String sql = "INSERT INTO accounts(username, encrypted_password) VALUES(?,?)";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, encryptedPassword);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Account> getAllAccounts() {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT id, username, encrypted_password FROM accounts";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                accounts.add(new Account(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("encrypted_password")

                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accounts;
    }

    @Override
    public void deleteAccount(int id) {
        String sql = "DELETE FROM accounts WHERE id=?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
