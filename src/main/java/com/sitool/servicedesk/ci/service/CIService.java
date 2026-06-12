package com.sitool.servicedesk.ci.service;

import com.sitool.servicedesk.ci.dto.request.CreateCIRequest;
import com.sitool.servicedesk.ci.dto.request.UpdateCIRequest;
import com.sitool.servicedesk.ci.dto.response.CIDto;
import com.sitool.servicedesk.ci.exceptions.CIAlreadyExistException;
import com.sitool.servicedesk.ci.exceptions.CINotFoundException;

import java.util.List;
import java.util.UUID;

/**
 * Service interface for managing configuration items.
 *
 * <p>Configuration Items (CI) represent physical or logical assets
 * tracked within the service desk system, such as servers, network
 * equipment, or software components.</p>
 */
public interface CIService {

    /**
     * Creates a new configuration item.
     *
     * @param request the request containing configuration item data
     * @return the created configuration item
     * @throws CIAlreadyExistException if a configuration item with the same name already exists
     */
    CIDto createCI(CreateCIRequest request);

    /**
     * Updates an existing configuration item.
     *
     * @param ciId    the UUID of the configuration item to update
     * @param request the request containing updated configuration item data
     * @return the updated configuration item
     * @throws CINotFoundException     if no configuration item with the given ID exists
     * @throws CIAlreadyExistException if a configuration item with the new name already exists
     */
    CIDto updateCI(UUID ciId, UpdateCIRequest request);

    /**
     * Deletes a configuration item by its ID.
     *
     * @param ciId the UUID of the configuration item to delete
     * @throws CINotFoundException if no configuration item with the given ID exists
     */
    void deleteCI(UUID ciId);

    /**
     * Returns a configuration item by its ID.
     *
     * @param ciId the UUID of the configuration item to retrieve
     * @return the configuration item data
     * @throws CINotFoundException if no configuration item with the given ID exists
     */
    CIDto getCIById(UUID ciId);

    /**
     * Returns a list of all configuration items.
     *
     * @return list of all configuration items
     */
    List<CIDto> getAllCI();
}
