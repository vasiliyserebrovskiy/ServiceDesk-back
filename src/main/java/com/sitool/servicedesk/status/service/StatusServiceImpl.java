package com.sitool.servicedesk.status.service;

import com.sitool.servicedesk.status.dto.request.CreateStatusRequest;
import com.sitool.servicedesk.status.dto.request.UpdateStatusRequest;
import com.sitool.servicedesk.status.dto.response.StatusDto;
import com.sitool.servicedesk.status.entity.Status;
import com.sitool.servicedesk.status.exceptions.StatusAlreadyExistException;
import com.sitool.servicedesk.status.exceptions.StatusNotFoundException;
import com.sitool.servicedesk.status.mapper.StatusMapper;
import com.sitool.servicedesk.status.repository.StatusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Implementation of {@link StatusService} for managing ticket statuses.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StatusServiceImpl implements StatusService {

    private final StatusRepository statusRepository;
    private final StatusMapper statusMapper;

    /**
     * Creates a new status.
     *
     * <p>Normalizes the status name by trimming whitespace before saving.</p>
     *
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public StatusDto createStatus(CreateStatusRequest request) {
        String normalizeName = request.name().trim();
        if (statusRepository.existsByNameIgnoreCase(normalizeName)) {
            log.info("createStatus: Status name {} already exists", normalizeName);
            throw new StatusAlreadyExistException();
        }

        Status status = new Status();
        status.setName(normalizeName);
        status.setDescription(request.description());
        status.setIsIncident(request.isIncident());
        status.setIsProblem(request.isProblem());
        status.setIsRequest(request.isRequest());
        status.setIsChange(request.isChange());
        status.setIsTask(request.isTask());

        statusRepository.save(status);

        return statusMapper.statusToStatusDto(status);
    }

    /**
     * Updates an existing status.
     *
     * <p>Only modified fields are persisted to avoid unnecessary database calls.</p>
     *
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public StatusDto updateStatus(UUID statusId, UpdateStatusRequest request) {
        boolean updated = false;
        String normalizeName = request.name().trim();

        Status currentStatus = statusRepository.findById(statusId).orElseThrow(() -> {
            log.info("updateStatus: Status id {} not found", statusId);
            return new StatusNotFoundException();
        });

        if (!currentStatus.getName().equalsIgnoreCase(normalizeName) && statusRepository.existsByNameIgnoreCase(normalizeName)) {
            log.info("updateStatus: Status name {} already exists", normalizeName);
            throw new StatusAlreadyExistException();
        }

        if (!currentStatus.getName().equals(normalizeName)) {
            updated = true;
            currentStatus.setName(normalizeName);
        }

        if (!Objects.equals(currentStatus.getDescription(), request.description())) {
            updated = true;
            currentStatus.setDescription(request.description());
        }
        if (currentStatus.getIsIncident() != request.isIncident()) {
            updated = true;
            currentStatus.setIsIncident(request.isIncident());
        }
        if (currentStatus.getIsProblem() != request.isProblem()) {
            updated = true;
            currentStatus.setIsProblem(request.isProblem());
        }
        if (currentStatus.getIsRequest() != request.isRequest()) {
            updated = true;
            currentStatus.setIsRequest(request.isRequest());
        }
        if (currentStatus.getIsChange() != request.isChange()) {
            updated = true;
            currentStatus.setIsChange(request.isChange());
        }
        if (currentStatus.getIsTask() != request.isTask()) {
            updated = true;
            currentStatus.setIsTask(request.isTask());
        }

        if (updated) {
            statusRepository.save(currentStatus);
        }

        return statusMapper.statusToStatusDto(currentStatus);
    }

    /**
     * Deletes a status by its ID.
     *
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void deleteStatus(UUID statusId) {
        Status currentStatus = statusRepository.findById(statusId).orElseThrow(() -> {
            log.info("deleteStatus: Status id {} not found", statusId);
            return new StatusNotFoundException();
        });
        statusRepository.delete(currentStatus);
    }

    @Override
    @Transactional(readOnly = true)
    public StatusDto getStatus(UUID statusId) {
        Status currentStatus = statusRepository.findById(statusId).orElseThrow(() -> {
            log.info("getStatus: Status id {} not found", statusId);
            return new StatusNotFoundException();
        });
        return statusMapper.statusToStatusDto(currentStatus);
    }

    /**
     * Returns a status by its ID.
     *
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<StatusDto> getAllStatuses(String type) {
        if (type == null || type.isBlank()) {
            return statusRepository.findAll()
                    .stream()
                    .map(statusMapper::statusToStatusDto)
                    .toList();
        }
        List<Status> statuses = switch (type.toUpperCase()) {
            case "INCIDENT" -> statusRepository.findAllByIsIncidentTrue();
            case "PROBLEM" -> statusRepository.findAllByIsProblemTrue();
            case "REQUEST" -> statusRepository.findAllByIsRequestTrue();
            case "CHANGE" -> statusRepository.findAllByIsChangeTrue();
            case "TASK" -> statusRepository.findAllByIsTaskTrue();
            default -> statusRepository.findAll();
        };

        /**
         * Returns statuses filtered by ticket type.
         *
         * <p>If type is null or blank, all statuses are returned.
         * Unrecognized type values fall back to returning all statuses.</p>
         *
         * {@inheritDoc}
         */
        return statuses.stream()
                .map(statusMapper::statusToStatusDto)
                .toList();
    }
}
