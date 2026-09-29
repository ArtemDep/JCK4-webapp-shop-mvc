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
            request.setAttribute("error", "Не удалось загрузить товары.");
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
                productService.delete(parseLong(request.getParameter("id"), "Некорректный ID товара"));
            } else {
                throw new IllegalArgumentException("Неизвестная операция");
            }
            response.sendRedirect(request.getContextPath() + "/products");
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            doGet(request, response);
        } catch (Exception e) {
            LOGGER.error("Product operation failed: {}", action, e);
            request.setAttribute("error", "Операция с товаром не выполнена.");
            doGet(request, response);
        }
    }

    private BigDecimal parsePrice(String value) {
        try {
            return new BigDecimal(value);
        } catch (Exception e) {
            throw new IllegalArgumentException("Цена должна быть корректным числом");
        }
    }

    private int parseQuantity(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            throw new IllegalArgumentException("Количество должно быть целым числом");
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
