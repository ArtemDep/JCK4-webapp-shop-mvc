package com.shop.dao;

import com.shop.model.User;
import com.shop.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DAO for users. All database access is performed with JDBC prepared statements.
 */
public class UserDao {

    /**
     * Finds a user by login.
     *
     * @param username login
     * @return matching user or null
     * @throws SQLException when database access fails
     */
    public User findByUsername(String username) throws SQLException {
        String sql = "SELECT id, username, password, email, role FROM users WHERE username = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? map(resultSet) : null;
            }
        }
    }

    /**
     * Creates a regular user.
     *
     * @param user user to persist
     * @throws SQLException when database access fails
     */
    public void create(User user) throws SQLException {
        String sql = "INSERT INTO users(username, password, email, role) VALUES(?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getRole().name());
            statement.executeUpdate();
        }
    }

    /**
     * Updates a user's email.
     *
     * @param id user identifier
     * @param email new email
     * @throws SQLException when database access fails
     */
    public void updateEmail(long id, String email) throws SQLException {
        String sql = "UPDATE users SET email = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            statement.setLong(2, id);
            statement.executeUpdate();
        }
    }

    private User map(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getLong("id"));
        user.setUsername(resultSet.getString("username"));
        user.setPassword(resultSet.getString("password"));
        user.setEmail(resultSet.getString("email"));
        String roleStr = resultSet.getString("role");
        user.setRole(roleStr != null ? UserRole.valueOf(roleStr) : null);
        return user;
    }
}
