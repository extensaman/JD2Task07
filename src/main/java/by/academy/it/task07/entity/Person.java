package by.academy.it.task07.entity;

import java.util.Objects;

/**
 * Entity class person.
 */
@MyTable(name = "person")
public class Person {
    /**
     * Identifier field.
     */
    @MyColumn(name = "identifier")
    private Integer identifier;
    /**
     * Surname field.
     */
    @MyColumn(name = "surname")
    private String surname;
    /**
     * Name field.
     */
    @MyColumn(name = "name")
    private String name;

    /**
     * Constructor for person class.
     *
     * @param incomingIdentifier Incoming identifier
     * @param incomingSurname    Incoming Surname
     * @param incomingName       Incoming name
     */
    public Person(final Integer incomingIdentifier,
                  final String incomingSurname,
                  final String incomingName) {
        this.identifier = incomingIdentifier;
        this.surname = incomingSurname;
        this.name = incomingName;
    }

    /**
     * Default constructor.
     */
    public Person() {
    }

    /**
     * Getter for field identifier.
     *
     * @return identifier
     */
    public Integer getId() {
        return identifier;
    }

    /**
     * Getter for field surname.
     *
     * @return surname
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Getter for field name.
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @param o incoming object to compare
     * @return true if equals, else false
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Person person = (Person) o;
        return Objects.equals(identifier, person.identifier)
                && Objects.equals(surname, person.surname)
                && Objects.equals(name, person.name);
    }

    /**
     * @return Hash of fields of this class.
     */
    @Override
    public int hashCode() {
        return Objects.hash(identifier, surname, name);
    }

    /**
     * @return String with fields names and their values.
     */
    @Override
    public String toString() {
        return "Person{"
                + "identifier=" + identifier
                + ", surname='" + surname + '\''
                + ", name='" + name + '\''
                + '}';
    }
}
