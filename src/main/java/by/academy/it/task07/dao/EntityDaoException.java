package by.academy.it.task07.dao;

public class EntityDaoException extends Exception {
    public EntityDaoException() {
    }

    public EntityDaoException(String message) {
        super(message);
    }

    public EntityDaoException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntityDaoException(Throwable cause) {
        super(cause);
    }
}
