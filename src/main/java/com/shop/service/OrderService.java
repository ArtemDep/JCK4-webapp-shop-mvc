package com.shop.service;

import com.shop.dao.OrderDao;
import com.shop.dao.ProductDao;
import com.shop.model.Order;
import com.shop.model.Product;
import com.shop.util.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

/**
 * Contains business operations for customer orders.
 */
public class OrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);
    private final OrderDao orders;
    private final ProductDao products;

    /**
     * Creates an order service with default DAOs.
     */
    public OrderService() {
        this(new OrderDao(), new ProductDao());
    }

    /**
     * Creates an order service with DAO dependencies.
     *
     * @param orders order DAO
     * @param products product DAO
     */
    public OrderService(OrderDao orders, ProductDao products) {
        this.orders = orders;
        this.products = products;
    }

    /**
     * Creates an order and calculates its total price.
     *
     * @param userId user identifier
     * @param productId product identifier
     * @param quantity requested quantity
     * @throws SQLException when database access fails
     */
    public void create(long userId, long productId, int quantity) throws SQLException {
        if (userId <= 0 || productId <= 0) {
            throw new IllegalArgumentException("Некорректные данные заказа");
        }
        ValidationUtil.validateQuantity(quantity);

        Product product = products.findById(productId);
        if (product == null) {
            throw new IllegalArgumentException("Товар не найден");
        }
        if (quantity > product.getQuantity()) {
            throw new IllegalArgumentException("Недостаточное количество товара");
        }

        BigDecimal total = product.getPrice().multiply(BigDecimal.valueOf(quantity));
        orders.create(userId, productId, quantity, total);
        LOGGER.info("Order created: user={}, product={}, quantity={}", userId, productId, quantity);
    }

    /**
     * Finds orders belonging to a user.
     *
     * @param userId user identifier
     * @return user's orders
     * @throws SQLException when database access fails
     */
    public List<Order> findByUser(long userId) throws SQLException {
        return orders.findByUser(userId);
    }

    /**
     * Cancels a user's order.
     *
     * @param orderId order identifier
     * @param userId user identifier
     * @throws SQLException when database access fails
     */
    public void cancel(long orderId, long userId) throws SQLException {
        if (orderId <= 0 || userId <= 0) {
            throw new IllegalArgumentException("Некорректный идентификатор заказа");
        }
        orders.cancel(orderId, userId);
        LOGGER.info("Order cancelled: order={}, user={}", orderId, userId);
    }
}
