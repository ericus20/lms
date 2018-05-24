package com.developersboard.lms.service.bootstrap;

import com.developersboard.lms.enums.PlansEnum;
import com.developersboard.lms.enums.RolesEnum;
import com.developersboard.lms.security.Plan;
import com.developersboard.lms.security.Role;
import com.developersboard.lms.security.User;
import com.developersboard.lms.service.security.PlanService;
import com.developersboard.lms.service.security.RoleService;
import com.developersboard.lms.service.security.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Created by Eric on 3/29/2018.
 *
 * @author Eric Opoku
 */
@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final PlanService planService;
    private final RoleService roleService;
    private final UserService userService;

    @Value("${webmaster.username}")
    private String webMasterUsername;

    @Value("${webmaster.password}")
    private String webMasterPassword;

    @Autowired
    public DatabaseSeeder(
            PlanService planService,
            RoleService roleService, UserService userService) {
        this.planService = planService;
        this.roleService = roleService;
        this.userService = userService;
    }

    /**
     * Callback used to run the bean.
     *
     * @param args incoming main method arguments
     */
    @Override
    public void run(String... args) {

        Optional<Role> adminRole = roleService.saveRole(new Role(RolesEnum.ADMIN));
        Optional<Role> userRole = roleService.saveRole(new Role(RolesEnum.USER));
        Optional<Role> librarianRole = roleService.saveRole(new Role(RolesEnum.LIBRARIAN));

        planService.createPlan(new Plan(PlansEnum.BASIC));
        planService.createPlan(new Plan(PlansEnum.PRO));

        assert adminRole.isPresent();
        assert userRole.isPresent();
        assert librarianRole.isPresent();

        User admin = new User();
        admin.setUsername(webMasterUsername);
        admin.setPassword(webMasterPassword);
        admin.setEmail("admin@email.com");
        admin.setDescription("The administrator for LMS");

        /* Admin is practically a user, librarian with admin roles */
        Set<Role> roles = new HashSet<>();
        roles.add(adminRole.get());

        userService.createUser(admin, roles);
    }
}
