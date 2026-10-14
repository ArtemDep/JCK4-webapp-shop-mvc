package com.shop.controller;

import com.shop.model.User;
import com.shop.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Handles user sign in.
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginServlet.class);
    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            User user = userService.authenticate(username, password);
            if (user == null) {
                request.setAttribute("error", "Invalid username or password");
                doGet(request, response);
                return;
            }

            request.getSession(true).setAttribute("user", user);
            response.sendRedirect(request.getContextPath() + "/products");
        } catch (Exception e) {
            LOGGER.error("Login failed for user: {}", username, e);
            request.setAttribute("error", "Login failed. Please try again later.");
            doGet(request, response);
        }
    }
}
