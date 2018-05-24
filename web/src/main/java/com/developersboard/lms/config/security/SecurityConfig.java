package com.developersboard.lms.config.security;

import com.developersboard.lms.service.security.impl.UserSecurityService;
import com.developersboard.lms.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.Arrays;
import java.util.List;

import static com.developersboard.lms.constant.signup.PasswordTokenConstants.CHANGE_PASSWORD_PATH;
import static com.developersboard.lms.constant.signup.PasswordTokenConstants.FORGOT_PASSWORD_URL_MAPPING;

/**
 * Created by Matthew on 2/9/2018.
 *
 * @author Matthew
 * @author Eric
 */

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    private final Environment environment;
    private final UserSecurityService userSecurityService;
    private final LMSAuthenticationEntryPoint authEntryPoint;

    /**
     * Public URLs
     */
    private static final String[] PUBLIC_MATCHERS = {

            "/webjars/**",
            "/css/**",
            "/js/**",
            "/images/**",
            "/about/**",
            "/contact/**",
            "/signup/**",
            "/console/**",
            "/error/**",
            "/library/**",
            "/lms/**",
            "/",

            FORGOT_PASSWORD_URL_MAPPING,
            CHANGE_PASSWORD_PATH,

    };

    @Autowired
    public SecurityConfig(UserSecurityService userSecurityService,
                          Environment environment,
                          LMSAuthenticationEntryPoint authEntryPoint) {
        this.userSecurityService = userSecurityService;
        this.environment = environment;
        this.authEntryPoint = authEntryPoint;
    }


    /**
     * Override this method to configure the {@link HttpSecurity}. Typically subclasses
     * should not invoke this method by calling super as it may override their
     * configuration. The default configuration is:
     * <p>
     * <pre>
     * http.authorizeRequests().anyRequest().authenticated().and().formLogin().and().httpBasic();
     * </pre>
     *
     * @param http the {@link HttpSecurity} to modify
     * @throws Exception if an error occurs
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        /*
         * If we are running with dev profile, disable csrf and frame options to enable h2
         * to work.
         */
        List<String> activeProfiles = Arrays.asList(environment.getActiveProfiles());
        if (activeProfiles.contains("dev") || activeProfiles.contains("prod-mysql") ||
                activeProfiles.contains("prod-oracle")) {
            http.csrf().disable();
            http.headers().frameOptions().disable();
        }

        /* URLs to be authorized and some authenticated.*/
        http
                .httpBasic().authenticationEntryPoint(authEntryPoint).and()
                .authorizeRequests()
                .antMatchers(PUBLIC_MATCHERS).permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated();

        http
                .formLogin().loginPage("/login")
                    .successHandler(new LMSAuthenticationSuccessHandler())
                    .failureUrl("/login?error").permitAll()
                .and()
                .exceptionHandling().accessDeniedPage("/error")
                .and()
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutSuccessUrl("/login?logout")
                    .deleteCookies("remember-me").deleteCookies("JSESSIONID")
                    .invalidateHttpSession(true).permitAll()
                .and()
                .rememberMe();
    }

    /**
     * Configures global user's with authentication credentials.
     *
     * @param auth to easily build in memory, LDAP, and JDBC authentication
     * @throws Exception If anything goes wrong, there will be an exception thrown.
     */
    @Autowired
    protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userSecurityService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return SecurityUtils.passwordEncoder();
    }
}
