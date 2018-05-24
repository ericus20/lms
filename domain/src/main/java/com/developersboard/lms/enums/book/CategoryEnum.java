package com.developersboard.lms.enums.book;

/**
 * Created by Eric on 3/29/2018.
 *
 * @author Eric Opoku
 */
public enum CategoryEnum {

    JAVA(1, "JAVA"),
    HTML(2, "HTML"),
    NOVEL(3, "NOVEL"),
    GENERIC(4, "GENERIC");

    private final int id;
    private final String name;


    CategoryEnum(final int id, final String name) {
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
        return "Status{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

}
