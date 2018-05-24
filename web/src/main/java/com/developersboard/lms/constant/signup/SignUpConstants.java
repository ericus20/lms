package com.developersboard.lms.constant.signup;

/**
 * Created by Eric on 3/29/2018.
 *
 * @author Eric Opoku
 */
public abstract class SignUpConstants {

    private SignUpConstants() {}

    /** URL Mapping Constants */
    public static final String SIGNUP_URL_MAPPING = "/signup";

    /** View Name Constants */
    public static final String SUBSCRIPTION_VIEW_NAME = "library/user/signup";

    /** Model Key Constants */
    public static final String PAYLOAD_MODEL_KEY_NAME = "payload";
    public static final String DUPLICATED_USERNAME_KEY = "duplicatedUsername";
    public static final String DUPLICATED_EMAIL_KEY = "duplicatedEmail";
    public static final String SIGNED_UP_MESSAGE_KEY = "signedUp";
    public static final String ERROR_MESSAGE_KEY = "message";

    /** Payload Constants for Stripe */
    public static final String STRIPE_CARD_NUMBER_KEY = "number";
    public static final String STRIPE_EXPIRY_MONTH_KEY = "exp_month";
    public static final String STRIPE_EXPIRY_YEAR_KEY = "exp_year";
    public static final String STRIPE_CVC_KEY = "cvc";
    public static final String STRIPE_CARD_KEY = "card";
}
