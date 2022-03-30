package by.academy.it.task07.dao;

import java.util.List;

public interface EntityDao {
    List select() throws EntityDaoException;

    int update(int id, Object[] params) throws EntityDaoException;

    int delete(int id) throws EntityDaoException;

    int insert(Object entity) throws EntityDaoException;

    default void closeDao() throws EntityDaoException {
    };
}
