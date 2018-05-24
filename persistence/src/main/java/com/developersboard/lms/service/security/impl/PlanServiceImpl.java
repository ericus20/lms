package com.developersboard.lms.service.security.impl;

import com.developersboard.lms.repository.security.PlanRepository;
import com.developersboard.lms.security.Plan;
import com.developersboard.lms.service.security.PlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Created by Eric on 3/30/2018.
 *
 * @author Eric Opoku
 */
@Service
@Transactional(readOnly = true)
public class PlanServiceImpl implements PlanService {

    private final PlanRepository planRepository;

    @Autowired
    public PlanServiceImpl(PlanRepository planRepository) {
        this.planRepository = planRepository;
    }

    /**
     * It creates a Basic or Pro plan.
     *
     * @param plan The plan to create
     * @return the created plan
     * @throws IllegalArgumentException If the plan id is not 1 or 2
     */
    @Override
    @Transactional
    public Optional<Plan> createPlan(Plan plan) {
        return Optional.of(planRepository.save(plan));
    }

    /**
     * It gets the plan with the specified id.
     *
     * @param planId the id to get
     * @return the plan
     */
    @Override
    public Optional<Plan> getPlanById(int planId) {
        return planRepository.findById(planId);
    }

    /**
     * It gets the plan with the specified name.
     *
     * @param name the name to get
     * @return the plan
     */
    @Override
    public Optional<Plan> getPlanByName(String name) {
        return planRepository.findByName(name);
    }
}
