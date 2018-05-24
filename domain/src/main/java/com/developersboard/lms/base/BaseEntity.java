package com.developersboard.lms.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Eric on 3/29/2018.
 *
 * @author Eric Opoku
 */

@Data                   // Lombok: Auto generates getters and setters for the instance variables.
@ToString               // Lombok: Generates pretty toString.
@EqualsAndHashCode( of = {"version"} )  // Lombok: Generates equals and hashcode for the class using only version.
@MappedSuperclass       // This is a super class to be mapped by Entity classes.
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @GenericGenerator(
            name = "LMS_StoreSequenceGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "LMS_StoreSequence"),
                    @Parameter(name = "initial_value", value = "1"),
                    @Parameter(name = "increment_size", value = "1")
            }
    )

    @Id
    @GeneratedValue(generator = "LMS_StoreSequenceGenerator")
    private Long id;

    @Version
    @Column(name = "version")
    private int version;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdAt;

    @CreatedBy
    private String createdBy;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedAt;

    @LastModifiedBy
    private String modifiedBy;

    public Date getCreatedAt() {
        return new Date(createdAt.getTime());
    }

    public Date getUpdatedAt() {
        return new Date(updatedAt.getTime());
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = new Date(createdAt.getTime());
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = new Date(updatedAt.getTime());
    }
}
