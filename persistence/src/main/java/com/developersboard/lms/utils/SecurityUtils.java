package com.developersboard.lms.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.SecureRandom;
import java.util.Random;

/**
 * Created by Eric on 3/29/2018.
 *
 * @author Eric Opoku
 */
public abstract class SecurityUtils {

    /** Class cannot be instantiated */
    private SecurityUtils() {
        throw new AssertionError("Non instantiating class");
    }

    public static BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12, new SecureRandom("SALT".getBytes()));
    }


    /** To be used in generating random passwords for resets and signup */
    public static String randomPasswordGenerator() {

        String saltChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890abcdefghijklmnopqrstuvwxyz~!@#$%^&*(){}";
        StringBuilder passwordBuilder = new StringBuilder();
        Random random = new Random();

        while (passwordBuilder.length() < 18) {
            int randomIndex = (int) random.nextFloat() * saltChars.length();
            passwordBuilder.append(saltChars.charAt(randomIndex));
        }

        return passwordBuilder.toString();

    }

}
