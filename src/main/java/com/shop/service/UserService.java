package com.shop.service;

import com.shop.dao.UserDao;
import com.shop.model.User;
import com.shop.util.PasswordUtil;
import com.shop.util.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

/**
 * Contains business operations related to users and authentication.
 */
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private final UserDao userDao;

    /**
     * Creates a service with the default DAO.
     */
    public UserService() {
        this(new UserDao());
    }

    /**
     * Creates a service with a DAO dependency.
     *
     * @param userDao DAO used by this service
     */
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    /**
     * Authenticates a user.
     *
     * @param username login
     * @param password plain password
     * @return authenticated user or null when credentials are invalid
     * @throws SQLException when database access fails
     */
    public User authenticate(String username, String password) throws SQLException {
        if (username == null || username.isBlank() || password == null) {
            return null;
        }
        User user = userDao.findByUsername(username.strip());
        return user != null && PasswordUtil.checkPassword(password, user.getPassword())
                ? user : null;
    }

    /**
     * Registers a new regular user.
     *
     * @param username login
     * @param password plain password
     * @param email email address
     * @throws SQLException when database access fails
     */
    public void register(String username, String password, String email) throws SQLException {
        username = username == null ? null : username.strip();
        email = email == null ? null : email.strip();
        ValidationUtil.validateUsername(username);
        ValidationUtil.validatePassword(password);
        ValidationUtil.validateEmail(email);

        if (userDao.findByUsername(username) != null) {
            throw new IllegalArgumentException("User already exists");
        }

        userDao.create(new User(username, PasswordUtil.hashPassword(password), email));
        LOGGER.info("User registered: {}", username);
    }

    /**
     * Updates a user's email address.
     *
     * @param userId user identifier
     * @param email new email
     * @throws SQLException when database access fails
     */
    public void updateEmail(long userId, String email) throws SQLException {
        email = email == null ? null : email.strip();
        ValidationUtil.validateEmail(email);
        userDao.updateEmail(userId, email);
    }
}
