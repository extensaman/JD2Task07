package by.academy.it.task07;

import static org.junit.Assert.assertTrue;

import by.academy.it.task07.dao.EntityDao;
import by.academy.it.task07.dao.EntityDaoException;
import by.academy.it.task07.dao.impl.ConnectionPool;
import by.academy.it.task07.dao.impl.EntityDaoImpl;
import by.academy.it.task07.entity.Person;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.ResourceBundle;

public class AppTest
{

    public static final String DATABASE_H_2 = "databaseH2";

    @Test
    public void shouldAnswerWithTrue() throws SQLException, EntityDaoException {
        ResourceBundle bundleH2 = ResourceBundle.getBundle(DATABASE_H_2);

        ConnectionPool connectionPool = new ConnectionPool(bundleH2);
        String query = "create table person (id int auto_increment primary key," +
                "identifier int, surname varchar(50) not null,name varchar(50))";
        Statement statement = connectionPool.getConnection().createStatement();
        statement.executeUpdate(query);
        EntityDao dao = new EntityDaoImpl(Person.class, connectionPool);
        Person person1 = new Person(1011, "First", "James");
        Person person2 = new Person(2022, "Second", "Michael");
        Person person3 = new Person(3003, "Third", "Mike");
        dao.insert(person1);
        dao.insert(person2);
        dao.insert(person3);
        Map<Long, Object> list = dao.select();
        System.out.println("Inserted\n" + list);
        Person person4 = new Person(4000, "Forth", "Bill");
        dao.update(2L, person4);
        list = dao.select();
        System.out.println("Updated\n" + list);

        /*String url = bundle.getString("url");
        String user = bundle.getString("user");
        String password = bundle.getString("password");
        Connection connection = null;

        connection = DriverManager.getConnection(url, user, password);*/

        assertTrue( connectionPool.getConnection() != null );
    }
}
