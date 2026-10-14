package com.shop.controller;

import com.shop.model.User;
import com.shop.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Controller for viewing, creating and cancelling customer orders.
 */
@WebServlet("/orders")
public class OrderServlet extends HttpServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderServlet.class);
    private final OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            User user = currentUser(request);
            request.setAttribute("orders", orderService.findByUser(user.getId()));
            request.getRequestDispatcher("/WEB-INF/views/orders.jsp").forward(request, response);
        } catch (Exception e) {
            LOGGER.error("Failed to load orders", e);
            request.setAttribute("error", "Failed to load orders.");
            request.getRequestDispatcher("/WEB-INF/views/orders.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = currentUser(request);
        try {
            String action = request.getParameter("action");
            if ("create".equals(action)) {
                orderService.create(
                        user.getId(),
                        parseLong(request.getParameter("productId"), "Invalid product ID"),
                        parseInt(request.getParameter("quantity"), "Quantity must be an integer")
                );
            } else if ("cancel".equals(action)) {
                orderService.cancel(
                        parseLong(request.getParameter("id"), "Invalid order ID"),
                        user.getId()
                );
            } else {
                throw new IllegalArgumentException("Unknown operation");
            }
            response.sendRedirect(request.getContextPath() + "/orders");
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            doGet(request, response);
        } catch (Exception e) {
            LOGGER.error("Order operation failed for user {}", user.getId(), e);
            request.setAttribute("error", "Order operation failed.");
            doGet(request, response);
        }
    }

    private User currentUser(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            throw new IllegalStateException("User not authenticated");
        }
        return user;
    }

    private long parseLong(String value, String message) {
        try {
            return Long.parseLong(value);
        } catch (Exception e) {
            throw new IllegalArgumentException(message);
        }
    }

    private int parseInt(String value, String message) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            throw new IllegalArgumentException(message);
        }
    }
}
