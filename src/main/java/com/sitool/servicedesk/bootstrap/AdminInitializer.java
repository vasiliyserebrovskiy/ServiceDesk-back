package com.sitool.servicedesk.bootstrap;

import com.sitool.servicedesk.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


/**
 * Initializes default application data during startup.
 * Creates the default administrator account
 * if it does not already exist.
 */
@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {
    private final UserService userService;

    /**
     * Executes initialization logic after application startup.
     *
     * @param args application startup arguments
     */
    @Override
    public void run(String... args) {
        userService.createAdminIfNotExists();
    }
}
