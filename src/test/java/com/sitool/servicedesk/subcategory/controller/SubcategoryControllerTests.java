package com.sitool.servicedesk.subcategory.controller;

import com.sitool.servicedesk.category.dto.request.UpdateCategoryRequest;
import com.sitool.servicedesk.category.dto.response.CategoryDto;
import com.sitool.servicedesk.security.service.CustomUserDetailsService;
import com.sitool.servicedesk.security.service.JwtTokenService;
import com.sitool.servicedesk.sybcategory.controller.SubcategoryController;
import com.sitool.servicedesk.sybcategory.dto.request.CreateSubcategoryRequest;
import com.sitool.servicedesk.sybcategory.dto.request.UpdateSubcategoryRequest;
import com.sitool.servicedesk.sybcategory.dto.response.SubcategoryDto;
import com.sitool.servicedesk.sybcategory.service.SubcategoryService;
import com.sitool.servicedesk.token.service.RefreshTokenService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SubcategoryController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(SubcategoryControllerTests.TestConfig.class)
@TestPropertySource(properties = {
        "jwt.at.live-in-min=15",
        "jwt.rt.live-in-min=60"
})
public class SubcategoryControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SubcategoryService subcategoryService;

    @TestConfiguration
    static class TestConfig {
        @Bean
        SubcategoryService subcategoryService() {return mock(SubcategoryService.class);}

        @Bean
        JwtTokenService jwtTokenService() {return mock(JwtTokenService.class);}

        @Bean
        CustomUserDetailsService  customUserDetailsService() {return mock(CustomUserDetailsService.class);}

        @Bean
        RefreshTokenService refreshTokenService() {return mock(RefreshTokenService.class);}
    }

    @Test
    @WithMockUser
    @DisplayName("Should create subcategory and return 201")
    void createSubcategory_shouldReturnCreated() throws Exception {

        UUID categoryId = UUID.randomUUID();

        SubcategoryDto response = new SubcategoryDto(
                UUID.randomUUID(),
                "CPU",
                "Some description",
                categoryId
        );

        when(subcategoryService.createSubcategory(any(CreateSubcategoryRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/v1/subcategories")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                                {
                                "name": "CPU",
                                "description":"Some description",
                                "categoryId": "%s"
                                }
                                """.formatted(categoryId))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("CPU"))
                .andExpect(jsonPath("$.description").value("Some description"))
                .andExpect(jsonPath("$.categoryId").value(categoryId.toString()));

        verify(subcategoryService).createSubcategory(any(CreateSubcategoryRequest.class));
    }

    @Test
    @WithMockUser
    @DisplayName("Should return 400 when create request is invalid")
    void createSubcategory_shouldReturnBadRequest_whenInvalidInput() throws Exception {
        mockMvc.perform(post("/api/v1/subcategories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "name": "",
                                "description":"Some description"
                                }
                                """)
                )
                .andExpect(status().isBadRequest());

        verify(subcategoryService, never()).createSubcategory(any());
    }

    @Test
    @WithMockUser
    @DisplayName("Should update category and return 200")
    void updateCategory_shouldReturnUpdatedCategory() throws Exception {
        UUID subcategoryId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();

        SubcategoryDto response = new SubcategoryDto(
                subcategoryId,
                "Updated Subcategory",
                "Updated Description",
                categoryId
        );

        when(subcategoryService.updateSubcategory(any(UUID.class), any(UpdateSubcategoryRequest.class)))
                .thenReturn(response);

        mockMvc.perform(put("/api/v1/subcategories/{subcategoryId}", subcategoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "name": "Updated Subcategory",
                          "description":"Updated Description",
                          "categoryId": "%s"
                        }
                        """.formatted(categoryId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(subcategoryId.toString()))
                .andExpect(jsonPath("$.name").value("Updated Subcategory"))
                .andExpect(jsonPath("$.description").value("Updated Description"))
                .andExpect(jsonPath("$.categoryId").value(categoryId.toString()));

        verify(subcategoryService).updateSubcategory(any(UUID.class), any(UpdateSubcategoryRequest.class));

    }

    @Test
    @WithMockUser
    @DisplayName("Should delete subcategory and return 204")
    void deleteSubcategory_shouldReturnNoContent() throws Exception {

        UUID subcategoryId = UUID.randomUUID();

        doNothing().when(subcategoryService).deleteSubcategory(subcategoryId);

        mockMvc.perform(delete("/api/v1/subcategories/{subcategoryId}", subcategoryId))
                .andExpect(status().isNoContent());

        verify(subcategoryService).deleteSubcategory(subcategoryId);
    }

    @Test
    @WithMockUser
    @DisplayName("Should return subcategory by id")
    void getSubcategory_shouldReturnCategory() throws Exception {
        UUID subcategoryId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();

        SubcategoryDto response = new SubcategoryDto(
                subcategoryId,
                "Hardware",
                "Some description",
                categoryId
        );

        when(subcategoryService.getSubcategoryById(subcategoryId))
                .thenReturn(response);

        mockMvc.perform(get("/api/v1/subcategories/{subcategoryId}", subcategoryId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(subcategoryId.toString()))
                .andExpect(jsonPath("$.name").value("Hardware"))
                .andExpect(jsonPath("$.description").value("Some description"))
                .andExpect(jsonPath("$.categoryId").value(categoryId.toString()));

        verify(subcategoryService).getSubcategoryById(subcategoryId);
    }

    @Test
    @WithMockUser
    @DisplayName("Should return all subcategories")
    void getAllSubcategories_shouldReturnAllIncidentCategories() throws Exception {
        UUID categoryId = UUID.randomUUID();
        SubcategoryDto first = new SubcategoryDto(
                UUID.randomUUID(),
                "CPU",
                "First description",
                categoryId
        );

        SubcategoryDto second = new SubcategoryDto(
                UUID.randomUUID(),
                "Memory",
                "Second description",
                categoryId
        );

        when(subcategoryService.getAllSubcategories(categoryId))
                .thenReturn(List.of(first, second));

        mockMvc.perform(get("/api/v1/subcategories?categoryId={categoryId}", categoryId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("CPU"))
                .andExpect(jsonPath("$[1].name").value("Memory"));

        verify(subcategoryService).getAllSubcategories(categoryId);
    }
}
