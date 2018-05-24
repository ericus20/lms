package com.developersboard.lms.enums.book;

/**
 * Created by Eric on 3/29/2018.
 *
 * @author Eric Opoku
 */
public enum CatalogEnum {

    BOOKS(1, "BOOKS"),
    EBOOKS(2, "EBOOKS"),
    DVDS(3, "DVDS"),
    MAGAZINES(4, "MAGAZINES"),
    AUDIO(5, "AUDIO"),
    KIDS(6, "KIDS"),
    EAUDIO(7, "EAUDIO");

    private final int id;
    private final String name;


    CatalogEnum(final int id, final String name) {
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
