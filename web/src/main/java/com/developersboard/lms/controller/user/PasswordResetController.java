package com.developersboard.lms.controller.user;

import com.developersboard.lms.constant.index.IndexConstant;
import com.developersboard.lms.proxy.mail.EmailServiceProxy;
import com.developersboard.lms.security.PasswordToken;
import com.developersboard.lms.security.User;
import com.developersboard.lms.service.i18n.I18NService;
import com.developersboard.lms.service.security.PasswordTokenService;
import com.developersboard.lms.service.security.UserService;
import com.developersboard.lms.utils.WebModuleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.developersboard.lms.constant.signup.PasswordTokenConstants.*;

/**
 * Created by Eric on 3/30/2018.
 *
 * @author Eric Opoku
 */
@Controller
public class PasswordResetController {

    /**
     * The application logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(PasswordResetController.class);
    
    private final PasswordTokenService passwordTokenService;
    private final I18NService i18NService;
    private final UserService userService;
    private final EmailServiceProxy emailService;

    @Value("${webmaster.email}")
    private String webMasterEmail;

    @Autowired
    public PasswordResetController(
            PasswordTokenService passwordTokenService,
            I18NService i18NService, UserService userService, EmailServiceProxy emailService) {
        this.passwordTokenService = passwordTokenService;
        this.i18NService = i18NService;
        this.userService = userService;
        this.emailService = emailService;
    }

    @GetMapping(value = FORGOT_PASSWORD_URL_MAPPING)
    public String forgotPasswordGet() {
        return EMAIL_ADDRESS_VIEW_NAME;
    }

    @PostMapping(value = FORGOT_PASSWORD_URL_MAPPING)
    public String forgetPasswordPost(Model model, HttpServletRequest request,
                                     @RequestParam("email") String email) {

        Optional<PasswordToken> passwordResetToken
                = passwordTokenService.createNewPasswordResetTokenForEmail(email);


        if (!passwordResetToken.isPresent()) {
            LOG.warn("Couldn't find a reset passwordResetToken for email {}", email);
        } else {

            PasswordToken passwordToken = passwordResetToken.get();
            User user = passwordToken.getUser();
            String token = passwordToken.getToken();

            String resetPasswordUrl = WebModuleUtils.createPasswordResetUrl(request, user.getId(), token);
            LOG.debug("Reset Password URL {}", resetPasswordUrl);

            String emailText = i18NService.getMessage(EMAIL_MESSAGE_TEXT_PROPERTY_NAME);
            String messageBody = emailText + "\r\n" + resetPasswordUrl;
            String messageSubject = "[LMS]: How to Reset Your Password";

            SimpleMailMessage mailMessage = WebModuleUtils.getSimpleMailMessage(webMasterEmail, user,
                    messageBody, messageSubject);
            emailService.sendGenericMail(mailMessage);
            LOG.debug("Email sent successfully!");
        }

        model.addAttribute(EMAIL_SENT_KEY, true);
        return EMAIL_ADDRESS_VIEW_NAME;
    }


    @GetMapping(value = CHANGE_PASSWORD_PATH)
    public String changeUserPasswordGet(@RequestParam("id") Long id,
                                        @RequestParam("token") String token,
                                        Model model) {

        if (StringUtils.isEmpty(token) || id == 0) {
            LOG.error("Invalid user id {} or token value {}", id, token);
            model.addAttribute(PASSWORD_RESET_ATTRIBUTE_NAME, "false");
            model.addAttribute(MESSAGE_ATTRIBUTE_NAME, "Invalid user id or token value");
            return CHANGE_PASSWORD_VIEW_NAME;
        }

        Optional<PasswordToken> passwordTokenByToken
                = passwordTokenService.getPasswordTokenByToken(token);

        if (!passwordTokenByToken.isPresent()) {
            LOG.warn("A token couldn't be found with value {}", token);
            model.addAttribute(PASSWORD_RESET_ATTRIBUTE_NAME, "false");
            model.addAttribute(MESSAGE_ATTRIBUTE_NAME, "Token not found");
            return CHANGE_PASSWORD_VIEW_NAME;
        }

        User user = passwordTokenByToken.get().getUser();
        PasswordToken passwordToken = passwordTokenByToken.get();

        if (!user.getId().equals(id)) {
            LOG.error("The user id {} passed as parameter does not match the user id {} " +
                    "associated with the token {}", id, user.getId(), token);
            model.addAttribute(PASSWORD_RESET_ATTRIBUTE_NAME, "false");
            model.addAttribute(MESSAGE_ATTRIBUTE_NAME,
                    i18NService.getMessage("reset.password.token.invalid"));
            return CHANGE_PASSWORD_VIEW_NAME;
        }

        if (LocalDateTime.now(Clock.systemUTC()).isAfter(passwordToken.getExpiryDate())) {

            LOG.error("The token {} has expired", token);
            model.addAttribute(PASSWORD_RESET_ATTRIBUTE_NAME, "false");
            model.addAttribute(MESSAGE_ATTRIBUTE_NAME,
                    i18NService.getMessage("reset.password.token.expired"));
            return CHANGE_PASSWORD_VIEW_NAME;
        }

        model.addAttribute("principalId", user.getId());

        // OK to proceed. We auto-authenticate the user so that in the POST request we can check
        // if the user is authenticated
        Authentication authentication
                = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);


        return CHANGE_PASSWORD_VIEW_NAME;
    }

    @PostMapping(value = CHANGE_PASSWORD_PATH)
    public String changeUserPasswordGetPost(@RequestParam("password") String password,
                                            @RequestParam("principal_id") Long id,
                                            Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if ((authentication instanceof AnonymousAuthenticationToken)) {

            LOG.debug("Authenticated user is {} with username as {}", authentication.getPrincipal
                    (), authentication.getName());
            LOG.error("An unauthenticated user tried to invoke the reset password POST method");
            model.addAttribute(PASSWORD_RESET_ATTRIBUTE_NAME, "false");
            model.addAttribute(MESSAGE_ATTRIBUTE_NAME, "You are not authorized to perform this " +
                    "request.");
            return CHANGE_PASSWORD_VIEW_NAME;
        }

        if (authentication == null) {
            LOG.error("An unauthenticated user tried to invoke the reset password POST method");
            model.addAttribute(PASSWORD_RESET_ATTRIBUTE_NAME, "false");
            model.addAttribute(MESSAGE_ATTRIBUTE_NAME, "You are not authorized to perform this " +
                    "request.");
            return CHANGE_PASSWORD_VIEW_NAME;
        }

        User user = (User) authentication.getPrincipal();

        if (user == null || !user.getId().equals(id)) {
            LOG.error("Security breach! User {} is trying to make a password reset request on " +
                    "behalf of {}", user != null ? user.getId() : "null", id);
            model.addAttribute(PASSWORD_RESET_ATTRIBUTE_NAME, "false");
            model.addAttribute(MESSAGE_ATTRIBUTE_NAME, "You are not authorized to perform this " +
                    "request.");
            return CHANGE_PASSWORD_VIEW_NAME;
        }

        userService.updateUserPassword(id, password);
        LOG.info("Password successfully updated for user {}", user.getUsername());

        model.addAttribute(PASSWORD_RESET_ATTRIBUTE_NAME, "true");

        String emailText = i18NService.getMessage("reset.password.email.sent.success");
        String messageSubject = "[LMS]: Password Reset";

        SimpleMailMessage mailMessage = WebModuleUtils.getSimpleMailMessage(webMasterEmail, user, emailText, messageSubject);
        emailService.sendGenericMail(mailMessage);
        LOG.debug("Email sent successfully!");

        return IndexConstant.LOGIN_VIEW_NAME;
    }
}
