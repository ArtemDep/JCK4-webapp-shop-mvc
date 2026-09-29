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
            throw new IllegalArgumentException("Логин не может быть пустым");
        }
        if (username.length() < 3 || username.length() > 50) {
            throw new IllegalArgumentException("Логин должен содержать от 3 до 50 символов");
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
            throw new IllegalArgumentException("Пароль должен содержать от 6 до 100 символов");
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
            throw new IllegalArgumentException("Введите корректный email");
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
            throw new IllegalArgumentException("Название товара обязательно и должно содержать не более 150 символов");
        }
        if (description != null && description.length() > 2000) {
            throw new IllegalArgumentException("Описание товара слишком длинное");
        }
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Цена должна быть неотрицательной");
        }
        if (quantity < 0) {
            throw new IllegalArgumentException("Количество не может быть отрицательным");
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
            throw new IllegalArgumentException("Количество должно быть больше нуля");
        }
    }
}
