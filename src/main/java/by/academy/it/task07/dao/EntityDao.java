package by.academy.it.task07.dao;

import java.util.Map;

public interface EntityDao {
    /**
     * Select method.
     *
     * @return Map with ID as key and Object as value
     * @throws EntityDaoException Some rare exception
     */
    Map<Long, Object> select() throws EntityDaoException;

    /**
     * Update method.
     *
     * @param id     Id from database to update
     * @param entity Entity for update command
     * @throws EntityDaoException Some rare exception
     */
    void update(Long id, Object entity) throws EntityDaoException;

    /**
     * Delete method.
     * @param id Id to delete from database
     * @throws EntityDaoException Some rare exception
     */
    void delete(Long id) throws EntityDaoException;

    /**
     * Insert method.
     * @param entity Entity to insert in database
     * @throws EntityDaoException Some rare exception
     */
    void insert(Object entity) throws EntityDaoException;
}
