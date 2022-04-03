package by.academy.it.task07;
import static org.junit.Assert.assertTrue;

import by.academy.it.task07.dao.EntityDao;
import by.academy.it.task07.dao.EntityDaoException;
import by.academy.it.task07.dao.impl.EntityDaoMySQLImpl;
import by.academy.it.task07.entity.Person;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

public class AppTest {
    EntityDao dao1 = new EntityDaoMySQLImpl(Person.class);//тестовый объект класса EntityDaoMySQLImpl
    Map<Long, Object> map = new HashMap<>();//тестовая коллекция map
    private long finishKeyMap;//переменная хранящая номер последнего ID таблицы person
    private static Person person;
    private static Person person2;
    private static Person person3;
    private static Person person4;
    private static Person person5;
    private static Object fakeEntity = new Object();

    public AppTest() throws EntityDaoException {
    }

    @Test
    public void shouldAnswerWithTrue() throws SQLException {
        ResourceBundle bundle = ResourceBundle.getBundle("PUBLIC");
        String url = bundle.getString("url");
        String user = bundle.getString("username");
        String password = bundle.getString("password");
        Connection connection = null;

        connection = DriverManager.getConnection(url, user, password);

        assertTrue(connection != null);
    }

    @BeforeClass //метод будет выполняться в начале всех тестов
    public static void setUp() throws Exception {
        //тестовые данные
        person = new Person(1011, "First", "James");
        person2 = new Person(2022, "Second", "Michael");
        person3 = new Person(3003, "Third", "Mike");
        person4 = null;
        person5 = new Person(1020, "Five", "Egor");
    }

    @Test
    public void insertDao() throws EntityDaoException {//проверка правильности добавления элементов (INSERT) с помощью сторонней Map
        dao1.insert(person);
        dao1.insert(person2);
        dao1.insert(person3);
        for (Long key : dao1.select().keySet()//нахождение последнего использованного ключа
        ) {
            if (key.intValue() > finishKeyMap) {
                finishKeyMap = key.intValue();
            }
        }
        map.put(finishKeyMap - 2, person);
        map.put(finishKeyMap - 1, person2);
        map.put(finishKeyMap, person3);
        Assert.assertEquals("Mistake! Element with ID: " + finishKeyMap + " not added!", map.get(finishKeyMap), dao1.select().get(finishKeyMap));
        Assert.assertEquals("Mistake! Element with ID: " + (finishKeyMap - 1) + " not added!", map.get(finishKeyMap - 1), dao1.select().get(finishKeyMap - 1));
        Assert.assertEquals("Mistake! Element with ID: " + (finishKeyMap - 2) + " not added!", map.get(finishKeyMap - 2), dao1.select().get(finishKeyMap - 2));
    }

    @Test
    public void updateDao() throws EntityDaoException {//проверка метода UPDATE
        long updateElement = 273L;
        map.put(updateElement, person3);
        map.replace(updateElement, person3);
        try {
            dao1.update(updateElement, person3);
        } catch (EntityDaoException e) {
            e.printStackTrace();
            System.out.println("Mistake! Attempt to replace the data of a non-existent string!");
        }
        Assert.assertEquals("Mistake! Data update operation failed!", map.get(updateElement), dao1.select().get(updateElement));//Проверка на правильность работы UPDATE с помощью сторонней Map
        Assert.assertTrue(dao1.select().get(updateElement).hashCode() == person3.hashCode());//проверка путём сравнения hashcode
    }

    @Test
    public void deleteDao() throws EntityDaoException {//проверка метода DELETE
        long deleteElement = 275L;
        try {
            dao1.delete(deleteElement);
        } catch (EntityDaoException e) {
            e.printStackTrace();
            System.out.println("Mistake! Attempt to delete a non-existent row!");
        }
    }

