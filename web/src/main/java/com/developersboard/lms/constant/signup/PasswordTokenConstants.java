package com.developersboard.lms.constant.signup;

/**
 * Created by Eric on 3/30/2018.
 *
 * @author Eric Opoku
 */
public abstract class PasswordTokenConstants {

    private PasswordTokenConstants() {}

    /** URL Mapping Constants */
    public static final String FORGOT_PASSWORD_URL_MAPPING = "/passwordReset";
    public static final String CHANGE_PASSWORD_PATH = "/changeUserPassword";

    /** View Name Constants */
    public static final String EMAIL_ADDRESS_VIEW_NAME = "library/password/emailForm";
    public static final String CHANGE_PASSWORD_VIEW_NAME = "library/password/passwordReset";

    /** Model Key Constants */
    public static final String EMAIL_SENT_KEY = "emailSent";
    public static final String EMAIL_MESSAGE_TEXT_PROPERTY_NAME = "forgot.my.password.email.text";
    public static final String PASSWORD_RESET_ATTRIBUTE_NAME = "passwordReset";
    public static final String MESSAGE_ATTRIBUTE_NAME = "message";
}
