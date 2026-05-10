package com.sitool.servicedesk.role.repository;

import com.sitool.servicedesk.role.entity.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    @DisplayName("Should find default role")
    void shouldFindDefaultRole() {

        Role userRole = new Role();
        userRole.setName("USER");
        userRole.setDescription("Default user role");
        userRole.setDefaultRole(true);

        roleRepository.save(userRole);

        Optional<Role> result =
                roleRepository.findByDefaultRoleTrue();

        assertTrue(result.isPresent());
        assertEquals("USER", result.get().getName());
        assertTrue(result.get().isDefaultRole());
    }

    @Test
    @DisplayName("Should return empty optional when default role does not exist")
    void shouldReturnEmptyWhenDefaultRoleDoesNotExist() {

        Role adminRole = new Role();
        adminRole.setName("ADMIN");
        adminRole.setDescription("Administrator role");
        adminRole.setDefaultRole(false);

        roleRepository.save(adminRole);

        Optional<Role> result =
                roleRepository.findByDefaultRoleTrue();

        assertTrue(result.isEmpty());
    }

}
