package com.developersboard.lms.address;

import com.developersboard.lms.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

/**
 * Created by Eric on 4/19/2018.
 *
 * @author Eric Opoku
 */
@Data
@Entity
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false, of = {"zipCode"})
public class ZipCode extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -1916296581014829018L;

    private String zipCode;

    public ZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id")
    public City city;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "state_id")
    public State state;
}
