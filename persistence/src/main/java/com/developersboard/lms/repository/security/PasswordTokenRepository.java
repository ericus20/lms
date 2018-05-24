package com.developersboard.lms.repository.security;

import com.developersboard.lms.security.PasswordToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Eric on 3/29/2018.
 *
 * @author Eric Opoku
 */
@Repository
@Transactional(readOnly = true)
public interface PasswordTokenRepository extends CrudRepository<PasswordToken, Long> {

    /** Returns a password token given the token */
    PasswordToken findByToken(String token);

}
