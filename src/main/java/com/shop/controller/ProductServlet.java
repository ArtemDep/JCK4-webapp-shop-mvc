package com.shop.controller;

import com.shop.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * Controller for product viewing and administrator product management.
 */
@WebServlet("/products")
public class ProductServlet extends HttpServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductServlet.class);
    private final ProductService productService = new ProductService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setAttribute("products", productService.findAll());
            request.getRequestDispatcher("/WEB-INF/views/products.jsp").forward(request, response);
        } catch (Exception e) {
            LOGGER.error("Failed to load products", e);
            request.setAttribute("error", "Failed to load products.");
            request.getRequestDispatcher("/WEB-INF/views/products.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        try {
            if ("add".equals(action)) {
                productService.create(
                        request.getParameter("name"),
                        request.getParameter("description"),
                        parsePrice(request.getParameter("price")),
                        parseQuantity(request.getParameter("quantity"))
                );
            } else if ("delete".equals(action)) {
                productService.delete(parseLong(request.getParameter("id"), "Invalid product ID"));
            } else {
                throw new IllegalArgumentException("Unknown operation");
            }
            response.sendRedirect(request.getContextPath() + "/products");
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            doGet(request, response);
        } catch (Exception e) {
            LOGGER.error("Product operation failed: {}", action, e);
            request.setAttribute("error", "Product operation failed.");
            doGet(request, response);
        }
    }

    private BigDecimal parsePrice(String value) {
        try {
            return new BigDecimal(value);
        } catch (Exception e) {
            throw new IllegalArgumentException("Price must be a valid number");
        }
    }

    private int parseQuantity(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            throw new IllegalArgumentException("Quantity must be an integer");
        }
    }

    private long parseLong(String value, String message) {
        try {
            return Long.parseLong(value);
        } catch (Exception e) {
            throw new IllegalArgumentException(message);
        }
    }
}
