package com.developersboard.lms.security;

import com.developersboard.lms.enums.RolesEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Eric on 3/29/2018.
 *
 * @author Eric Opoku
 */
@Data
@Entity
@NoArgsConstructor
@ToString(exclude = {"users"})
@EqualsAndHashCode( of = {"id", "name"} )
public class Role implements Serializable {

    /* Since the object will be travelling across several JVM this UID will
     * be to serialize and deserialize
     */
    private static final long serialVersionUID = -4726597492234568939L;


    /** Roles needed as at now is only 2 and no need to enable auto generation of Id. We will have 2 roles. */
    @Id
    private int id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "roles")
	@Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @LazyCollection(LazyCollectionOption.FALSE)
	@JsonIgnore
    private Set<User> users = new HashSet<>();

    public Role(RolesEnum rolesEnum) {
        this.id = rolesEnum.getId();
        this.name = rolesEnum.getName();
    }
}

