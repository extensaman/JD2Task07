package by.academy.it.task07.dao.impl;

import by.academy.it.task07.dao.EntityDao;
import by.academy.it.task07.dao.EntityDaoException;
import by.academy.it.task07.entity.MyColumn;
import by.academy.it.task07.entity.MyTable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class EntityDaoImpl implements EntityDao {
    /**
     * Template for select statement.
     */
    private static final String SELECT_STATEMENT =
            "SELECT * FROM %s";
    /**
     * Template for delete statement.
     */
    private static final String DELETE_STATEMENT =
            "DELETE FROM %s WHERE id = %d";
    /**
     * Template for insert statement.
     */
    private static final String INSERT_STATEMENT =
            "INSERT INTO %s (%s) VALUES (%s)";
    /**
     * Template for update statement.
     */
    private static final String UPDATE_STATEMENT =
            "UPDATE %s SET %s WHERE id = %d";
    /**
     * Single quote sign for SQL requests.
     */
    private static final String SINGLE_QUOTE_SIGN =
            "'";
    /**
     * Comma sign for SQL requests.
     */
    private static final String COMMA_SIGN = ",";
    /**
     * Equal sign for SQL requests.
     */
    private static final String EQUAL_SIGN = "=";
    /**
     * Table name for database.
     * Get from bean class by reflection.
     */
    private final String tableName;
    /**
     * Table column names.
     */
    private final String[] tableColumnNames;
    /**
     * Class fields names.
     */
    private final String[] classFieldNames;
    /**
     * Constructor for bean class.
     */
    private final Constructor constructor;
    /**
     * Connection pool for DAO.
     */
    private final ConnectionPool connectionPool;

    /**
     * Constructor for DAO implementation.
     *
     * @param aClass                 Bean class
     * @param incomingConnectionPool Connection pool
     * @throws EntityDaoException Some rare exception
     */
    public EntityDaoImpl(final Class aClass,
                         final ConnectionPool incomingConnectionPool)
            throws EntityDaoException {

        this.connectionPool = incomingConnectionPool;

        // check class for ability to persistence
        if (aClass.isAnnotationPresent(MyTable.class)) {
            tableName = ((MyTable) aClass.getAnnotation(MyTable.class)).name();
        } else {
            throw new EntityDaoException("No 'MyTable' annotation");
        }

        // looking for annotated fields
        Field[] fields = aClass.getDeclaredFields();
        int fieldCount = 0;
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(MyColumn.class)) {
                fieldCount++;
            }
            field.setAccessible(false);
        }

        tableColumnNames = new String[fieldCount];
        classFieldNames = new String[fieldCount];
        Class[] classFieldTypes = new Class[fieldCount];
        int index = 0;
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(MyColumn.class)) {
                tableColumnNames[index] =
                        ((MyColumn) field.getAnnotation(MyColumn.class)).name();
                classFieldTypes[index] = field.getType();
                classFieldNames[index++] = field.getName();
            }
            field.setAccessible(false);
        }

        // get constructor for ability of creation new instances
        // of persistent class
        try {
            constructor = aClass.getConstructor(classFieldTypes);
        } catch (NoSuchMethodException e) {
            throw new EntityDaoException(e);
        }
    }

    /**
     * Create new instance of persistent class.
     *
     * @param params - values of class fields
     * @return new instance of persistent class
     * @throws EntityDaoException
     */
    private Object createEntity(final Object[] params)
            throws EntityDaoException {
        Object entity = null;
        try {
            entity = constructor.newInstance(params);
        } catch (InstantiationException
                | IllegalAccessException
                | InvocationTargetException e) {
            throw new EntityDaoException(e);
        }
        return entity;
    }

    @Override
    public Map<Long, Object> select() throws EntityDaoException {
        Map<Long, Object> map = new HashMap<>();
        try (
                Connection connection = connectionPool.getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet =
                        // SELECT * FROM %s
                        statement.executeQuery(
                                String.format(SELECT_STATEMENT, tableName));
        ) {
            Object[] paramsForCreation = new Object[classFieldNames.length];
            while (resultSet.next()) {
                for (int i = 0; i < tableColumnNames.length; i++) {
                    paramsForCreation[i] = resultSet.getObject(i + 2);
                }
                map.put(resultSet.getLong(1), createEntity(paramsForCreation));
            }
        } catch (SQLException e) {
            throw new EntityDaoException(e);
        }
        return map;
    }

    @Override
    public void update(final Long id, final Object entity)
            throws EntityDaoException {
        int updatedCount = 0;
        String[] params = getObjectParam(entity);

        try (
                Connection connection = connectionPool.getConnection();
                Statement statement = connection.createStatement();
        ) {
            updatedCount =
                    // UPDATE %s SET %s WHERE id = %d
                    statement.executeUpdate(
                            String.format(UPDATE_STATEMENT,
                                    tableName,
                                    IntStream.range(0,
                                                    tableColumnNames
                                                            .length)
                                            .mapToObj(i ->
                                                    tableColumnNames[i]
                                                            .concat(EQUAL_SIGN)
                                                            .concat(params[i]))
                                            .collect(Collectors
                                                    .joining(COMMA_SIGN)),
                                    id
                            ));
        } catch (SQLException e) {
            throw new EntityDaoException(e);
        }

        if (updatedCount != 1) {
            throw new EntityDaoException("Updating transaction failed "
                    + "(updatedCount != 1)");
        }
    }

    @Override
    public void delete(final Long id) throws EntityDaoException {
        int deletedCount = 0;
        try (
                Connection connection = connectionPool.getConnection();
                Statement statement = connection.createStatement();
        ) {
            deletedCount =
                    // DELETE FROM %s WHERE id = %d
                    statement.executeUpdate(
                            String.format(DELETE_STATEMENT,
                                    tableName,
                                    id));
        } catch (SQLException e) {
            throw new EntityDaoException(e);
        }
        if (deletedCount != 1) {
            throw new EntityDaoException("Deletion transaction failed "
                    + "(deletedCount != 1)");
        }
    }

    @Override
    public void insert(final Object entity) throws EntityDaoException {
        int insertedCount = 0;
        String[] params = getObjectParam(entity);

        try (
                Connection connection = connectionPool.getConnection();
                Statement statement = connection.createStatement();
        ) {
            insertedCount =
                    //INSERT INTO %s (%s) VALUES (%s)
                    statement.executeUpdate(
                            String.format(INSERT_STATEMENT,
                                    tableName,
                                    String.join(COMMA_SIGN, tableColumnNames),
                                    String.join(COMMA_SIGN, params)
                            ));

        } catch (SQLException e) {
            throw new EntityDaoException(e);
        }
        if (insertedCount != 1) {
            throw new EntityDaoException("Insertion transaction failed "
                    + "(insertedCount != 1)");
        }
    }

    private String[] getObjectParam(final Object entity)
            throws EntityDaoException {
        String[] param = new String[classFieldNames.length];
        try {
            for (int i = 0; i < classFieldNames.length; i++) {
                Field field = entity.getClass()
                        .getDeclaredField(classFieldNames[i]);
                field.setAccessible(true);
                Object fieldValue = field.get(entity);
                if (fieldValue instanceof CharSequence) {
                    fieldValue = SINGLE_QUOTE_SIGN
                            .concat(((CharSequence) fieldValue)
                                    .toString())
                            .concat(SINGLE_QUOTE_SIGN);
                }
                param[i] = fieldValue.toString();
                field.setAccessible(false);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new EntityDaoException(e);
        }
        return param;
    }
}
