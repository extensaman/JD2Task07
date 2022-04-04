package by.academy.it.task07;

import by.academy.it.task07.dao.EntityDao;
import by.academy.it.task07.dao.EntityDaoException;
import by.academy.it.task07.dao.impl.ConnectionPool;
import by.academy.it.task07.dao.impl.EntityDaoImpl;
import by.academy.it.task07.entity.Person;

import java.util.Map;
import java.util.ResourceBundle;

public class App {
    public static final String DATABASE_MY_SQL = "databaseMySQL";

    public static void main(String[] args) throws EntityDaoException {
        ResourceBundle bundleMySql = ResourceBundle.getBundle(DATABASE_MY_SQL);

        ConnectionPool connectionPool = new ConnectionPool(bundleMySql);
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
    }
}
