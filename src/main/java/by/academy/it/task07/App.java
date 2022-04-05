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
    public static final long ID_FOR_UPDATE = 4L;
    public static final long ID_FOR_DELETE = 2L;

    public static void main(String[] args) throws EntityDaoException {
        ResourceBundle bundleMySql = ResourceBundle.getBundle(DATABASE_MY_SQL);
        ConnectionPool connectionPool = new ConnectionPool(bundleMySql);
        EntityDao dao = new EntityDaoImpl(Person.class, connectionPool);

        printMap("Original DB:", dao.select());
        Person person4 = new Person(4000, "Forth", "Bill");
        System.out.println("\nCreate Person(4000, \"Forth\", \"Bill\")");

        dao.update(ID_FOR_UPDATE, person4);
        printMap("\nUpdated position #" + ID_FOR_UPDATE + " by Person (4000...):", dao.select());

        dao.delete(ID_FOR_DELETE);
        printMap("\nDeleted position #" + ID_FOR_DELETE + " :", dao.select());

        dao.insert(new Person(7777, "Black", "Jack"));
        printMap("\nInserted Person(7777, \"Black\", \"Jack\"):", dao.select());
    }

    private static void printMap(String message, Map<Long, Object> map) {
        System.out.println(message);
        for (Map.Entry<Long, Object> longObjectEntry : map.entrySet()) {
            System.out.println("ID = " + longObjectEntry.getKey());
            System.out.println(longObjectEntry.getValue());
        }
    }
}
