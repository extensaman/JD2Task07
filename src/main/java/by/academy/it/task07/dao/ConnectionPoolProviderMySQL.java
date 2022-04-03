package by.academy.it.task07.dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ConnectionPoolProviderMySQL implements ConnectionProvider{
    public static final String DATABASE_CONFIG_FILE_NAME = "database";
    public static final String URL_ALIAS = "url";
    public static final String USER_ALIAS = "user";
    public static final String PASSWORD_ALIAS = "password";
    private static HikariDataSource dataSource;


    public ConnectionPoolProviderMySQL() {
           }


     public Connection getConnection() throws EntityDaoException {

        if (dataSource == null) {
            ResourceBundle bundle = ResourceBundle.getBundle(DATABASE_CONFIG_FILE_NAME);
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
