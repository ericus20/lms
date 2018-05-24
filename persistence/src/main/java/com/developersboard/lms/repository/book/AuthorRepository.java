package com.developersboard.lms.repository.book;

import com.developersboard.lms.book.Author;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Eric on 4/2/2018.
 *
 * @author Eric Opoku
 */
@Repository
@Transactional(readOnly = true)
public interface AuthorRepository extends CrudRepository<Author, Long> {

    Author findByName(String name);

    List<Author> findAll();
}
