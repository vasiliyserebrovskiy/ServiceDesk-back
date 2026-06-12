package com.sitool.servicedesk.ci.controller;

import com.sitool.servicedesk.ci.dto.request.CreateCIRequest;
import com.sitool.servicedesk.ci.dto.request.UpdateCIRequest;
import com.sitool.servicedesk.ci.dto.response.CIDto;
import com.sitool.servicedesk.ci.service.CIService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * REST controller that delegates configuration item operations to the CIService.
 */
@RestController
@RequiredArgsConstructor
public class CIController implements CIApi {

    private final CIService ciService;

    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public CIDto createCI(CreateCIRequest request) {
        return ciService.createCI(request);
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public CIDto updateCI(UUID ciId, UpdateCIRequest request) {
        return ciService.updateCI(ciId, request);
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public void deleteCI(UUID ciId) {
        ciService.deleteCI(ciId);
    }

    @Override
    public CIDto getCIById(UUID ciId) {
        return ciService.getCIById(ciId);
    }

    @Override
    public List<CIDto> getAllCI() {
        return ciService.getAllCI();
    }
}
