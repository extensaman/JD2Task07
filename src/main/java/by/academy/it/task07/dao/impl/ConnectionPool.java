package by.academy.it.task07.dao.impl;

import by.academy.it.task07.dao.EntityDaoException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

public final class ConnectionPool {
    /**
     * Resource bundle to get needed param from property file.
     */
    private final ResourceBundle bundle;
    /**
     * This is a constant for resource bundle for url param.
     */
    private static final String URL_ALIAS = "url";
    /**
     * This is a constant for resource bundle for user param.
     */
    private static final String USER_ALIAS = "user";
    /**
     * This is a constant for resource bundle for password param.
     */
    private static final String PASSWORD_ALIAS = "password";
    /**
     * THis is Hikari database driver.
     */
    private HikariDataSource dataSource;

    /**
     * Constructor for our connection pool.
     * @param incomingBundle Resource bundle for our pool
     */
    public ConnectionPool(final ResourceBundle incomingBundle) {
        this.bundle = incomingBundle;
    }


    /**
     * Method to create connection with database by Hikari driver.
     * @return Connection
     * @throws EntityDaoException Some very rare exception
     */
    public Connection getConnection() throws EntityDaoException {
        if (dataSource == null) {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(bundle.getString(URL_ALIAS));
            config.setUsername(bundle.getString(USER_ALIAS));
            config.setPassword(bundle.getString(PASSWORD_ALIAS));
            dataSource = new HikariDataSource(config);
        }
        Connection connection;
        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            throw new EntityDaoException(e);
        }
        return connection;
    }
}
