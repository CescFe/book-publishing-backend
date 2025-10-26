package org.cescfe.bookpublishing.author.infrastructure.adapters.input.rest

import org.cescfe.bookpublishing.author.application.port.input.CreateAuthorUseCase
import org.cescfe.bookpublishing.author.objectMothers.AuthorObjectMother
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(
    properties = [
        "spring.autoconfigure.exclude=" +
            "org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration," +
            "org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration," +
            "org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration",
    ],
)
class CreateAuthorControllerIT {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var createAuthorUseCase: CreateAuthorUseCase

    companion object {
        const val TEST_AUTHOR_ID = "123e4567-e89b-12d3-a456-426614174000"
    }

    @BeforeEach
    fun setup() {
        val testAuthor = AuthorObjectMother.createWithMultipleRoles()
        whenever(createAuthorUseCase.execute(any())).thenReturn(testAuthor)
    }

    @Test
    fun `should create author successfully`() {
        val requestBody =
            """
            {
                "full_name": "J.R.R. Tolkien",
                "roles": ["AUTHOR", "ILLUSTRATOR"],
                "pseudonym": "Tolkien",
                "biography": "English writer and philologist",
                "email": "tolkien@example.com",
                "website": "https://www.tolkiensociety.org"
            }
            """.trimIndent()

        mockMvc
            .perform(
                MockMvcRequestBuilders
                    .post("/api/v1/authors")
                    .with(jwt().authorities(SimpleGrantedAuthority("ROLE_ADMIN")))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody),
            ).andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(TEST_AUTHOR_ID))
            .andExpect(MockMvcResultMatchers.jsonPath("$.full_name").value("J.R.R. Tolkien"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.roles").isArray)
            .andExpect(MockMvcResultMatchers.jsonPath("$.roles[0]").value("AUTHOR"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.roles[1]").value("ILLUSTRATOR"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.pseudonym").value("Tolkien"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.biography").value("English writer and philologist"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("tolkien@example.com"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.website").value("https://www.tolkiensociety.org"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.created_at").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.updated_at").exists())
    }
}
