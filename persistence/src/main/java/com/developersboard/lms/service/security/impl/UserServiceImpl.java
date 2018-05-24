package com.developersboard.lms.service.security.impl;

import com.developersboard.lms.repository.security.UserRepository;
import com.developersboard.lms.security.Role;
import com.developersboard.lms.security.User;
import com.developersboard.lms.service.security.RoleService;
import com.developersboard.lms.service.security.UserService;
import com.developersboard.lms.utils.LMSValidationUtils;
import com.developersboard.lms.utils.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Created by Eric on 3/29/2018.
 *
 * @author Eric Opoku
 */
@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    /**
     * The application logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final RoleService roleService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleService roleService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
    }

    /**
     * Create the user with the user instance given.
     *
     * @param user      the user with updated information.
     * @param userRoles the user roles assigned to the user.
     * @return the updated user.
     * @throws IllegalArgumentException in case the given entity is {@literal null}.
     */
    @Override
    @Transactional
    public Optional<User> createUser(User user, Set<Role> userRoles) {

        // Ensure permitted inputs are received
        validateInput(user, userRoles);

        // Attempt to check if user already exist
        User localUser = userRepository.findByUsername(user.getUsername());

        if (localUser != null) {

            LOG.info("User with username {} and email {} already exist. Nothing will be done.",
                    user.getUsername(), user.getEmail());

        } else {

            /* Encrypt user password */
            String encryptedPassword = SecurityUtils.passwordEncoder().encode(user.getPassword());
            user.setPassword(encryptedPassword);


            userRoles.forEach(userRole -> {
                Optional<Role> role = roleService.saveRole(userRole);
                role.ifPresent(role1 -> user.getRoles().add(role1));
            });

            localUser = userRepository.save(user);

        }

        return Optional.of(localUser);
    }

    /**
     * Returns a user for the given username or null if a user could not be found.
     *
     * @param username The username associated to the user to find.
     * @return a user for the given username or null if a user could not be found.
     * @throws IllegalArgumentException in case the given entity is {@literal null}.
     */
    @Override
    public Optional<User> getUserByUsername(String username) {
        validateInput(username);
        return Optional.ofNullable(userRepository.findByUsername(username));
    }

    /**
     * Returns a user for the given id or null if a user could not be found.
     *
     * @param id The id associated to the user to find.
     * @return a user for the given email or null if a user could not be found.
     * @throws IllegalArgumentException in case the given entity is {@literal null}.
     */
    @Override
    public Optional<User> getUserById(Long id) {
        validateInput(id);
        return userRepository.findById(id);
    }

    /**
     * Returns a user for the given email or null if a user could not be found.
     *
     * @param email The email associated to the user to find.
     * @return a user for the given email or null if a user could not be found.
     * @throws IllegalArgumentException in case the given entity is {@literal null}.
     */
    @Override
    public Optional<User> getUserByEmail(String email) {
        validateInput(email);
        return Optional.ofNullable(userRepository.findByEmail(email));
    }

    /**
     * Returns all user or null if no user exist.
     *
     * @return a user for the given email or null if a user could not be found.
     */
    @Override
    public Optional<List<User>> getAllUsers() {
        return Optional.of(userRepository.findAll());
    }

    /**
     * Update the user with the user instance given.
     *
     * @param user The user with updated information.
     * @return the updated user.
     * @throws IllegalArgumentException in case the given entity is {@literal null}.
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Optional<User> updateUser(User user) {
        validateInput(user);
        User save = userRepository.save(user);
        return Optional.of(save);
    }

    @Override
    @Transactional
    public void updateUserPassword(Long id, String password) {
        //userRepository.updateUserPassword(id, SecurityUtils.passwordEncoder().encode(password));
        Optional<User> userById = userRepository.findById(id);
        if (!userById.isPresent()) {
            LOG.error("No user with id {} exists!", id);
        } else {
            User user = userById.get();
            user.setPassword(SecurityUtils.passwordEncoder().encode(password));
            updateUser(user);
        }

    }

    /**
     * Delete the user with the user instance given.
     *
     * @param user The user to delete.
     * @throws IllegalArgumentException in case the given entity is {@literal null}.
     */
    @Override
    @Transactional
    public void deleteUser(User user) {
        validateInput(user);
        userRepository.delete(user);
    }

    /**
     * Delete the user with the user instance given.
     *
     * @param id The id of the user to delete.
     * @throws IllegalArgumentException in case the given entity is {@literal null}.
     */
    @Override
    @Transactional
    public void deleteUserById(Long id) {
        validateInput(id);
        userRepository.deleteById(id);
    }

    @Override
    public boolean checkUserExists(
            @NotBlank(message = "Username cannot be left blank") String username,
            @NotBlank(message = "Email cannot be left blank") String email) {
        validateInput(username, email);
        return checkUsernameExists(username) || checkEmailExists(username);
    }

    @Override
    public boolean checkEmailExists(
            @NotBlank(message = "Email cannot be left blank") String email) {
        return getUserByEmail(email).isPresent();
    }

    @Override
    public boolean checkUsernameExists(
            @NotBlank(message = "Username cannot be left blank") String username) {
        return getUserByUsername(username).isPresent();
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
