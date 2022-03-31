package by.academy.it.task07;

import by.academy.it.task07.dao.EntityDao;
import by.academy.it.task07.dao.EntityDaoException;
import by.academy.it.task07.dao.impl.EntityDaoMySQLImpl;
import by.academy.it.task07.entity.Person;

import java.util.Map;

public class App {
    public static void main(String[] args) throws EntityDaoException {
        EntityDao dao = new EntityDaoMySQLImpl(Person.class);
        Person person1 = new Person(1011, "First", "James");
        Person person2 = new Person(2022, "Second", "Michael");
        Person person3 = new Person(3003, "Third", "Mike");
        dao.insert(person1);
        dao.insert(person2);
        dao.insert(person3);
        Map<Long, Object> list = dao.select();
        System.out.println("Inserted\n" + list);
        Person person4 = new Person(4000, "Forth", "Bill");
        dao.update(8L, person4);

    }
}
