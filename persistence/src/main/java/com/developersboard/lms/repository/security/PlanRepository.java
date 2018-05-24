package com.developersboard.lms.repository.security;

import com.developersboard.lms.security.Plan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Created by Eric on 3/30/2018.
 *
 * @author Eric Opoku
 */
@Repository
@Transactional(readOnly = true)
public interface PlanRepository extends CrudRepository<Plan, Integer> {

    /** Returns the Plan given the name */
    Optional<Plan> findByName(String name);
}
