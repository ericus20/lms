package com.developersboard.lms.service.security.impl;

import com.developersboard.lms.repository.security.PasswordTokenRepository;
import com.developersboard.lms.repository.security.UserRepository;
import com.developersboard.lms.security.PasswordToken;
import com.developersboard.lms.security.User;
import com.developersboard.lms.service.security.PasswordTokenService;
import com.developersboard.lms.utils.LMSValidationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Created by Eric on 3/29/2018.
 *
 * @author Eric Opoku
 */
@Service
@Transactional(readOnly = true)
public class PasswordTokenServiceImpl implements PasswordTokenService {

    /**
     * The application logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(PasswordTokenServiceImpl.class);

    private final PasswordTokenRepository passwordTokenRepository;
    private final UserRepository userRepository;

    @Autowired
    public PasswordTokenServiceImpl(
            PasswordTokenRepository passwordTokenRepository,
            UserRepository userRepository) {
        this.passwordTokenRepository = passwordTokenRepository;
        this.userRepository = userRepository;
    }

    /**
     * Save token with user specified
     *
     * @param token the token
     * @param user  the user
     * @return created token assigned to the user
     */
    @Override
    @Transactional
    public Optional<PasswordToken> savePasswordTokenWithUser(String token, User user) {
        LMSValidationUtils.validateInputs(token, user);
        return Optional.of(passwordTokenRepository.save(new PasswordToken(token, user)));
    }

    /**
     * Gets token with token specified
     *
     * @param token the token
     * @return created token assigned to the user
     */
    @Override
    public Optional<PasswordToken> getPasswordTokenByToken(String token) {
        LMSValidationUtils.validateInputs(token);
        return Optional.ofNullable(passwordTokenRepository.findByToken(token));
    }

    /**
     * Gets token with id specified
     *
     * @param id the id
     * @return created token assigned to the user
     */
    @Override
    public Optional<PasswordToken> getPasswordTokenById(Long id) {
        LMSValidationUtils.validateInputs(id);
        return passwordTokenRepository.findById(id);
    }

    /**
     * Delete token
     *
     * @param passwordToken the token to delete
     */
    @Override
    @Transactional
    public void deleteRestToken(PasswordToken passwordToken) {
        LMSValidationUtils.validateInputs(passwordToken);
        passwordTokenRepository.delete(passwordToken);
    }

    /**
     * Creates a new Password Reset Token for the user identified by the given email.
     *
     * @param email The email uniquely identifying the user
     * @return PasswordResetToken a new Password Reset Token for the user identified by the given
     * email or null if
     * none is found.
     */
    @Override
    @Transactional
    public Optional<PasswordToken> createNewPasswordResetTokenForEmail(String email) {

        User existingUser = userRepository.findByEmail(email);

        if (existingUser != null) {

            String token = UUID.randomUUID().toString();
            PasswordToken passwordResetToken =
                    new PasswordToken(token, existingUser);

            passwordResetToken = passwordTokenRepository.save(passwordResetToken);

            userRepository.save(existingUser);
            LOG.debug("Successfully created token {} for user {}", token, existingUser);

            return Optional.of(passwordResetToken);

        } else {
            LOG.warn("User with email {} is not found in our records", email);
        }

        return Optional.empty();

    }
}
