package com.sitool.servicedesk.ci.dto.request;

import com.sitool.servicedesk.ci.constraints.CIValidationConstraints;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request payload for updating a CI.
 */
public record UpdateCIRequest(
        @NotBlank(message = "{ci.name.notBlank}")
        @Size(
                min = CIValidationConstraints.NAME_MIN_LENGTH,
                max = CIValidationConstraints.NAME_MAX_LENGTH,
                message = "{ci.name.length}"
        )
        String name,
        String description,
        @Size(
                max = CIValidationConstraints.TYPE_MAX_LENGTH,
                message = "{ci.type.length}"
        )
        String type,
        @Size(
                max = CIValidationConstraints.MANUFACTURER_MAX_LENGTH,
                message = "{ci.manufacturer.length}"
        )
        String manufacturer,
        @Size(
                max = CIValidationConstraints.SERIAL_NUMBER_MAX_LENGTH,
                message = "{ci.serialNumber.length}"
        )
        String serialNumber,
        @Size(
                max = CIValidationConstraints.MODEL_MAX_LENGTH,
                message = "{ci.model.length}"
        )
        String model
) {
}
