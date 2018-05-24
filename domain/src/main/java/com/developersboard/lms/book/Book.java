package com.developersboard.lms.book;

import com.developersboard.lms.base.BaseEntity;
import com.developersboard.lms.enums.book.CategoryEnum;
import com.developersboard.lms.enums.book.FormatEnum;
import com.developersboard.lms.enums.book.Status;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.*;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.PERSIST;

/**
 * Created by Eric on 4/2/2018.
 *
 * @author Eric Opoku
 */
@Data
@Entity
@NoArgsConstructor
@ToString(callSuper = true, exclude = "authors")
@EqualsAndHashCode(callSuper = false, of = {"isbn", "title"})
public class Book extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 6229479648807010701L;

    @Column(name = "isbn", nullable = false, unique = true)
    private Long isbn;

    @Column(name = "title", nullable = false, unique = true)
    private String title;

    @ManyToOne(fetch = FetchType.EAGER, cascade = MERGE)
    @JoinColumn(name = "publisher_id")
    private Publisher publisher;

    @Min(value = 0, message = "Pages cannot be negative number")
    private int pages;

    @Enumerated(EnumType.STRING)
    private FormatEnum format;

    @Min(value = 0)
    private int rating;

    @Column(name = "book_category")
    @Enumerated(EnumType.STRING)
    private CategoryEnum category;

    private String language;

    @Min(value = 1, message = "Edition cannot be less than 1")
    private int edition;

    @ManyToMany(cascade = {PERSIST, ALL})
    @JoinTable(name = "Book_Author", joinColumns = @JoinColumn(name = "BOOK_ID"),
            inverseJoinColumns = @JoinColumn(name = "AUTHOR_ID"))
//    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @Cascade(CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    @Fetch(FetchMode.SELECT)
    @JsonIgnore
    private Set<Author> authors = new HashSet<>();

    private String bookImageUrl;

    @Enumerated(EnumType.STRING)
    private Status status;

    // it's more portable and should effectively generate TEXT or LONGTEXT data type.
    @Lob
    private String description;

    public Book(Long isbn, String title, Publisher publisher) {
        this.isbn = isbn;
        this.title = title;
        this.publisher = publisher;
    }

    public Book(Long isbn, String title, Publisher publisher,
                @Min(value = 1, message = "Edition cannot be less than 1") int edition) {
        this.isbn = isbn;
        this.title = title;
        this.publisher = publisher;
        this.edition = edition;
    }
}
