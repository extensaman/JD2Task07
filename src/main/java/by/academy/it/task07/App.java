package by.academy.it.task07;

import by.academy.it.task07.dao.EntityDao;
import by.academy.it.task07.dao.EntityDaoException;
import by.academy.it.task07.dao.impl.ConnectionPool;
import by.academy.it.task07.dao.impl.EntityDaoImpl;
import by.academy.it.task07.entity.Person;

import java.util.Map;
import java.util.ResourceBundle;

/**
 * There is a Person entity, it has
 * - an identifier;
 * - a first name
 * - a last name.
 *
 * <p>The task is to create a project. Adding a table to the database must be done through liquibase,
 * make tests using H2.</p>
 * <p>Cover functionality with tests and make a report using the jacoco plugin.</p>
 * <p>Connect checkstyle to the project.</p>
 * <p>For the Person entity, make a DAO over each field, write your own annotation
 * MyColumn(name - the name of the column) with the name of the column,
 * write the annotation MyTable(name - the name of the table) above the Person class.
 * Implement CRUD operations on Person using jdbc</p>
 *
 * - select
 * - update
 * - delete
 * - insert
 *
 * <p>Moreover, these operations should make a request to the database using the annotations
 * MyColumn and MyTable (through reflection). i.e. if the user of this API creates another entity, then</p>
 * - select
 * - update
 * - delete
 * - insert
 * <p>should work without changing the internal logic</p>
 *
 * @author LidiaZh
 * @author JustShooter Aliaksei Iyunski
 * @author Flashsan
 * @author Sergey060890
 * @author Yusikau Aliaksandr
 * @version 1.0
 */

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
