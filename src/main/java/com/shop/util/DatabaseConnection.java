package com.shop.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Provides JDBC connections from a HikariCP connection pool.
 */
public final class DatabaseConnection {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseConnection.class);
    private static HikariDataSource dataSource;

    private DatabaseConnection() {
    }

    /**
     * Initializes the connection pool from db.properties.
     */
    public static void init() {
        if (dataSource != null) {
            return;
        }
        try {
            Properties properties = new Properties();
            try (InputStream in = DatabaseConnection.class.getClassLoader()
                    .getResourceAsStream("db.properties")) {
                if (in == null) {
                    throw new IllegalStateException("db.properties not found");
                }
                properties.load(in);
            }

            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(properties.getProperty("db.url"));
            config.setUsername(properties.getProperty("db.user"));
            config.setPassword(properties.getProperty("db.password"));
            config.setMaximumPoolSize(10);
            config.setMinimumIdle(2);
            config.setPoolName("ShopMvcPool");

            dataSource = new HikariDataSource(config);
            LOGGER.info("Database connection pool initialized");
        } catch (Exception e) {
            LOGGER.error("Database initialization failed", e);
            throw new IllegalStateException("Database initialization error", e);
        }
    }

    /**
     * Obtains a JDBC connection from the pool.
     *
     * @return pooled JDBC connection
     * @throws SQLException when a connection cannot be obtained
     */
    public static Connection getConnection() throws SQLException {
        if (dataSource == null) {
            throw new IllegalStateException("Database pool is not initialized");
        }
        return dataSource.getConnection();
    }

    /**
     * Closes the connection pool during application shutdown.
     */
    public static void close() {
        if (dataSource != null) {
            dataSource.close();
            dataSource = null;
            LOGGER.info("Database connection pool closed");
        }
    }
}
