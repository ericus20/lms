package com.developersboard.lms.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Created by Eric on 2/9/2018.
 *
 * Configuration class for only Production Environment configurations.
 *
 * @author Eric Opoku
 */
@Configuration
@Profile(value = {"prod-mysql", "prod-oracle"})
public class ProductionConfig {

}
