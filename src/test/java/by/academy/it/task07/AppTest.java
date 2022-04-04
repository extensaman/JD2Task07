package by.academy.it.task07;

import by.academy.it.task07.dao.EntityDao;
import by.academy.it.task07.dao.EntityDaoException;
import by.academy.it.task07.dao.impl.DBVariety;
import by.academy.it.task07.dao.impl.EntityDaoImpl;
import by.academy.it.task07.entity.Person;
import org.junit.*;
import org.junit.runners.MethodSorters;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AppTest extends Assert {

    EntityDao dao1 = new EntityDaoImpl(Person.class, DBVariety.H2);//тестовый объект класса EntityDaoMySQLImpl
    Map<Long, Object> map = new HashMap<>();//тестовая коллекция map
    private static Person person;
    private static Person person2;
    private static Person person3;
    private static Person person4;
    private static Person person5;
    private static final Object fakeEntity = new Object();

    public AppTest() throws EntityDaoException, SQLException {
    }

    @BeforeClass //метод будет выполняться в начале всех тестов
    public static void setUp() {

        //тестовые данные
        person = new Person(1011, "First", "James");
        person2 = new Person(2022, "Second", "Michael");
        person3 = new Person(3003, "Third", "Mike");
        person4 = null;
        person5 = new Person(1020, "Five", "Egor");

    }

    @Test
    public void test1_insertDao() throws EntityDaoException {//проверка правильности добавления элементов (INSERT) с помощью сторонней Map
        dao1.insert(person);
        dao1.insert(person2);
        dao1.insert(person3);
        assertEquals("Mistake! Element with ID 1 not added!", map.get(1), dao1.select().get(1));
        assertEquals("Mistake! Element with ID 2 not added!", map.get(2), dao1.select().get(2));
        assertEquals("Mistake! Element with ID 3 not added!", map.get(3), dao1.select().get(3));
    }

    @Test
    public void test3_updateDao() throws EntityDaoException {//проверка метода UPDATE
        long updateElement = 2L;
        map.put(updateElement, person3);
        map.replace(updateElement, person3);
        try {
            dao1.update(updateElement, person3);
        } catch (EntityDaoException e) {
            e.printStackTrace();
            System.out.println("Mistake! Attempt to replace the data of a non-existent string!");
        }
        assertEquals("Mistake! Data update operation failed!", map.get(updateElement), dao1.select().get(updateElement));//Проверка на правильность работы UPDATE с помощью сторонней MapassertTrue(dao1.select().get(updateElement).hashCode() == person3.hashCode());//проверка путём сравнения hashcode
    }

    @Test
    public void test2_selectDao() throws EntityDaoException {//проверка метода SELECT
        long elementSelect = 1L;
        long elementSelect2 = 3L;
        assertNotNull("Mistake! Row not found!", dao1.select().get(elementSelect));//Проверка на правильность работы выборки SELECT
        assertNotNull("Mistake! Row not found!", dao1.select().get(elementSelect2));
    }

    @Test
    public void test4_deleteDao() throws EntityDaoException {//проверка метода DELETE
        long deleteElement = 2L;
        try {
            dao1.delete(deleteElement);
        } catch (EntityDaoException e) {
            e.printStackTrace();
            System.out.println("Mistake! Attempt to delete a non-existent row!");
        }
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
        EntityDao dao1 = new EntityDaoImpl(MyFakeClass.class, DBVariety.H2);
        Assert.fail("Mistake! Exception: " + EntityDaoException.class.getSimpleName() + " not thrown!");
    }

    @Test(expected = EntityDaoException.class)
    public void testGetObjectParam() throws EntityDaoException {//Проверка вызова исключения EntityDaoException при вызове метода getObjectParam
        EntityDaoImpl entityDaoMySQL = new EntityDaoImpl(Person.class, DBVariety.H2);
        entityDaoMySQL.update(261L, fakeEntity);//пытаемся обновить данные с помощью фейковой сущности
        entityDaoMySQL.update(261L, person5);
        Assert.fail("Mistake! Exception: " + EntityDaoException.class.getSimpleName() + " not thrown!");
    }

    @Test(expected = EntityDaoException.class)
    public void testCreateEntity() throws EntityDaoException {//Проверка вызова исключения EntityDaoException при вызове метода createEntity
        EntityDao dao2 = new EntityDaoImpl(Person.class, DBVariety.H2);
        dao2.insert(fakeEntity);//пытаемся создать сущность, используя данные фейковой сущности
//      dao2.insert(person5);
        Assert.fail("Mistake! Exception: " + EntityDaoException.class.getSimpleName() + " not thrown!");
    }

    @Test
    public void hashCodeEqualsPerson() {//проверка hashCode и Equals Person
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

    @After
    public void tearDown()
            throws Exception {
        ;
    }

}

