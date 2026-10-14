package com.shop.util;

import java.math.BigDecimal;
import java.util.regex.Pattern;

/**
 * Provides server-side validation for user and domain input.
 */
public final class ValidationUtil {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    private ValidationUtil() {
    }

    /**
     * Validates a username.
     *
     * @param username username to validate
     * @throws IllegalArgumentException if the value is invalid
     */
    public static void validateUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (username.length() < 3 || username.length() > 50) {
            throw new IllegalArgumentException("Username must be between 3 and 50 characters");
        }
    }

    /**
     * Validates a password.
     *
     * @param password password to validate
     * @throws IllegalArgumentException if the value is invalid
     */
    public static void validatePassword(String password) {
        if (password == null || password.length() < 6 || password.length() > 100) {
            throw new IllegalArgumentException("Password must be between 6 and 100 characters");
        }
    }

    /**
     * Validates an email address.
     *
     * @param email email to validate
     * @throws IllegalArgumentException if the value is invalid
     */
    public static void validateEmail(String email) {
        if (email == null || email.length() > 100 || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("Please enter a valid email");
        }
    }

    /**
     * Validates product data.
     *
     * @param name product name
     * @param description product description
     * @param price product price
     * @param quantity product quantity
     * @throws IllegalArgumentException if any value is invalid
     */
    public static void validateProduct(String name, String description,
                                       BigDecimal price, int quantity) {
        if (name == null || name.isBlank() || name.length() > 150) {
            throw new IllegalArgumentException("Product name is required and must not exceed 150 characters");
        }
        if (description != null && description.length() > 2000) {
            throw new IllegalArgumentException("Product description is too long");
        }
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price must be non-negative");
        }
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
    }

    /**
     * Validates an order quantity.
     *
     * @param quantity requested quantity
     * @throws IllegalArgumentException if the quantity is invalid
     */
    public static void validateQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
    }
}
