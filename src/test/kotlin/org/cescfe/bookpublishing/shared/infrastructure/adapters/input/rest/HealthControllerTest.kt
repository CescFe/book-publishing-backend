package org.cescfe.bookpublishing.shared.infrastructure.adapters.input.rest

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest(HealthController::class)
class HealthControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `health endpoint should return 200 OK`() {
        mockMvc
            .perform(MockMvcRequestBuilders.get("/api/v1/health"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("OK"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Hello World"))
    }
}
