package com.sitool.servicedesk.role.controller;

import com.sitool.servicedesk.role.dto.response.RoleDto;
import com.sitool.servicedesk.role.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller implementation for role operations.
 */
@RestController
@RequiredArgsConstructor
public class RoleController implements  RoleApi {

    private final RoleService roleService;

    @Override
    public List<RoleDto> getRoles() {
        return roleService.getRoles();
    }
}
