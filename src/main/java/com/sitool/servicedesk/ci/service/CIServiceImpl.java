package com.sitool.servicedesk.ci.service;

import com.sitool.servicedesk.ci.dto.request.CreateCIRequest;
import com.sitool.servicedesk.ci.dto.request.UpdateCIRequest;
import com.sitool.servicedesk.ci.dto.response.CIDto;
import com.sitool.servicedesk.ci.entity.CI;
import com.sitool.servicedesk.ci.exceptions.CIAlreadyExistException;
import com.sitool.servicedesk.ci.exceptions.CINotFoundException;
import com.sitool.servicedesk.ci.mapper.CIMapper;
import com.sitool.servicedesk.ci.repository.CIRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Implementation of {@link CIService} for managing configuration items.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CIServiceImpl implements CIService {

    private final CIRepository ciRepository;
    private final CIMapper ciMapper;

    /**
     * Creates a new configuration item.
     *
     * <p>Normalizes the CI name by trimming whitespace before saving.</p>
     *
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public CIDto createCI(CreateCIRequest request) {

        String normalizedName = request.name().trim();
        if (ciRepository.existsByNameIgnoreCase(normalizedName)) {
            log.info("createCI: CI already exists with the name {}", normalizedName);
            throw new CIAlreadyExistException();
        }

        CI ci = new CI();
        ci.setName(normalizedName);
        ci.setDescription(request.description());
        ci.setType(request.type());
        ci.setManufacturer(request.manufacturer());
        ci.setSerialNumber(request.serialNumber());
        ci.setModel(request.model());

        ciRepository.save(ci);
        return ciMapper.ciToCIDto(ci);
    }

    /**
     * Updates an existing configuration item.
     *
     * <p>Only modified fields are persisted to avoid unnecessary database calls.</p>
     *
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public CIDto updateCI(UUID ciId, UpdateCIRequest request) {
        boolean updated = false;
        String normalizedName = request.name().trim();

        CI currentCI = ciRepository.findById(ciId).orElseThrow(()-> {
            log.info("updateCI: No CI found with id {}", ciId);
            return new CINotFoundException();
        });

        if (!currentCI.getName().equalsIgnoreCase(normalizedName) && ciRepository.existsByNameIgnoreCase(normalizedName)) {
            log.info("updateCI: CI already exists with the name {}", normalizedName);
            throw new CIAlreadyExistException();
        }

        if (!currentCI.getName().equals(normalizedName)) {
            currentCI.setName(normalizedName);
            updated = true;
        }

        if (!Objects.equals(currentCI.getDescription(), request.description())) {
            currentCI.setDescription(request.description());
            updated = true;
        }

        if (!Objects.equals(currentCI.getType(), request.type())) {
            currentCI.setType(request.type());
            updated = true;
        }

        if (!Objects.equals(currentCI.getManufacturer(), request.manufacturer())) {
            currentCI.setManufacturer(request.manufacturer());
            updated = true;
        }

        if (!Objects.equals(currentCI.getSerialNumber(), request.serialNumber())) {
            currentCI.setSerialNumber(request.serialNumber());
            updated = true;
        }

        if (!Objects.equals(currentCI.getModel(), request.model())) {
            currentCI.setModel(request.model());
            updated = true;
        }

        if (updated) {
            ciRepository.save(currentCI);
        }

        return ciMapper.ciToCIDto(currentCI);
    }

    /**
     * Deletes a configuration item by its ID.
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void deleteCI(UUID ciId) {
        CI currentCI = ciRepository.findById(ciId).orElseThrow(() -> {
            log.info("deleteCI: No CI found with id {}", ciId);
            return new CINotFoundException();
        });

        ciRepository.delete(currentCI);
    }

    /**
     * Returns a configuration item by its ID.
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public CIDto getCIById(UUID ciId) {
        CI currentCI = ciRepository.findById(ciId).orElseThrow(() -> {
            log.info("getCIById: No CI found with id {}", ciId);
            return new CINotFoundException();
        });

        return ciMapper.ciToCIDto(currentCI);
    }

    /**
     * Returns all configuration items.
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<CIDto> getAllCI() {
        return ciRepository.findAll()
                .stream()
                .map(ciMapper::ciToCIDto)
                .toList();
    }
}
