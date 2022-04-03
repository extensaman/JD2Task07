package by.academy.it.task07.dao.impl;

import by.academy.it.task07.dao.*;
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

public class EntityDaoMySQLImpl implements EntityDao {
    public static final String SELECT_STATEMENT = "SELECT * FROM %s";
    public static final String DELETE_STATEMENT = "DELETE FROM %s WHERE id = %d";
    public static final String INSERT_STATEMENT = "INSERT INTO %s (%s) VALUES (%s)";
    public static final String UPDATE_STATEMENT = "UPDATE %s SET %s WHERE id = %d";
    public static final String SINGLE_QUOTE_SIGN = "'";
    public static final String COMMA_SIGN = ",";
    public static final String EQUAL_SIGN = "=";
    private final String tableName;
    private final String[] tableColumnNames;
    private final String[] classFieldNames;
    private final Constructor constructor;
    public static ConnectionProvider connectionProvider;

    public EntityDaoMySQLImpl(Class aClass, DBVariety nameOfDatabase) throws EntityDaoException {

        switch (nameOfDatabase) {
            case H2:
                connectionProvider = new ConnectionPoolProviderH2();
            case MYSQL:
                connectionProvider = new ConnectionPoolProviderMySQL();
        }

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
                tableColumnNames[index] = ((MyColumn) field.getAnnotation(MyColumn.class)).name();
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
     * Create new instance of persistent class
     *
     * @param params - values of class fields
     * @return new instance of persistent class
     * @throws EntityDaoException
     */
    private Object createEntity(Object[] params) throws EntityDaoException {
        Object entity = null;
        try {
            entity = constructor.newInstance(params);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new EntityDaoException(e);
        }
        return entity;
    }

    @Override
    public Map<Long, Object> select() throws EntityDaoException {
        Map<Long, Object> map = new HashMap<>();
        try (
                Connection connection = connectionProvider.getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet =
                        statement.executeQuery( // SELECT * FROM %s
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
    public void update(Long id, Object entity) throws EntityDaoException {
        int updatedCount = 0;
        String[] params = getObjectParam(entity);

        try (
                Connection connection = connectionProvider.getConnection();
                Statement statement = connection.createStatement();
        ) {
            updatedCount =
                    statement.executeUpdate( // UPDATE %s SET %s WHERE id = %d
                            String.format(UPDATE_STATEMENT,
                                    tableName,
                                    IntStream.range(0, tableColumnNames.length)
                                            .mapToObj(i -> tableColumnNames[i].concat(EQUAL_SIGN).concat(params[i]))
                                            .collect(Collectors.joining(COMMA_SIGN)),
                                    id
                            ));
        } catch (SQLException e) {
            throw new EntityDaoException(e);
        }

        if (updatedCount != 1) {
            throw new EntityDaoException("Updating transaction failed (updatedCount != 1)");
        }
    }

    @Override
    public void delete(Long id) throws EntityDaoException {
        int deletedCount = 0;
        try (
                Connection connection = connectionProvider.getConnection();
                Statement statement = connection.createStatement();
        ) {
            deletedCount =
                    statement.executeUpdate( // DELETE FROM %s WHERE id = %d
                            String.format(DELETE_STATEMENT,
                                    tableName,
                                    id));
        } catch (SQLException e) {
            throw new EntityDaoException(e);
        }
        if (deletedCount != 1) {
            throw new EntityDaoException("Deletion transaction failed (deletedCount != 1)");
        }
    }

    @Override
    public void insert(Object entity) throws EntityDaoException {
        int insertedCount = 0;
        String[] params = getObjectParam(entity);

        try (
                Connection connection = connectionProvider.getConnection();
                Statement statement = connection.createStatement();
        ) {
            insertedCount =
                    statement.executeUpdate( //INSERT INTO %s (%s) VALUES (%s)
                            String.format(INSERT_STATEMENT,
                                    tableName,
                                    String.join(COMMA_SIGN, tableColumnNames),
                                    String.join(COMMA_SIGN, params)
                            ));

        } catch (SQLException e) {
            throw new EntityDaoException(e);
        }
        if (insertedCount != 1) {
            throw new EntityDaoException("Insertion transaction failed (insertedCount != 1)");
        }
    }

    private String[] getObjectParam(Object entity) throws EntityDaoException {
        String[] param = new String[classFieldNames.length];
        try {
            for (int i = 0; i < classFieldNames.length; i++) {
                Field field = entity.getClass().getDeclaredField(classFieldNames[i]);
                field.setAccessible(true);
                Object fieldValue = field.get(entity);
                if (fieldValue instanceof CharSequence) {
                    fieldValue = SINGLE_QUOTE_SIGN.concat(((CharSequence) fieldValue).toString()).concat(SINGLE_QUOTE_SIGN);
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
