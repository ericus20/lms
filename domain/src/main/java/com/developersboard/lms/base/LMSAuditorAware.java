package com.developersboard.lms.base;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * Created by Eric on 3/29/2018.
 *
 * @author Eric Opoku
 */
public class LMSAuditorAware implements AuditorAware<String> {
    /**
     * Returns the current auditor of the application.
     *
     * @return the current auditor
     */
    @Override
    public Optional<String> getCurrentAuditor() {

        // Check if there is a user logged in. If so, use the logged in user as the current auditor.
        // spring injects an anonymousUser if there is no authentication and authorization
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null
                && authentication.isAuthenticated()
                && authentication.getPrincipal() != "anonymousUser") {
            return Optional.ofNullable(authentication.getName());
        }

        // If there is no authentication, then the system will be used as the current auditor.
        return Optional.of("system");

    }
}
