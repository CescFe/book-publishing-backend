package org.cescfe.bookpublishing.auth.infrastructure.adapters.input.config

import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
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
        private const val BOOKS_URI = "/api/v1/books"
        private const val COLLECTIONS_URI = "/api/v1/collections"
        private const val ANY_ID = "00000000-0000-0000-0000-000000000000"

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

    @ParameterizedTest
    @ValueSource(strings = [AUTHORS_URI, BOOKS_URI, COLLECTIONS_URI])
    fun `manager should access POST resources`(uri: String) {
        mockMvc
            .perform(
                MockMvcRequestBuilders
                    .post(uri)
                    .with(SecurityMockMvcRequestPostProcessors.user("manager").roles("MANAGER"))
                    .contentType("application/json")
                    .content("{}"),
            ).andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @ParameterizedTest
    @ValueSource(strings = [AUTHORS_URI, BOOKS_URI, COLLECTIONS_URI])
    fun `manager should access PUT resources`(uri: String) {
        mockMvc
            .perform(
                MockMvcRequestBuilders
                    .put("$uri/$ANY_ID")
                    .with(SecurityMockMvcRequestPostProcessors.user("manager").roles("MANAGER"))
                    .contentType("application/json")
                    .content("{}"),
            ).andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @ParameterizedTest
    @ValueSource(strings = [AUTHORS_URI, BOOKS_URI, COLLECTIONS_URI])
    fun `manager should not access DELETE resources`(uri: String) {
        mockMvc
            .perform(
                MockMvcRequestBuilders
                    .delete("$uri/$ANY_ID")
                    .with(SecurityMockMvcRequestPostProcessors.user("manager").roles("MANAGER")),
            ).andExpect(MockMvcResultMatchers.status().isForbidden)
    }
}
