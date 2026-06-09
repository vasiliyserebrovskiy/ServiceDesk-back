package com.sitool.servicedesk.category.controller;

import com.sitool.servicedesk.category.dto.request.CreateCategoryRequest;
import com.sitool.servicedesk.category.dto.request.UpdateCategoryRequest;
import com.sitool.servicedesk.category.dto.response.CategoryDto;
import com.sitool.servicedesk.category.service.CategoryService;
import com.sitool.servicedesk.security.service.CustomUserDetailsService;
import com.sitool.servicedesk.security.service.JwtTokenService;
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

@WebMvcTest(CategoryController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(CategoryControllerTests.TestConfig.class)
@TestPropertySource(properties = {
        "jwt.at.live-in-min=15",
        "jwt.rt.live-in-min=60"
})
public class CategoryControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoryService categoryService;

    @TestConfiguration
    static class TestConfig {
        @Bean
        CategoryService categoryService() {
            return mock(CategoryService.class);
        }

        @Bean
        JwtTokenService jwtTokenService() {
            return mock(JwtTokenService.class);
        }

        @Bean
        CustomUserDetailsService customUserDetailsService() {
            return mock(CustomUserDetailsService.class);
        }

        @Bean
        RefreshTokenService refreshTokenService() {
            return mock(RefreshTokenService.class);
        }
    }

    @Test
    @WithMockUser
    @DisplayName("Should create category and return 201")
    void createCategory_shouldReturnCreated() throws Exception {

        CategoryDto response = new CategoryDto(
                UUID.randomUUID(),
                "Hardware",
                "Some description",
                true,
                false,
                false,
                false
        );

        when(categoryService.createCategory(any(CreateCategoryRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "name": "Hardware",
                                "description":"Some description",
                                "isIncident": true,
                                "isProblem": false,
                                "isRequest": false,
                                "isChange": false
                                }
                                """)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Hardware"))
                .andExpect(jsonPath("$.description").value("Some description"))
                .andExpect(jsonPath("$.isIncident").value(true))
                .andExpect(jsonPath("$.isProblem").value(false))
                .andExpect(jsonPath("$.isRequest").value(false))
                .andExpect(jsonPath("$.isChange").value(false));

        verify(categoryService).createCategory(any(CreateCategoryRequest.class));

    }

    @Test
    @WithMockUser
    @DisplayName("Should return 400 when create request is invalid")
    void createCategory_shouldReturnBadRequest_whenInvalidInput() throws Exception {
        mockMvc.perform(post("/api/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "name": "",
                                "description":"Some description"
                                }
                                """)
                )
                .andExpect(status().isBadRequest());

        verify(categoryService, never()).createCategory(any());
    }

    @Test
    @WithMockUser
    @DisplayName("Should update category and return 200")
    void updateCategory_shouldReturnUpdatedCategory() throws Exception {
        UUID categoryId = UUID.randomUUID();

        CategoryDto response = new CategoryDto(
                categoryId,
                "Updated Category",
                "Updated Description",
                true,
                false,
                false,
                false
        );

        when(categoryService.updateCategory(any(UUID.class), any(UpdateCategoryRequest.class)))
                .thenReturn(response);

        mockMvc.perform(put("/api/v1/categories/{categoryId}", categoryId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "name": "Updated Category",
                          "description":"Updated Description",
                          "isIncident": true,
                          "isProblem": false,
                          "isRequest": false,
                          "isChange": false
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(categoryId.toString()))
                .andExpect(jsonPath("$.name").value("Updated Category"))
                .andExpect(jsonPath("$.description").value("Updated Description"));

        verify(categoryService).updateCategory(any(UUID.class), any(UpdateCategoryRequest.class));

    }

    @Test
    @WithMockUser
    @DisplayName("Should delete category and return 204")
    void deleteCategory_shouldReturnNoContent() throws Exception {

        UUID categoryId = UUID.randomUUID();

        doNothing().when(categoryService).deleteCategory(categoryId);

        mockMvc.perform(delete("/api/v1/categories/{categoryId}", categoryId))
                .andExpect(status().isNoContent());

        verify(categoryService).deleteCategory(categoryId);
    }

    @Test
    @WithMockUser
    @DisplayName("Should return category by id")
    void getCategory_shouldReturnCategory() throws Exception {
        UUID categoryId = UUID.randomUUID();

        CategoryDto response = new CategoryDto(
                categoryId,
                "Hardware",
                "Some description",
                true,
                false,
                false,
                false
        );

        when(categoryService.getCategory(categoryId))
                .thenReturn(response);

        mockMvc.perform(get("/api/v1/categories/{categoryId}", categoryId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(categoryId.toString()))
                .andExpect(jsonPath("$.name").value("Hardware"))
                .andExpect(jsonPath("$.description").value("Some description"))
                .andExpect(jsonPath("$.isIncident").value(true))
                .andExpect(jsonPath("$.isProblem").value(false))
                .andExpect(jsonPath("$.isRequest").value(false))
                .andExpect(jsonPath("$.isChange").value(false));

        verify(categoryService).getCategory(categoryId);
    }

    @Test
    @WithMockUser
    @DisplayName("Should return all incident categories")
    void getAllIncidentCategories_shouldReturnAllIncidentCategories() throws Exception {
        CategoryDto first = new CategoryDto(
                UUID.randomUUID(),
                "Hardware",
                "First description",
                true,
                false,
                false,
                false
        );

        CategoryDto second = new CategoryDto(
                UUID.randomUUID(),
                "Software",
                "Second description",
                true,
                false,
                false,
                false
        );

        when(categoryService.getAllCategories("INCIDENT"))
                .thenReturn(List.of(first, second));

        mockMvc.perform(get("/api/v1/categories?type=INCIDENT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Hardware"))
                .andExpect(jsonPath("$[1].name").value("Software"));

        verify(categoryService).getAllCategories("INCIDENT");
    }

}
