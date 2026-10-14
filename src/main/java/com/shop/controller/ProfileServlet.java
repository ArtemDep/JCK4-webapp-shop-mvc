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
 * Handles viewing and editing of the current user's profile.
 */
@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProfileServlet.class);
    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        try {
            String email = request.getParameter("email");
            userService.updateEmail(user.getId(), email);
            user.setEmail(email.strip());
            response.sendRedirect(request.getContextPath() + "/profile");
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            doGet(request, response);
        } catch (Exception e) {
            LOGGER.error("Profile update failed for user {}", user.getId(), e);
            request.setAttribute("error", "Failed to update profile.");
            doGet(request, response);
        }
    }
}
