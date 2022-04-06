package by.academy.it.task07.dao;

public class EntityDaoException extends Exception {
    /**
     * Very strange and rare exception.
     */
    public EntityDaoException() {
    }

    /**
     * Very strange and rare exception, but with some string param.
     *
     * @param message some message
     */
    public EntityDaoException(final String message) {
        super(message);
    }

    /**
     * Very strange and rare exception, but with some string param.
     * Also have some cause param
     *
     * @param message some message
     * @param cause   some cause
     */
    public EntityDaoException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Very strange and rare exception, but with some cause param.
     *
     * @param cause some cause
     */
    public EntityDaoException(final Throwable cause) {
        super(cause);
    }
}
