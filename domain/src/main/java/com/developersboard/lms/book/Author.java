package com.developersboard.lms.book;

import com.developersboard.lms.base.BaseEntity;
import com.developersboard.lms.enums.author.GenderEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Eric on 4/2/2018.
 *
 * @author Eric Opoku
 */
@Data
@Entity
@Table(name = "authors")
@NoArgsConstructor
@ToString(callSuper = true, exclude = {"books"})
@EqualsAndHashCode(callSuper = false, of = {"name", "gender"})
public class Author extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -4694022092090102651L;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private GenderEnum gender;

    @ManyToMany(mappedBy = "authors")
    @LazyCollection(LazyCollectionOption.FALSE)
    @Fetch(FetchMode.SELECT)
    private Set<Book> books = new HashSet<>();

    public Author(String name, GenderEnum gender) {
        this.name = name;
        this.gender = gender;
    }
}