    @Test
    public void selectDao() throws EntityDaoException {//проверка метода SELECT
        long elementSelect = 280L;
        long elementSelect2 = 255L;
        Assert.assertNotNull("Mistake! Row not found!",dao1.select().get(elementSelect));//Проверка на правильность работы выборки SELECT
//      Assert.assertNotNull("Mistake! Row not found!",dao1.select().get(elementSelect2));
    }

    @Test(expected = EntityDaoException.class)
    public void testUpdateException() throws EntityDaoException {//Проверка вызова исключения при обновлении данных
        Long updateExcemtion = 218L;
        dao1.update(updateExcemtion, person5);
        Assert.fail("Mistake! Exception: " + EntityDaoException.class.getSimpleName() + " not thrown!");
    }

    @Test(expected = EntityDaoException.class)
    public void testDeleteException() throws EntityDaoException {//Проверка вызова исключения при удалении данных
        Long deleteExcemtion = 228L;
        dao1.delete(deleteExcemtion);
        Assert.fail("Mistake! Exception: " + EntityDaoException.class.getSimpleName() + " not thrown!");
    }

    @Test(expected = NullPointerException.class)
    public void testInsertException() throws EntityDaoException {//Проверка вызова исключения при добавлении null данных
        dao1.insert(person4);
//      dao1.insert(person3);
        Assert.fail("Mistake! Exception: " + NullPointerException.class.getSimpleName() + " not thrown!");
    }


    @Test(expected = EntityDaoException.class)
    public void testEnityDaoMySQLImpl() throws EntityDaoException {//Проверка вызова исключения EntityDaoException при вызове конустрктора EntityDaoMySQLImpl
        class MyFakeClass {
            //создание фейкового класса для проверки работы EnityDaoException в конструкторе
        }
        EntityDao dao1 = new EntityDaoMySQLImpl(MyFakeClass.class);
        Assert.fail("Mistake! Exception: " + EntityDaoException.class.getSimpleName() + " not thrown!");
    }

    @Test(expected = EntityDaoException.class)
    public void testGetObjectParam() throws EntityDaoException {//Проверка вызова исключения EntityDaoException при вызове метода getObjectParam
        EntityDaoMySQLImpl entityDaoMySQL = new EntityDaoMySQLImpl(Person.class);
        entityDaoMySQL.update(261L, fakeEntity);//пытаемся обновить данные с помощью фейковой сущности
//      entityDaoMySQL.update(261L, person5);
        Assert.fail("Mistake! Exception: " + EntityDaoException.class.getSimpleName() + " not thrown!");
    }

    @Test(expected = EntityDaoException.class)
    public void testCreateEntity() throws EntityDaoException {//Проверка вызова исключения EntityDaoException при вызове метода createEntity
        EntityDao dao2 = new EntityDaoMySQLImpl(Person.class);
        dao2.insert(fakeEntity);//пытаемся создать сущность, используя данные фейковой сущности
//      dao2.insert(person5);
        Assert.fail("Mistake! Exception: " + EntityDaoException.class.getSimpleName() + " not thrown!");
    }

    @Test
    public void hashCodeEqualsPerson(){//проверка hashCode и Equals Person
        Person actualPerson = new Person(1030, "Yakovlev", "Dmitriy");
        Person expectedPerson = new Person(1030, "Yakovlev", "Dmitriy");
        Person expectedPerson2 = new Person(1040, "Ivanov", "Sergey");
        //Проверка на равенство
        assertThat("Mistake! Objects are not equal!", actualPerson, is(expectedPerson));
//      assertThat("Mistake! Objects are not equal!", actualPerson, is(expectedPerson));
    }

    @Test
    public void testToStringPerson() {//проверка метода toString
        Person actualPerson = new Person(1030, "Yakovlev", "Dmitriy");
        String str = "Person{identifier=1030, surname='Yakovlev', name='Dmitriy'}";
        //String str2 = "Persons{identifier=1030, surname='Yakovlev', name='Dmitriy'}";
        Assert.assertEquals(str, actualPerson.toString());
        //Assert.assertEquals(str2, actualPerson.toString());
    }
}
