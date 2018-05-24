package com.developersboard.lms.service.security;


import com.developersboard.lms.security.Role;
import com.developersboard.lms.security.User;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Created by Eric on 3/29/2018.
 *
 * @author Eric Opoku
 */
public interface UserService {

    /**
     * Create the user with the user instance given.
     *
     * @param user      the user with updated information.
     * @param userRoles the user roles assigned to the user.
     * @return the updated user.
     * @throws IllegalArgumentException in case the given entity is {@literal null}.
     */
    Optional<User> createUser(User user, Set<Role> userRoles);

    /**
     * Returns a user for the given username or null if a user could not be found.
     *
     * @param username The username associated to the user to find.
     * @return a user for the given username or null if a user could not be found.
     * @throws IllegalArgumentException in case the given entity is {@literal null}.
     */
    Optional<User> getUserByUsername(String username);

    /**
     * Returns a user for the given id or null if a user could not be found.
     *
     * @param id The id associated to the user to find.
     * @return a user for the given email or null if a user could not be found.
     * @throws IllegalArgumentException in case the given entity is {@literal null}.
     */
    Optional<User> getUserById(Long id);

    /**
     * Returns a user for the given email or null if a user could not be found.
     *
     * @param email The email associated to the user to find.
     * @return a user for the given email or null if a user could not be found.
     * @throws IllegalArgumentException in case the given entity is {@literal null}.
     */
    Optional<User> getUserByEmail(String email);

    /**
     * Returns all user or null if no user exist.
     *
     * @return a user for the given email or null if a user could not be found.
     */
    Optional<List<User>> getAllUsers();

    /**
     * Update the user with the user instance given.
     *
     * @param user The user with updated information.
     * @return the updated user.
     * @throws IllegalArgumentException in case the given entity is {@literal null}.
     */
    Optional<User> updateUser(User user);

    /**
     * Update the user's password with the user instance given.
     *
     * @param id The id for the user to update
     * @param password The new password for the user
     * @throws IllegalArgumentException in case the given entity is {@literal null}.
     */
    void updateUserPassword(Long id, String password);

    /**
     * Delete the user with the user instance given.
     *
     * @param user The user to delete.
     * @throws IllegalArgumentException in case the given entity is {@literal null}.
     */
    void deleteUser(User user);

    /**
     * Delete the user with the user instance given.
     *
     * @param id The id of the user to delete.
     * @throws IllegalArgumentException in case the given entity is {@literal null}.
     */
     void deleteUserById(Long id);

    /**
     * Checks if user already exist. If user exists, nothing will be done.
     *
     * @param username the username of the new user
     * @param email the email fo the new user
     * @return if the user exists.
     * @throws Exception Exception will be thrown if there is an error.
     */
    boolean checkUserExists(
            @NotBlank(message = "Username cannot be left blank") String username,
            @NotBlank(message = "Email cannot be left blank") String email) throws Exception;

    boolean checkEmailExists(@NotBlank(message = "Email cannot be left blank") String email);

    boolean checkUsernameExists(@NotBlank(message = "Username cannot be left blank") String username);

}
