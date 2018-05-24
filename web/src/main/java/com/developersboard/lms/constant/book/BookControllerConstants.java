package com.developersboard.lms.constant.book;

/**
 * Created by Eric on 4/15/2018.
 *
 * @author Eric Opoku
 */
public abstract class BookControllerConstants {

    private BookControllerConstants() { throw new AssertionError("Non Instantiable");}

    public static final String BOOKS_AVAILABLE_VIEW_NAME = "library/books/books-available";
    public static final String MY_BOOKS_VIEW_NAME = "library/books/my-books";
    public static final String BOOK_DETAILS_VIEW_NAME = "library/books/book-details";
}
