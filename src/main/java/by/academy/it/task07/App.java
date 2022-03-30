package by.academy.it.task07;

import by.academy.it.task07.dao.EntityDao;
import by.academy.it.task07.dao.EntityDaoException;
import by.academy.it.task07.dao.impl.EntityDaoMySQLImpl;
import by.academy.it.task07.entity.Person;

import java.util.List;

public class App {
    public static void main(String[] args) throws EntityDaoException {
        EntityDao dao = new EntityDaoMySQLImpl(Person.class);
        List<Person> list = dao.select();
        System.out.println(list);
    }
}
