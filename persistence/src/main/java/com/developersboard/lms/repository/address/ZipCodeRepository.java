package com.developersboard.lms.repository.address;

import com.developersboard.lms.address.ZipCode;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Created by Eric on 4/19/2018.
 *
 * @author Eric Opoku
 */
@Repository
@Transactional(readOnly = true)
public interface ZipCodeRepository extends CrudRepository<ZipCode, Long> {

    /** Retrieves zipCode with the given zipCode */
    Optional<ZipCode> findByZipCode(String zipCode);
}
