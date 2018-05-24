package com.developersboard.lms.security;

import com.developersboard.lms.address.Address;
import com.developersboard.lms.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Eric on 3/29/2018.
 *
 * @author Eric Opoku
 */
@Data
@Entity
@Table(name = "users")
@ToString(callSuper = true, exclude = {"roles", "password"} )
@EqualsAndHashCode(callSuper = false, of = {"username", "email"})
public class User extends BaseEntity implements Serializable, UserDetails {

    /* Since the object will be travelling across several JVM this UID will
     * be to serialize and deserialize
     */
    private static final long serialVersionUID = 456722779467783575L;

    @Column(unique = true)
    @Size(min = 2, message = "Username should be at least 2 characters")
    @NotBlank(message = "Username cannot be left blank")
    private String username;

    @JsonIgnore
    @NotBlank(message = "Password cannot be left blank")
    private String password;

    private String firstName;

    @Column(name = "last_name", unique = true)
    private String lastName;

    @Email(message = "A valid email format is required")
    @NotBlank(message = "Email cannot be left blank")
    @Column(unique = true)
    private String email;

    @Column(name = "phone", unique = true)
    private String phone;

    private String country;

    private String stripeCustomerId;

    private boolean enabled = true;
    private boolean accountNonExpired = true;
    private boolean accountNonLocked = true;
    private boolean credentialsNonExpired = true;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "plan_id")
    private Plan plan;


    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Length(max = 500)
    private String description;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="address_id")
    private Address address;


    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.ALL})
    @JoinTable(name = "USER_ROLE",
            joinColumns = @JoinColumn(name = "USER_ID"),
            inverseJoinColumns = @JoinColumn(name = "ROLE_ID"))
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
	@JsonIgnore
    private Set<Role> roles = new HashSet<>();

    /**
     * Returns the authorities granted to the user. Cannot return <code>null</code>.
     *
     * @return the authorities, sorted by natural key (never <code>null</code>)
     */
    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Set<GrantedAuthority> authorities = new HashSet<>();

        // Iterate through all userRoles and add each role to the authorities. This will be used by Spring Security.
        roles.forEach(userRole -> authorities.add(new Authority(userRole.getName())));
        return authorities;
    }
}
