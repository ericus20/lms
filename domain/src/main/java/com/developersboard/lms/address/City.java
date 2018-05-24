package com.developersboard.lms.address;

import com.developersboard.lms.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
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
@ToString(callSuper = true, exclude = "zipCodes")
@EqualsAndHashCode(callSuper = false, of = {"name", "state"})
public class City extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -3656661263300202018L;

    private String name;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "state_id")
    private State state;

    @OneToMany(mappedBy = "city", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    public Set<ZipCode> zipCodes = new HashSet<>();

    public City(String name, State state) {
        this.name = name;
        this.state = state;
    }
}
