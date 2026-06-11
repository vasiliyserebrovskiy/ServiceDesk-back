package com.sitool.servicedesk.status.service;

import com.sitool.servicedesk.status.dto.request.CreateStatusRequest;
import com.sitool.servicedesk.status.dto.request.UpdateStatusRequest;
import com.sitool.servicedesk.status.dto.response.StatusDto;
import com.sitool.servicedesk.status.exceptions.StatusAlreadyExistException;
import com.sitool.servicedesk.status.exceptions.StatusNotFoundException;

import java.util.List;
import java.util.UUID;

/**
 * Service interface for managing ticket statuses.
 *
 * <p>Statuses represent the current state of a ticket in its lifecycle.
 * Each status can be assigned to one or more ticket types:
 * Incident, Problem, Request, Change, or Task.</p>
 */
public interface StatusService {

    /**
     * Creates a new status.
     *
     * @param request the request containing status data
     * @return the created status
     * @throws StatusAlreadyExistException if a status with the same name already exists
     */
    StatusDto createStatus(CreateStatusRequest request);

    /**
     * Updates an existing status.
     *
     * @param statusId the UUID of the status to update
     * @param request  the request containing updated status data
     * @return the updated status
     * @throws StatusNotFoundException     if no status with the given ID exists
     * @throws StatusAlreadyExistException if a status with the new name already exists
     */
    StatusDto updateStatus(UUID statusId, UpdateStatusRequest request);

    /**
     * Deletes a status by its ID.
     *
     * @param statusId the UUID of the status to delete
     * @throws StatusNotFoundException if no status with the given ID exists
     */
    void deleteStatus(UUID statusId);

    /**
     * Returns a status by its ID.
     *
     * @param statusId the UUID of the status to retrieve
     * @return the status data
     * @throws StatusNotFoundException if no status with the given ID exists
     */
    StatusDto getStatus(UUID statusId);

    /**
     * Returns a list of statuses filtered by ticket type.
     *
     * <p>Supported types: INCIDENT, PROBLEM, REQUEST, CHANGE, TASK.
     * If no type is provided, all statuses are returned.</p>
     *
     * @param type optional ticket type filter (case-insensitive)
     * @return list of matching statuses
     */
    List<StatusDto> getAllStatuses(String type);
}
