package com.developersboard.lms.model.payload;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by Eric on 3/30/2018.
 *
 * @author Eric Opoku
 */
@Data
@EqualsAndHashCode(of = {"email", "username"})
public class BasicAccountPayload implements Serializable {

    private static final long serialVersionUID = 5737794432544253113L;

    @NotNull
    @Email
    private String email;

    @NotNull
    private String username;

    @NotNull
    private String password;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    private String description;

    @NotNull
    private String phoneNumber;

    @NotNull
    private String country;
}
