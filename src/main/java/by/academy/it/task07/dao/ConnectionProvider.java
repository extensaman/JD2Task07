package by.academy.it.task07.dao;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionProvider {

    default Connection getConnection() throws EntityDaoException, SQLException {
        return null;
    }

}
