package by.academy.it.task07.dao;

import java.sql.Connection;

public interface ConnectionProvider {

      default Connection getConnection() throws EntityDaoException{
        return null;
    };

}
