package com.portfolio.db;

import java.sql.*;

public final class ResultsRepository {

    private static final String URL = "jdbc:h2:file:./build/results/results;AUTO_SERVER=TRUE";

    private ResultsRepository() {

    }

    private static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, "sa", "");
    }

    public static synchronized void reset() {
        try (Connection c = connect(); Statement s = c.createStatement()) {
            s.execute("""
                    CREATE TABLE IF NOT EXISTS test_result (
                        id IDENTITY PRIMARY KEY,
                        test_class VARCHAR(200),
                        test_name VARCHAR(200),
                        status VARCHAR(10),
                        duration_ms BIGINT,
                        run_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                    )""");
            s.execute("DELETE FROM test_result");


        } catch (SQLException e) {
            throw new IllegalStateException("could not prepare results database", e);
        }
    }

    public static synchronized void save(String testClass, String testName, String status, long durationMs) {
        String sql = "INSERT INTO test_result (test_class, test_name, status, duration_ms) VALUES (?, ?, ?, ?)";
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, testClass);
            ps.setString(2, testName);
            ps.setString(3, status);
            ps.setLong(4, durationMs);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Could not save test result", e);
        }
    }

    public static double passRate() {
        String sql = """
                SELECT COALESCE(100.0 * SUM(CASE WHEN status = 'PASS' THEN 1 ELSE 0 END)
                       / NULLIF(COUNT(*), 0), 0)
                FROM test_result WHERE status IN ('PASS', 'FAIL')""";
        try (Connection c = connect(); Statement s = c.createStatement(); ResultSet rs = s.executeQuery(sql)) {
            rs.next();
            return rs.getDouble(1);
        } catch (SQLException e) {
            throw new IllegalStateException("Could not read pass rate", e);
        }
    }

    public static int count(String status) {
        try (Connection c = connect();
             PreparedStatement ps = c.prepareStatement("SELECT COUNT(*) FROM test_result WHERE status = ?")) {
            ps.setString(1, status);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Could not count results", e);
        }
    }
}
