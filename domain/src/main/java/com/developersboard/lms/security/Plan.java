package com.developersboard.lms.security;

import com.developersboard.lms.enums.PlansEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by Eric on 3/30/2018.
 *
 * @author Eric Opoku
 */
@Data
@Entity
@ToString
@NoArgsConstructor
@EqualsAndHashCode(of = {"name"})
public class Plan implements Serializable {

    /** The Serial Version UID for Serializable classes. */
    private static final long serialVersionUID = 6685900211968423780L;

    @Id
    private int id;

    private String name;

    public Plan(PlansEnum plansEnum) {
        this.id = plansEnum.getId();
        this.name = plansEnum.getPlan();
    }
}
