package com.sitool.servicedesk.bootstrap;

import com.sitool.servicedesk.role.service.RoleService;
import com.sitool.servicedesk.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


/**
 * Initializes default application data during startup.
 * Create the default roles if they do not already exist.
 * Creates the default administrator account if it does not already exist.
 */
@Component
@RequiredArgsConstructor
public class ApplicationInitializer implements CommandLineRunner {
    private final UserService userService;
    private final RoleService roleService;

    /**
     * Executes initialization logic after application startup.
     *
     * @param args application startup arguments
     */
    @Override
    public void run(String... args) {
        // create Roles fisrt
        roleService.createRolesIfNorExists();
        // create admin user
        userService.createAdminIfNotExists();
    }
}
