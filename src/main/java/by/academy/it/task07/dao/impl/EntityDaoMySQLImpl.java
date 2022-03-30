package by.academy.it.task07.dao.impl;

import by.academy.it.task07.dao.EntityDao;
import by.academy.it.task07.dao.EntityDaoException;
import by.academy.it.task07.entity.MyColumn;
import by.academy.it.task07.entity.MyTable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class EntityDaoMySQLImpl implements EntityDao {
    private final String tableName;
    private final String[] tableColumnNames;
    private final String[] classFieldNames;
    private final Constructor constructor;
    private final Connection connection;
    private final PreparedStatement selectStatement;
    private final PreparedStatement updateStatement;
    private final PreparedStatement deleteStatement;
    private final PreparedStatement insertStatement;

    public EntityDaoMySQLImpl(Class aClass) throws EntityDaoException {
        /// To separate class
        ResourceBundle bundle = ResourceBundle.getBundle("database");
        String url = bundle.getString("url");
        String user = bundle.getString("user");
        String password = bundle.getString("password");


        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new EntityDaoException(e);
        }
        /////

        if (aClass.isAnnotationPresent(MyTable.class)) {
            tableName = ((MyTable) aClass.getAnnotation(MyTable.class)).name();
        } else {
            throw new EntityDaoException("No 'MyTable' annotation");
        }


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
        try {
            constructor = aClass.getConstructor(classFieldTypes);
        } catch (NoSuchMethodException e) {
            throw new EntityDaoException(e);
        }

        try {
            selectStatement = connection.prepareStatement(getSelectQuery());
            updateStatement = connection.prepareStatement(getUpdateQuery());
            deleteStatement = connection.prepareStatement(getDeleteQuery());
            insertStatement = connection.prepareStatement(getInsertQuery());
        } catch (SQLException e) {
            throw new EntityDaoException(e);
        }
    }

    private String getSelectQuery() {
        StringBuilder builder = new StringBuilder("select ");
        for (String s : tableColumnNames) {
            builder.append(s).append(',');
        }
        // Need optimization
        builder.deleteCharAt(builder.length() - 1);
        builder.append(" from ").append(tableName);
        return builder.toString();
    }

    private String getUpdateQuery() {
        // Need realization
        StringBuilder builder = new StringBuilder("update ");
/*        for (String s : tableColumnNames) {
            builder.append(s).append(',');
        }
        builder.append(" from ").append(tableName);*/
        return builder.toString();
    }

    private String getDeleteQuery() {
        StringBuilder builder = new StringBuilder("delete from ");
        builder.append(tableName)
                .append(" where id = ?");
        return builder.toString();
    }

    private String getInsertQuery() {
        // Need realization
        StringBuilder builder = new StringBuilder("insert into ");
/*        for (String s : tableColumnNames) {
            builder.append(s).append(',');
        }
        builder.append(" from ").append(tableName);*/
        return builder.toString();
    }

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
    public List select() throws EntityDaoException {
        List list = new ArrayList();
        ResultSet resultSet = null;
        try {
            resultSet = selectStatement.executeQuery();
            Object[] paramsForCreation = new Object[classFieldNames.length];
            while (resultSet.next()) {
                for (int i = 0; i < tableColumnNames.length; i++) {
                    paramsForCreation[i] = resultSet.getObject(i + 1);
                }
                list.add(createEntity(paramsForCreation));
            }
        } catch (SQLException e) {
            throw new EntityDaoException(e);
        } finally {
            try {
                resultSet.close();
            } catch (SQLException | NullPointerException e) {
                throw new EntityDaoException(e);
            }
        }
        return list;
    }

    @Override
    public int update(int id, Object[] params) throws EntityDaoException {
        // Need realization
        return 0;
    }

    @Override
    public int delete(int id) throws EntityDaoException {
        int deletedCount = 0;
        try {
            deleteStatement.setInt(1, id);
            deletedCount = deleteStatement.executeUpdate();
        } catch (SQLException e) {
            throw new EntityDaoException(e);
        }
        return deletedCount;
    }

    @Override
    public int insert(Object entity) throws EntityDaoException {
        // Need realization
        return 0;
    }

    @Override
    public void closeDao() throws EntityDaoException {
        try {
            insertStatement.close();
            deleteStatement.close();
            updateStatement.close();
            selectStatement.close();
            connection.close();
        } catch (SQLException e) {
            throw new EntityDaoException(e);
        }
    }
}
