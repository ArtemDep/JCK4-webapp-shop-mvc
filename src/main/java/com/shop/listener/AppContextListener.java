package com.shop.listener;

import com.shop.util.DatabaseConnection;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Initializes and closes shared application resources.
 */
@WebListener
public class AppContextListener implements ServletContextListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppContextListener.class);

    @Override
    public void contextInitialized(ServletContextEvent event) {
        try {
            DatabaseConnection.init();
            LOGGER.info("Shop MVC application started");
        } catch (RuntimeException e) {
            LOGGER.error("Application startup failed", e);
            throw e;
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        DatabaseConnection.close();
        LOGGER.info("Shop MVC application stopped");
    }
}
