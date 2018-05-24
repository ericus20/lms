package com.developersboard.lms.address;

import com.developersboard.lms.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
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
@EqualsAndHashCode(callSuper = false, of = {"address", "address2", "zipCode", "city"})
public class Address extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 2352378663755161431L;

    private String address;
    private String address2;

    @OneToOne(cascade = CascadeType.MERGE)
    @LazyCollection(LazyCollectionOption.FALSE)
    @Fetch(FetchMode.SELECT)
    @JoinColumn(name = "city_id")
    private City city;

    @OneToOne(cascade = CascadeType.MERGE)
    @LazyCollection(LazyCollectionOption.FALSE)
    @Fetch(FetchMode.SELECT)
    @JoinColumn(name = "zipCode_id")
    private ZipCode zipCode;

    @OneToOne(cascade = CascadeType.MERGE)
    @LazyCollection(LazyCollectionOption.FALSE)
    @Fetch(FetchMode.SELECT)
    @JoinColumn(name = "state_id")
    private State state;
}
