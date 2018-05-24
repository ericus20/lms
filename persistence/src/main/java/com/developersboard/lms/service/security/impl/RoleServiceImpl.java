package com.developersboard.lms.service.security.impl;

import com.developersboard.lms.repository.security.RoleRepository;
import com.developersboard.lms.security.Role;
import com.developersboard.lms.service.security.RoleService;
import com.developersboard.lms.utils.LMSValidationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Created by Eric on 3/29/2018.
 *
 * @author Eric Opoku
 */

@Service
@Transactional(readOnly = true)
public class RoleServiceImpl implements RoleService {

    /**
     * The application logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(RoleServiceImpl.class);

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    /**
     * Create the role with the role instance given.
     *
     * @param role the role to save
     * @return the persisted role with assigned id.
     */
    @Override
    @Transactional
    public Optional<Role> saveRole(Role role) {
        validateInput(role);
        return Optional.of(roleRepository.save(role));
    }

    /**
     * Retrieves the role with the specified id
     *
     * @param id the id of the role to retrieve
     * @return the role tuple that matches the id given.
     */
    @Override
    public Optional<Role> getRoleById(Integer id) {
        validateInput(id);
        return roleRepository.findById(id);
    }

    /**
     * Retrieves the role with the specified name
     *
     * @param name the name of the role to retrieve
     * @return the role tuple that matches the id given.
     */
    @Override
    public Optional<Role> getRoleByName(String name) {
        validateInput(name);
        return Optional.of(roleRepository.findByName(name));
    }

    //=========================================================> Private methods

    /**
     * A helper method which takes in multiple arguments and validate each instance not being null
     *
     * @param inputs instances to be validated
     * @throws IllegalArgumentException if any of the inputs is {@literal null}.
     */
    private void validateInput(Object ... inputs) {
        LMSValidationUtils.validateInputs(inputs);
    }
}
