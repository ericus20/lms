package com.developersboard.lms.enums.book;

/**
 * Created by Eric on 3/29/2018.
 *
 * @author Eric Opoku
 */
public enum Status {

    RENTED(1, "Rented"),
    AVAILABLE(2, "Available"),
    OUT_OF_STOCK(2, "Out of stock");

    private final int id;
    private final String name;


    Status(final int id, final String name) {
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
