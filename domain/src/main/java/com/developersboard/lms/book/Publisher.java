package com.developersboard.lms.book;

import com.developersboard.lms.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
@NoArgsConstructor
@ToString(callSuper = true, exclude = {"books"})
@EqualsAndHashCode(callSuper = false, of = {"name", "country"})
public class Publisher extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 5468027747415246892L;

    @Column(nullable = false, unique = true)
    private String name;

    private String country;

    @OneToMany(mappedBy = "publisher", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<Book> books = new HashSet<>();

    public Publisher(String name, String country) {
        this.name = name;
        this.country = country;
    }
}

