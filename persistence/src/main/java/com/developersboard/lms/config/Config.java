package com.developersboard.lms.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Eric on 5/24/2018.
 *
 * @author Eric Opoku
 */
@EntityScan(basePackages = {"com.developersboard.lms"})
@Configuration
public class Config {
}
