package com.developersboard.lms.repository.address;

import com.developersboard.lms.address.City;
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
public interface CityRepository extends CrudRepository<City, Long> {

    Optional<City> findByName(String name);

}
