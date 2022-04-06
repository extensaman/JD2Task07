package by.academy.it.task07.dao;

import java.util.Map;

public interface EntityDao {
    /**
     * Select method.
     * @return Map with ID as key and Object as value
     * @throws EntityDaoException Some rare exception
     */
    Map<Long, Object> select() throws EntityDaoException;

    void update(Long id, Object entity) throws EntityDaoException;

    void delete(Long id) throws EntityDaoException;

    void insert(Object entity) throws EntityDaoException;
}
