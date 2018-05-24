package com.developersboard.lms.config;

import com.developersboard.lms.base.LMSAuditorAware;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by Eric on 2/9/2018.
 *
 * @author Eric Opoku
 */
@Configuration
@EnableTransactionManagement
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableJpaRepositories(basePackages = "com.developersboard.lms.repository")
@EntityScan(basePackages = "com.developersboard.lms")
public class JpaConfig {

    /**
     * A bean to be served for teh AuditorAware interface.
     * @return auditorAware instance.
     */
    @Bean
    public AuditorAware<String> auditorAware() {
        return new LMSAuditorAware();
    }
}
