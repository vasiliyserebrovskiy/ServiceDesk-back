package com.sitool.servicedesk.category.controller;

import com.sitool.servicedesk.category.dto.request.CreateCategoryRequest;
import com.sitool.servicedesk.category.dto.request.UpdateCategoryRequest;
import com.sitool.servicedesk.category.dto.responce.CategoryDto;
import com.sitool.servicedesk.exceptions.handling.response.ValidationErrorDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * API contract for category-related operations.
 */
@Tag(name = "Categories", description = "Operations related to categories")
@RequestMapping("/api/v1/categories")
public interface CategoryApi {
    @Operation(summary = "Register new category", description = "Creates a new category record.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Category created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CategoryDto.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "id": "767ea865-8b32-454c-af05-52508be4033c",
                                      "name": "Hardware",
                                      "description":"Hardware category for incidents",
                                      "isIncident":true,
                                      "isProblem":false,
                                      "isRequest":false,
                                      "isChange":false
                                    }
                                    """))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Category already exists"
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
    CategoryDto createCategory(@RequestBody CreateCategoryRequest request);

    @Operation(
            summary = "Update category",
            description = "Updates editable information for the specified category."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Group updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CategoryDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request data or UUID format"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Category not found"
            )
    })
    @PutMapping("/{categoryId}")
    CategoryDto updateCategory(@PathVariable UUID categoryId, @RequestBody UpdateCategoryRequest request);

    @Operation(
            summary = "Delete category",
            description = "Deletes the category with the specified groupId (UUID)."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Category deleted successfully"
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
    @DeleteMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteCategory(@PathVariable UUID categoryId);

    @Operation(
            summary = "Get category by id",
            description = "Returns category information for the specified categoryId (UUID)."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Category retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CategoryDto.class)
                    )
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
    @GetMapping("/{categoryId}")
    CategoryDto getCategory(@PathVariable UUID categoryId);

    @Operation(
            summary = "Get all categories",
            description = "Returns a list of all categories."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Categories retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = CategoryDto.class))
            )
    )
    @GetMapping
    List<CategoryDto> getAllCategories(@RequestParam(required = false) String type);


}
