package by.academy.it.task07.entity;

import java.util.Objects;

@MyTable(name = "person")
public class Person {
    @MyColumn(name = "identifier")
    private Integer identifier;
    @MyColumn(name = "surname")
    private String surname;
    @MyColumn(name = "name")
    private String name;

    public Person(Integer identifier, String surname, String name) {
        this.identifier = identifier;
        this.surname = surname;
        this.name = name;
    }

    public Person() {
    }

    public Integer getId() {
        return identifier;
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
        return Objects.equals(identifier, person.identifier) &&
                Objects.equals(surname, person.surname) &&
                Objects.equals(name, person.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier, surname, name);
    }

    @Override
    public String toString() {
        return "Person{" +
                "identifier=" + identifier +
                ", surname='" + surname + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
