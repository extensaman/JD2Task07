package by.academy.it.task07;

import by.academy.it.task07.dao.EntityDao;
import by.academy.it.task07.dao.EntityDaoException;
import by.academy.it.task07.dao.impl.ConnectionPool;
import by.academy.it.task07.dao.impl.EntityDaoImpl;
import by.academy.it.task07.entity.Person;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import static org.hamcrest.CoreMatchers.is;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AppTest extends Assert {

    public static final String DATABASE = "database";
    private static ConnectionPool connectionPool;
    private static EntityDao dao;//тестовый объект класса EntityDaoMySQLImpl
    private static Map<Long, Object> map = new HashMap<>();//тестовая коллекция map
    private long finishKeyMap;//переменная хранящая номер последнего ID таблицы person
    private static Person person1;
    private static Person person2;
    private static Person person3;
    private static Person person4;
    private static Person person5;
    private static Object fakeEntity = new Object();


    public AppTest() throws EntityDaoException, SQLException {

    }

    @BeforeClass //метод будет выполняться в начале всех тестов
    public static void setUp() throws Exception {
        ResourceBundle bundleH2 = ResourceBundle.getBundle(DATABASE);
        connectionPool = new ConnectionPool(bundleH2);
        assertTrue(connectionPool.getConnection() != null);
        String query = "create table person (id int auto_increment primary key," +
                "identifier int, surname varchar(50) not null,name varchar(50))";
        Statement statement = connectionPool.getConnection().createStatement();
        int i = statement.executeUpdate(query);
        dao = new EntityDaoImpl(Person.class, connectionPool);
        assertTrue(dao != null);
        //тестовые данные
        person1 = new Person(1011, "First", "James");
        person2 = new Person(2022, "Second", "Michael");
        person3 = new Person(3003, "Third", "Mike");
        person4 = null;
        person5 = new Person(1020, "Five", "Egor");
    }

    @Test
    public void A_insertDao() throws EntityDaoException {//проверка правильности добавления элементов (INSERT) с помощью сторонней Map
        dao.insert(person1);
        dao.insert(person2);
        dao.insert(person3);
        for (Long key : dao.select().keySet()//нахождение последнего использованного ключа
        ) {
            if (key.intValue() > finishKeyMap) {
                finishKeyMap = key.intValue();
            }
        }
        map.put(finishKeyMap - 2, person1);
        map.put(finishKeyMap - 1, person2);
        map.put(finishKeyMap, person3);
        assertEquals("Mistake! Element with ID: " + finishKeyMap + " not added!", map.get(finishKeyMap), dao.select().get(finishKeyMap));
        assertEquals("Mistake! Element with ID: " + (finishKeyMap - 1) + " not added!", map.get(finishKeyMap - 1), dao.select().get(finishKeyMap - 1));
        assertEquals("Mistake! Element with ID: " + (finishKeyMap - 2) + " not added!", map.get(finishKeyMap - 2), dao.select().get(finishKeyMap - 2));
    }

    @Test
    public void B_updateDao() throws EntityDaoException {//проверка метода UPDATE
        long updateElement = 1L;
        map.put(updateElement, person3);
        map.replace(updateElement, person3);
        try {
            dao.update(updateElement, person3);
        } catch (EntityDaoException e) {
            e.printStackTrace();
            System.out.println("Mistake! Attempt to replace the data of a non-existent string!");
        }
        Assert.assertEquals("Mistake! Data update operation failed!", map.get(updateElement), dao.select().get(updateElement));//Проверка на правильность работы UPDATE с помощью сторонней Map
        Assert.assertTrue(dao.select().get(updateElement).hashCode() == person3.hashCode());//проверка путём сравнения hashcode
    }

    @Test
    public void deleteDao() throws EntityDaoException {//проверка метода DELETE
        long deleteElement = 2L;
        try {
            dao.delete(deleteElement);
        } catch (EntityDaoException e) {
            e.printStackTrace();
            System.out.println("Mistake! Attempt to delete a non-existent row!");
        }
    }

    @Test
    public void selectDao() throws EntityDaoException {//проверка метода SELECT
        long elementSelect = 3L;
        Assert.assertNotNull("Mistake! Row not found!", dao.select().get(elementSelect));//Проверка на правильность работы выборки SELECT
    }

    @Test(expected = EntityDaoException.class)
    public void testUpdateException() throws EntityDaoException {//Проверка вызова исключения при обновлении данных
        Long updateExcemtion = 218L;
        dao.update(updateExcemtion, person5);
        Assert.fail("Mistake! Exception: " + EntityDaoException.class.getSimpleName() + " not thrown!");
    }

    @Test(expected = EntityDaoException.class)
    public void testDeleteException() throws EntityDaoException {//Проверка вызова исключения при удалении данных
        Long deleteExcemtion = 228L;
        dao.delete(deleteExcemtion);
        Assert.fail("Mistake! Exception: " + EntityDaoException.class.getSimpleName() + " not thrown!");
    }

    @Test(expected = NullPointerException.class)
    public void testInsertException() throws EntityDaoException {//Проверка вызова исключения при добавлении null данных
        dao.insert(person4);
        Assert.fail("Mistake! Exception: " + NullPointerException.class.getSimpleName() + " not thrown!");
    }


    @Test(expected = EntityDaoException.class)
    public void testEnityDaoImpl() throws EntityDaoException {//Проверка вызова исключения EntityDaoException при вызове конустрктора EntityDaoMySQLImpl
        class MyFakeClass {
            //создание фейкового класса для проверки работы EnityDaoException в конструкторе
        }
        EntityDao dao1 = new EntityDaoImpl(MyFakeClass.class, connectionPool);
        Assert.fail("Mistake! Exception: " + EntityDaoException.class.getSimpleName() + " not thrown!");
    }

    @Test(expected = EntityDaoException.class)
    public void testGetObjectParam() throws EntityDaoException {//Проверка вызова исключения EntityDaoException при вызове метода getObjectParam
        EntityDaoImpl entityDao = new EntityDaoImpl(Person.class, connectionPool);
        entityDao.update(1L, fakeEntity);//пытаемся обновить данные с помощью фейковой сущности
        Assert.fail("Mistake! Exception: " + EntityDaoException.class.getSimpleName() + " not thrown!");
    }

    @Test(expected = EntityDaoException.class)
    public void testCreateEntity() throws EntityDaoException {//Проверка вызова исключения EntityDaoException при вызове метода createEntity
        EntityDao dao2 = new EntityDaoImpl(Person.class, connectionPool);
        dao2.insert(fakeEntity);//пытаемся создать сущность, используя данные фейковой сущности
        Assert.fail("Mistake! Exception: " + EntityDaoException.class.getSimpleName() + " not thrown!");
    }

    @Test
    public void hashCodeEqualsPerson() {//проверка hashCode и Equals Person
        Person actualPerson = new Person(1030, "Yakovlev", "Dmitriy");
        Person expectedPerson = new Person(1030, "Yakovlev", "Dmitriy");
        Person expectedPerson2 = new Person(1040, "Ivanov", "Sergey");
        //Проверка на равенство
        assertThat("Mistake! Objects are not equal!", actualPerson, is(expectedPerson));
    }

    @Test
    public void testToStringPerson() {//проверка метода toString
        Person actualPerson = new Person(1030, "Yakovlev", "Dmitriy");
        String str = "Person{identifier=1030, surname='Yakovlev', name='Dmitriy'}";
        Assert.assertEquals(str, actualPerson.toString());
    }
}