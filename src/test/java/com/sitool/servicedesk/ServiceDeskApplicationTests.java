package com.sitool.servicedesk;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
class ServiceDeskApplicationTests {

    @Test
    void contextLoads() {
    }

    // For future usage
    //    @Test
//    @DisplayName("Should return 403 when non-admin tries to reset password")
//    @WithMockUser(username = "vasiliy@domain.com", roles = "USER")
//    void resetPassword_shouldReturn403_whenNotAdmin() throws Exception {
//        UUID userId = UUID.randomUUID();
//
//        mockMvc.perform(post("/api/v1/users/" + userId + "/reset-password")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("""
//                            {
//                              "newPassword": "NewPassword123!"
//                            }
//                            """))
//                .andExpect(status().isForbidden());
//
//        verify(userService, never()).resetPassword(any(), any());
//    }

}
