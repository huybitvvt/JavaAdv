package com.bookstore.database;

import org.apache.commons.dbcp2.BasicDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Database Connection Manager - Sử dụng DBCP2 Connection Pool
 */
public class DatabaseConnection {

    private static BasicDataSource dataSource;
    private static final Properties properties = new Properties();

    // Database configuration
    private static String DB_URL;
    private static String DB_USER;
    private static String DB_PASSWORD;
    private static String DB_DRIVER;

    static {
        loadConfiguration();
        initializeDataSource();
    }

    /**
     * Load database configuration from config.properties
     */
    private static void loadConfiguration() {
        try (InputStream input = DatabaseConnection.class.getClassLoader()
                .getResourceAsStream("database.properties")) {

            if (input == null) {
                // Use default values if file not found
                System.out.println("Warning: database.properties not found, using default configuration");
                setDefaultConfiguration();
                return;
            }

            properties.load(input);

            DB_URL = properties.getProperty("db.url", "jdbc:sqlserver://localhost:1433;databaseName=QuanLyCuaHangSach;encrypt=false");
            DB_USER = properties.getProperty("db.username", "sa");
            DB_PASSWORD = properties.getProperty("db.password", "123");
            DB_DRIVER = properties.getProperty("db.driver", "com.microsoft.sqlserver.jdbc.SQLServerDriver");

        } catch (IOException e) {
            System.out.println("Error loading configuration: " + e.getMessage());
            setDefaultConfiguration();
        }
    }

    /**
     * Set default database configuration
     */
    private static void setDefaultConfiguration() {
        DB_URL = "jdbc:sqlserver://localhost:1433;databaseName=QuanLyCuaHangSach;encrypt=false";
        DB_USER = "sa";
        DB_PASSWORD = "123";
        DB_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    }

    /**
     * Initialize DBCP2 DataSource with connection pooling
     */
    private static void initializeDataSource() {
        dataSource = new BasicDataSource();

        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            System.err.println("SQL Server Driver not found: " + e.getMessage());
        }

        dataSource.setDriverClassName(DB_DRIVER);
        dataSource.setUrl(DB_URL);
        dataSource.setUsername(DB_USER);
        dataSource.setPassword(DB_PASSWORD);

        // Connection Pool Configuration
        dataSource.setInitialSize(5);
        dataSource.setMaxTotal(20);
        dataSource.setMaxIdle(10);
        dataSource.setMinIdle(5);
        dataSource.setMaxWaitMillis(10000);
        dataSource.setTestOnBorrow(true);
        dataSource.setTestOnReturn(false);
        dataSource.setTestWhileIdle(true);
        dataSource.setValidationQuery("SELECT 1");
        dataSource.setValidationQueryTimeout(5);
    }

    /**
     * Get database connection from pool
     * @return Connection object
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    /**
     * Close a connection (return to pool)
     * @param connection Connection to close
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }

    /**
     * Test database connection
     * @return true if connection successful
     */
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Database connection test failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get DataSource for advanced operations
     * @return BasicDataSource
     */
    public static BasicDataSource getDataSource() {
        return dataSource;
    }

    /**
     * Shutdown connection pool
     */
    public static void shutdown() {
        try {
            if (dataSource != null) {
                dataSource.close();
            }
        } catch (SQLException e) {
            System.err.println("Error shutting down connection pool: " + e.getMessage());
        }
    }
}
