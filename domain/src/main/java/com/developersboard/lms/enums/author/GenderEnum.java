package com.developersboard.lms.enums.author;

/**
 * Created by Eric on 4/2/2018.
 *
 * @author Eric Opoku
 */
public enum  GenderEnum {

    /** Male Constant with it's associated Id = 1 and string name */
    MALE(1, "MALE"),

    /** Female Constant with it's associated Id = 2 and string name */
    FEMALE(2, "FEMALE");

    // Id for the particular role.
    private final int id;

    // Particular role's name to be assigned in constructor.
    private final String name;

    GenderEnum(final int id, final String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Gender{" + "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
