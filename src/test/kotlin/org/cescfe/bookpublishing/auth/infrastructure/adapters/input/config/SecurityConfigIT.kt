package org.cescfe.bookpublishing.auth.infrastructure.adapters.input.config

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("test")
class SecurityConfigIT {
    @Autowired
    private lateinit var mockMvc: MockMvc

    companion object {
        private const val AUTHORS_URI = "/api/v1/authors"

        @Container
        @ServiceConnection
        @JvmStatic
        val postgres: PostgreSQLContainer<*> =
            PostgreSQLContainer(DockerImageName.parse("postgres:16"))
                .withDatabaseName("testdb")
                .withUsername("test")
                .withPassword("test")
    }

    @Test
    fun `admin should access POST authors`() {
        mockMvc
            .perform(
                MockMvcRequestBuilders
                    .post(AUTHORS_URI)
                    .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN"))
                    .contentType("application/json")
                    .content("{}"),
            ).andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun `user should not access POST authors`() {
        mockMvc
            .perform(
                MockMvcRequestBuilders
                    .post(AUTHORS_URI)
                    .with(SecurityMockMvcRequestPostProcessors.user("user").roles("USER"))
                    .contentType("application/json")
                    .content("{}"),
            ).andExpect(MockMvcResultMatchers.status().isForbidden)
    }

    @Test
    fun `admin should access GET authors`() {
        mockMvc
            .perform(
                MockMvcRequestBuilders
                    .get(AUTHORS_URI)
                    .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN")),
            ).andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun `user should access GET authors`() {
        mockMvc
            .perform(
                MockMvcRequestBuilders
                    .get(AUTHORS_URI)
                    .with(SecurityMockMvcRequestPostProcessors.user("user").roles("USER")),
            ).andExpect(MockMvcResultMatchers.status().isOk)
    }
}
