package com.shop.dao;

import com.shop.model.Product;
import com.shop.util.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for products using JDBC prepared statements.
 */
public class ProductDao {

    /**
     * Returns all products ordered by identifier.
     *
     * @return products
     * @throws SQLException when database access fails
     */
    public List<Product> findAll() throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT id, name, description, price, quantity FROM products ORDER BY id";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                products.add(map(resultSet));
            }
        }
        return products;
    }

    /**
     * Finds a product by identifier.
     *
     * @param id product identifier
     * @return product or null
     * @throws SQLException when database access fails
     */
    public Product findById(long id) throws SQLException {
        String sql = "SELECT id, name, description, price, quantity FROM products WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? map(resultSet) : null;
            }
        }
    }

    /**
     * Creates a product.
     *
     * @param name product name
     * @param description product description
     * @param price product price
     * @param quantity stock quantity
     * @throws SQLException when database access fails
     */
    public void create(String name, String description, BigDecimal price, int quantity) throws SQLException {
        String sql = "INSERT INTO products(name, description, price, quantity) VALUES(?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setString(2, description);
            statement.setBigDecimal(3, price);
            statement.setInt(4, quantity);
            statement.executeUpdate();
        }
    }

    /**
     * Deletes a product.
     *
     * @param id product identifier
     * @throws SQLException when database access fails
     */
    public void delete(long id) throws SQLException {
        String sql = "DELETE FROM products WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        }
    }

    private Product map(ResultSet resultSet) throws SQLException {
        Product product = new Product();
        product.setId(resultSet.getLong("id"));
        product.setName(resultSet.getString("name"));
        product.setDescription(resultSet.getString("description"));
        product.setPrice(resultSet.getBigDecimal("price"));
        product.setQuantity(resultSet.getInt("quantity"));
        return product;
    }
}
