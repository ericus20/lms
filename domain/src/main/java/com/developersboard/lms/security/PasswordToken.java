package com.developersboard.lms.security;

import com.developersboard.lms.base.BaseEntity;
import com.developersboard.lms.converter.LocalDateTimeConverter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Clock;
import java.time.LocalDateTime;

/**
 * Created by Eric on 3/29/2018.
 *
 * @author Eric Opoku
 */
@Data
@Entity
@ToString
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false, exclude = {"expiryDate"})
public class PasswordToken extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -8536449786681185005L;

    private static final int DEFAULT_EXPIRY_TIME_IN_MINUTES = 30; // 30 minutes as default

    private String token;

    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime expiryDate;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    public PasswordToken(String token, User user) {
        this(token, user, DEFAULT_EXPIRY_TIME_IN_MINUTES);
    }

    private PasswordToken(String token, User user, int expiryDateInMinutes) {

        if (StringUtils.isEmpty(token) || user == null) {
            throw new IllegalArgumentException("Null values not allowed");
        }

        expiryDateInMinutes = (expiryDateInMinutes > 0) ?
                expiryDateInMinutes : DEFAULT_EXPIRY_TIME_IN_MINUTES;

        this.token = token;
        this.user = user;
        this.expiryDate = LocalDateTime.now(Clock.systemUTC()).plusMinutes(expiryDateInMinutes);
    }
}
