package com.developersboard.lms.repository.security;

import com.developersboard.lms.security.Role;
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
public interface RoleRepository extends CrudRepository<Role, Integer> {

    /** Returns role that matches the specified name */
    Role findByName(String name);
}
