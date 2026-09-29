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
            request.setAttribute("error", "Не удалось загрузить заказы.");
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
                        parseLong(request.getParameter("productId"), "Некорректный ID товара"),
                        parseInt(request.getParameter("quantity"), "Количество должно быть целым числом")
                );
            } else if ("cancel".equals(action)) {
                orderService.cancel(
                        parseLong(request.getParameter("id"), "Некорректный ID заказа"),
                        user.getId()
                );
            } else {
                throw new IllegalArgumentException("Неизвестная операция");
            }
            response.sendRedirect(request.getContextPath() + "/orders");
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            doGet(request, response);
        } catch (Exception e) {
            LOGGER.error("Order operation failed for user {}", user.getId(), e);
            request.setAttribute("error", "Операция с заказом не выполнена.");
            doGet(request, response);
        }
    }

    private User currentUser(HttpServletRequest request) {
        return (User) request.getSession().getAttribute("user");
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
