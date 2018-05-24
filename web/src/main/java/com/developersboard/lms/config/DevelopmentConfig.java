package com.developersboard.lms.config;

import org.h2.server.web.WebServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Created by Eric on 2/18/2018.
 *
 * Configuration class for only Dev Environment configurations.
 *
 * @author Eric Opoku
 */
@Configuration
@Profile("dev")
public class DevelopmentConfig {

    @Bean
    public ServletRegistrationBean<WebServlet> h2ConsoleServletRegistration() {
        return new ServletRegistrationBean<>(new WebServlet(), "/console/*");
    }
}
