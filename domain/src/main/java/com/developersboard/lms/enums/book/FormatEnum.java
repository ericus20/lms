package com.developersboard.lms.enums.book;

/**
 * Created by Eric on 4/2/2018.
 *
 * @author Eric Opoku
 */
public enum FormatEnum {

    WORD(1, "WORD"),
    PDF(2, "PDF"),
    DVD(3, "DVD");

    private final int id;
    private final String name;

    FormatEnum(final int id, final String name) {
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
        return "FormatEnum{" + "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }



}
