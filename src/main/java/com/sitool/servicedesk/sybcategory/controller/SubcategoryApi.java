package com.sitool.servicedesk.sybcategory.controller;

import com.sitool.servicedesk.exceptions.handling.response.ValidationErrorDto;
import com.sitool.servicedesk.sybcategory.dto.request.CreateSubcategoryRequest;
import com.sitool.servicedesk.sybcategory.dto.request.UpdateSubcategoryRequest;
import com.sitool.servicedesk.sybcategory.dto.response.SubcategoryDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * API contract for subcategory-related operations.
 */
@Tag(name = "Subcategories", description = "Operations related to subcategories")
@RequestMapping("/api/v1/subcategories")
public interface SubcategoryApi {

    @Operation(summary = "Register new subcategory", description = "Creates a new subcategory record.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Subcategory created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SubcategoryDto.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "id": "767ea865-8b32-454c-af05-52508be4033c",
                                      "name": "CPU",
                                      "description":"CPU subcategory",
                                      "categoryId":"a37282e7-3279-4583-8699-48db9e65fd4d"
                                    }
                                    """))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Subcategory already exists"
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request payload",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ValidationErrorDto.class)),
                            examples = @ExampleObject(value = """
                                    [
                                      { "field": "name",
                                        "message": "must be a well-formed name" },
                                      { "field": "name",
                                        "message": "must not be blank" }
                                    ]
                                    """))
            )
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    SubcategoryDto  createSubcategory(@Valid @RequestBody CreateSubcategoryRequest request);

    @Operation(
            summary = "Update subcategory",
            description = "Updates editable information for the specified subcategory."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Subcategory updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SubcategoryDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request data or UUID format"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Subcategory not found"
            )
    })
    @PutMapping("/{subcategoryId}")
    SubcategoryDto updateSubcategory(@PathVariable UUID subcategoryId, @Valid @RequestBody UpdateSubcategoryRequest request);

    @Operation(
            summary = "Delete subcategory",
            description = "Deletes the subcategory with the specified subcategoryId (UUID)."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Subcategory deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid UUID format"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Category not found"
            )
    })
    @DeleteMapping("/{subcategoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteSubcategory(@PathVariable UUID subcategoryId);

    @Operation(
            summary = "Get subcategory by id",
            description = "Returns subcategory information for the specified subcategoryId (UUID)."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Subcategory retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SubcategoryDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid UUID format"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Subcategory not found"
            )
    })
    @GetMapping("/{subcategoryId}")
    SubcategoryDto getSubcategoryById(@PathVariable UUID subcategoryId);

    @Operation(
            summary = "Get all subcategories",
            description = "Returns a list of all subcategories."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Subcategories retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = SubcategoryDto.class))
            )
    )
    @GetMapping
    List<SubcategoryDto> getAllSubcategories(@RequestParam(required = false) UUID categoryId);

}
