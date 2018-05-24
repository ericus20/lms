package com.developersboard.lms.repository.security;

import com.developersboard.lms.security.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Eric on 3/29/2018.
 *
 * @author Eric Opoku
 */
@Repository
@Transactional(readOnly = true)
public interface UserRepository extends CrudRepository<User, Long> {

    /** Returns user that matches the specified username */
    User findByUsername(String username);

    /** Returns user that matches the specified email */
    User findByEmail(String email);

    /** Returns all users in the database */
    List<User> findAll();

    @Modifying
    @Transactional
    @Query("update User u set u.password = :password where u.id = :id")
    void updateUserPassword(@Param("id") Long id, @Param("password") String password);

    @Modifying
    @Transactional
    @Query("delete from User u where u.id = :id")
    void deleteUserById(@Param("id") Long id);
}
