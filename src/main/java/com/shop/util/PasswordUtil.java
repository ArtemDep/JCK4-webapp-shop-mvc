package com.shop.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Utility for securely hashing and checking passwords with BCrypt.
 */
public final class PasswordUtil {

    private PasswordUtil() {
    }

    /**
     * Creates a BCrypt password hash.
     *
     * @param password plain text password
     * @return BCrypt hash
     */
    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }

    /**
     * Checks a plain password against a BCrypt hash.
     *
     * @param password plain text password
     * @param hash stored BCrypt hash
     * @return true when the password matches
     */
    public static boolean checkPassword(String password, String hash) {
        if (password == null || hash == null || hash.isBlank()) {
            return false;
        }
        return BCrypt.checkpw(password, hash);
    }
}
