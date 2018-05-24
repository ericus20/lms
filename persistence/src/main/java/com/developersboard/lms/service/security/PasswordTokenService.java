package com.developersboard.lms.service.security;

import com.developersboard.lms.security.PasswordToken;
import com.developersboard.lms.security.User;

import java.util.Optional;

/**
 * Created by Eric on 3/29/2018.
 *
 * @author Eric Opoku
 */
public interface PasswordTokenService {

    /**
     * Save token with user specified
     * @param token the token
     * @param user the user
     * @return created token assigned to the user
     */
    Optional<PasswordToken> savePasswordTokenWithUser(String token, User user);

    /**
     * Gets token with token specified
     * @param token the token
     * @return created token assigned to the user
     */
    Optional<PasswordToken> getPasswordTokenByToken(String token);

    /**
     * Gets token with id specified
     * @param id the id
     * @return created token assigned to the user
     */
    Optional<PasswordToken> getPasswordTokenById(Long id);

    /**
     * Delete token
     * @param passwordToken the token to delete
     */
    void deleteRestToken(PasswordToken passwordToken);

    /**
     * Creates a new Password Reset Token for the user identified by the given email.
     *
     * @return PasswordResetToken a new Password Reset Token for the user identified by the given
     * email or null if
     * none is found.
     * @param email The email uniquely identifying the user
     */
     default Optional<PasswordToken> createNewPasswordResetTokenForEmail(String email) {
         return Optional.empty();
     }

}
