package by.academy.it.task07.entity;

import java.util.Objects;

@MyTable(name = "person")
public class Person {
    @MyColumn(name = "id")
    private final Integer id;
    @MyColumn(name = "surname")
    private final String surname;
    @MyColumn(name = "name")
    private final String name;

    public Person(Integer id, String surname, String name) {
        this.id = id;
        this.surname = surname;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getSurname() {
        return surname;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(id, person.id) &&
                Objects.equals(surname, person.surname) &&
                Objects.equals(name, person.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, surname, name);
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", surname='" + surname + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
