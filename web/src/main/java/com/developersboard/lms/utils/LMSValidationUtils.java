package com.developersboard.lms.utils;

/**
 * Created by Eric on 3/29/2018.
 *
 * @author Eric Opoku
 */
public abstract class LMSValidationUtils {

    /** Class cannot be instantiated */
    private LMSValidationUtils() {
        throw new AssertionError("Non instantiating class");
    }

    /**
     * A helper method which takes in multiple arguments and validate each instance not being null
     *
     * @param inputs instances to be validated
     * @throws IllegalArgumentException if any of the inputs is {@literal null}.
     */
    public static void validateInputs(Object ... inputs) {

        for (Object input : inputs) {
            if (input == null) {
                throw new IllegalArgumentException("Null elements are not allowed");
            }
        }
    }

}
