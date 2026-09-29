package com.shop.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/** Tests password hashing and verification. */
class PasswordUtilTest {

    @Test
    void shouldHashAndVerifyPassword() {
        String hash = PasswordUtil.hashPassword("admin123");

        assertNotEquals("admin123", hash);
        assertTrue(PasswordUtil.checkPassword("admin123", hash));
        assertFalse(PasswordUtil.checkPassword("wrong", hash));
    }
}
