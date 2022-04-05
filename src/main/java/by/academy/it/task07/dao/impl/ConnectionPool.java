package by.academy.it.task07.dao.impl;

import by.academy.it.task07.dao.EntityDaoException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ConnectionPool {
    private ResourceBundle bundle;
    private static final String URL_ALIAS = "url";
    private static final String USER_ALIAS = "user";
    private static final String PASSWORD_ALIAS = "password";
    private HikariDataSource dataSource;


    public ConnectionPool(ResourceBundle bundle) {
        this.bundle = bundle;
    }


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
