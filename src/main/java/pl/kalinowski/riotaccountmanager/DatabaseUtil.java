package pl.kalinowski.riotaccountmanager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DatabaseUtil {
    private static final String DB_URL = "jdbc:sqlite:riot_accounts.db";

    public static Connection connect() {
        try {
            return DriverManager.getConnection(DB_URL);
        } catch (Exception e) {
            throw new RuntimeException("Cannot connect to database", e);
        }
    }

    public static void initializeDatabase() {
        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.execute("""
                        CREATE TABLE IF NOT EXISTS accounts (
                            id INTEGER PRIMARY KEY AUTOINCREMENT,
                            username TEXT NOT NULL,
                            encrypted_password TEXT NOT NULL
                        );
                    """);
        } catch (Exception e) {
            throw new RuntimeException("Database initialization error", e);
        }

    }
}
