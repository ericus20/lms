package com.developersboard.lms.repository.address;


import com.developersboard.lms.address.Address;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Eric on 4/19/2018.
 *
 * @author Eric Opoku
 */
@Repository
@Transactional(readOnly = true)
public interface AddressRepository extends CrudRepository<Address, Long> {
    Address findByAddress(String street);

    List<Address> findAll();
}
