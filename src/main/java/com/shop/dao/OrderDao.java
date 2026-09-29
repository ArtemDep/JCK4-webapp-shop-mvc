package com.shop.dao;

import com.shop.model.Order;
import com.shop.util.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for orders. Order creation and cancellation use database transactions.
 */
public class OrderDao {

    /**
     * Creates an order and decreases product stock atomically.
     *
     * @param userId user identifier
     * @param productId product identifier
     * @param quantity quantity
     * @param total total price
     * @throws SQLException when database access fails
     */
    public void create(long userId, long productId, int quantity, BigDecimal total) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection()) {
            connection.setAutoCommit(false);
            try {
                String lockSql = "SELECT quantity FROM products WHERE id = ? FOR UPDATE";
                try (PreparedStatement statement = connection.prepareStatement(lockSql)) {
                    statement.setLong(1, productId);
                    try (ResultSet resultSet = statement.executeQuery()) {
                        if (!resultSet.next() || resultSet.getInt(1) < quantity) {
                            throw new IllegalArgumentException("Недостаточное количество товара");
                        }
                    }
                }

                String insertSql = "INSERT INTO orders(user_id, product_id, quantity, total_price) "
                        + "VALUES(?, ?, ?, ?)";
                try (PreparedStatement statement = connection.prepareStatement(insertSql)) {
                    statement.setLong(1, userId);
                    statement.setLong(2, productId);
                    statement.setInt(3, quantity);
                    statement.setBigDecimal(4, total);
                    statement.executeUpdate();
                }

                String updateSql = "UPDATE products SET quantity = quantity - ? WHERE id = ?";
                try (PreparedStatement statement = connection.prepareStatement(updateSql)) {
                    statement.setInt(1, quantity);
                    statement.setLong(2, productId);
                    statement.executeUpdate();
                }
                connection.commit();
            } catch (Exception e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    /**
     * Finds orders belonging to a user.
     *
     * @param userId user identifier
     * @return user's orders
     * @throws SQLException when database access fails
     */
    public List<Order> findByUser(long userId) throws SQLException {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT o.id, o.user_id, o.product_id, p.name, o.quantity, "
                + "o.total_price, o.status, o.created_at "
                + "FROM orders o JOIN products p ON p.id = o.product_id "
                + "WHERE o.user_id = ? ORDER BY o.created_at DESC";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Order order = new Order();
                    order.setId(resultSet.getLong("id"));
                    order.setUserId(resultSet.getLong("user_id"));
                    order.setProductId(resultSet.getLong("product_id"));
                    order.setProductName(resultSet.getString("name"));
                    order.setQuantity(resultSet.getInt("quantity"));
                    order.setTotalPrice(resultSet.getBigDecimal("total_price"));
                    order.setStatus(resultSet.getString("status"));
                    order.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
                    orders.add(order);
                }
            }
        }
        return orders;
    }

    /**
     * Cancels a user's active order and restores stock atomically.
     *
     * @param orderId order identifier
     * @param userId user identifier
     * @throws SQLException when database access fails
     */
    public void cancel(long orderId, long userId) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection()) {
            connection.setAutoCommit(false);
            try {
                Long productId;
                int quantity;
                String selectSql = "SELECT product_id, quantity FROM orders "
                        + "WHERE id = ? AND user_id = ? AND status = 'CREATED' FOR UPDATE";
                try (PreparedStatement statement = connection.prepareStatement(selectSql)) {
                    statement.setLong(1, orderId);
                    statement.setLong(2, userId);
                    try (ResultSet resultSet = statement.executeQuery()) {
                        if (!resultSet.next()) {
                            throw new IllegalArgumentException("Заказ не найден или уже отменен");
                        }
                        productId = resultSet.getLong("product_id");
                        quantity = resultSet.getInt("quantity");
                    }
                }

                String updateOrderSql = "UPDATE orders SET status = 'CANCELLED' "
                        + "WHERE id = ? AND user_id = ?";
                try (PreparedStatement statement = connection.prepareStatement(updateOrderSql)) {
                    statement.setLong(1, orderId);
                    statement.setLong(2, userId);
                    statement.executeUpdate();
                }

                String updateProductSql = "UPDATE products SET quantity = quantity + ? WHERE id = ?";
                try (PreparedStatement statement = connection.prepareStatement(updateProductSql)) {
                    statement.setInt(1, quantity);
                    statement.setLong(2, productId);
                    statement.executeUpdate();
                }
                connection.commit();
            } catch (Exception e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }
}
