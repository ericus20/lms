package com.developersboard.lms.enums;

/**
 * Created by Eric on 3/29/2018.
 *
 * @author Eric Opoku
 * @author Matthew Puentes
 */
public enum RolesEnum {

    /** Admin Constant with it's associated Id = 1 and string name */
    ADMIN(1, "ROLE_ADMIN"),

    /** User Constant with it's associated Id = 2 and string name */
    USER(2, "ROLE_USER"),

    /** Librarian Constant with it's associated Id = 3 and string name */
    LIBRARIAN(3, "ROLE_LIBRARIAN");


    // Id for the particular role.
    private final int id;

    // Particular role's name to be assigned in constructor.
    private final String name;

    RolesEnum(final int id, final String name) {
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
        final StringBuilder sb = new StringBuilder("RolesEnum{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }

}
