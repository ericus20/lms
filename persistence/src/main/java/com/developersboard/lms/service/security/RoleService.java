package com.developersboard.lms.service.security;


import com.developersboard.lms.security.Role;

import java.util.Optional;

/**
 * Created by Eric on 3/29/2018.
 *
 * @author Eric Opoku
 */
public interface RoleService {

    /**
     * Create the role with the role instance given.
     *
     * @param role the role to save
     * @return the persisted role with assigned id.
     */
    Optional<Role> saveRole(Role role);

    /**
     * Retrieves the role with the specified id
     *
     * @param id the id of the role to retrieve
     * @return the role tuple that matches the id given.
     */
    Optional<Role> getRoleById(Integer id);

    /**
     * Retrieves the role with the specified name
     *
     * @param name the name of the role to retrieve
     * @return the role tuple that matches the id given.
     */
    Optional<Role> getRoleByName(String name);

}
