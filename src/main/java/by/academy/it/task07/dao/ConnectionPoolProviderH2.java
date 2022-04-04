package by.academy.it.task07.dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class ConnectionPoolProviderH2 implements ConnectionProvider {
    private static final String DATABASE_CONFIG_FILE_NAME = "databaseH2";
    private static final String URL_ALIAS = "url";
    private static final String USER_ALIAS = "user";
    private static final String PASSWORD_ALIAS = "password";
    private static HikariDataSource dataSource;

    public ConnectionPoolProviderH2() {
    }

    public Connection getConnection() throws EntityDaoException {
        if (dataSource == null) {
            ResourceBundle bundle = ResourceBundle.getBundle(DATABASE_CONFIG_FILE_NAME);
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(bundle.getString(URL_ALIAS));
            config.setUsername(bundle.getString(USER_ALIAS));
            config.setPassword(bundle.getString(PASSWORD_ALIAS));
            dataSource = new HikariDataSource(config);

            //задаем таблицу person во временную БД
            try {
                Connection connection = dataSource.getConnection();
                Statement stmt = connection.createStatement();
                stmt.execute("create table person (\n" +
                        "    id int auto_increment primary key,\n" +
                        "    identifier int,\n" +
                        "    surname varchar(50),\n" +
                        "    name varchar(50)\n" +
                        ");");
            } catch (SQLException e) {
                throw new EntityDaoException(e);
            }

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

