package com.developersboard.lms.service.security;


import com.developersboard.lms.security.Plan;

import java.util.Optional;

/**
 * Created by Eric on 3/30/2018.
 *
 * @author Eric Opoku
 */
public interface PlanService {

    /**
     * It creates a Basic or Pro plan.
     * @param plan The plan to create
     * @return the created plan
     * @throws IllegalArgumentException If the plan id is not 1 or 2
     */
     Optional<Plan> createPlan(Plan plan);

    /**
     * It gets the plan with the specified id.
     * @param planId the id to get
     * @return the plan
     */
     Optional<Plan> getPlanById(int planId);

    /**
     * It gets the plan with the specified name.
     * @param name the name to get
     * @return the plan
     */
     Optional<Plan> getPlanByName(String name);
}
