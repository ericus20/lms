package com.developersboard.lms.model.payload;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by Eric on 3/30/2018.
 *
 * @author Eric Opoku
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ProAccountPayload extends BasicAccountPayload {

    private String cardNumber;
    private String cardCode;
    private String cardMonth;
    private String cardYear;

}
