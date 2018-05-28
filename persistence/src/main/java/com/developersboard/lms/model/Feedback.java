package com.developersboard.lms.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Created by Eric on 3/29/2018.
 *
 * @author Eric Opoku
 */
@Data
@ToString
@EqualsAndHashCode(of = {"phone", "email", "lastName"})
public final class Feedback {

    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String message;
}
