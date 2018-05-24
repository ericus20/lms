package com.developersboard.lms.address;

import com.developersboard.lms.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Eric on 4/19/2018.
 *
 * @author Eric Opoku
 */
@Data
@Entity
@NoArgsConstructor
@ToString(callSuper = true, exclude = {"cities", "zipCodes"})
@EqualsAndHashCode(callSuper = false, of = {"code"})
public class State extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -1097685964908351240L;

    private String code;

    @OneToMany(mappedBy = "state", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<City> cities = new HashSet<>();

    @OneToMany(mappedBy = "state", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<ZipCode> zipCodes = new HashSet<>();

    public State(String code) {
        this.code = code;
    }
}
