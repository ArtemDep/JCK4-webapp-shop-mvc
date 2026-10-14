package com.shop.service;

import com.shop.dao.ProductDao;
import com.shop.model.Product;
import com.shop.util.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

/**
 * Contains business operations for products.
 */
public class ProductService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductService.class);
    private final ProductDao productDao;

    /**
     * Creates a service with the default DAO.
     */
    public ProductService() {
        this(new ProductDao());
    }

    /**
     * Creates a service with a DAO dependency.
     *
     * @param productDao DAO used by this service
     */
    public ProductService(ProductDao productDao) {
        this.productDao = productDao;
    }

    /**
     * Returns all products.
     *
     * @return product list
     * @throws SQLException when database access fails
     */
    public List<Product> findAll() throws SQLException {
        return productDao.findAll();
    }

    /**
     * Adds a product after validating its fields.
     *
     * @param name product name
     * @param description product description
     * @param price product price
     * @param quantity stock quantity
     * @throws SQLException when database access fails
     */
    public void create(String name, String description, BigDecimal price, int quantity) throws SQLException {
        ValidationUtil.validateProduct(name, description, price, quantity);
        productDao.create(name.strip(), description == null ? null : description.trim(), price, quantity);
        LOGGER.info("Product created: {}", name);
    }

    /**
     * Deletes a product by identifier.
     *
     * @param id product identifier
     * @throws SQLException when database access fails
     */
    public void delete(long id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid product ID");
        }
        productDao.delete(id);
        LOGGER.info("Product deleted: {}", id);
    }
}
