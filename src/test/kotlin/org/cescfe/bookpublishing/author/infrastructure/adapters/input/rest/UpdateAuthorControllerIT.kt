package org.cescfe.bookpublishing.author.infrastructure.adapters.input.rest

import org.cescfe.bookpublishing.author.application.port.input.UpdateAuthorUseCase
import org.cescfe.bookpublishing.author.domain.model.Author
import org.cescfe.bookpublishing.author.domain.model.AuthorId
import org.cescfe.bookpublishing.author.domain.model.Biography
import org.cescfe.bookpublishing.author.domain.model.Email
import org.cescfe.bookpublishing.author.domain.model.FullName
import org.cescfe.bookpublishing.author.domain.model.Pseudonym
import org.cescfe.bookpublishing.author.domain.model.Website
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
import java.util.UUID

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
class UpdateAuthorControllerIT {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var updateAuthorUseCase: UpdateAuthorUseCase

    companion object {
        const val TEST_AUTHOR_ID = "123e4567-e89b-12d3-a456-426614174000"
    }

    @BeforeEach
    fun setup() {
        val updatedAuthor =
            Author(
                id = AuthorId(UUID.fromString(TEST_AUTHOR_ID)),
                fullName = FullName("Updated J.R.R. Tolkien"),
                pseudonym = Pseudonym("Updated Tolkien"),
                biography = Biography("Updated English writer and philologist"),
                email = Email("updated.tolkien@example.com"),
                website = Website("https://www.updated-tolkiensociety.org"),
            )
        whenever(updateAuthorUseCase.execute(any(), any())).thenReturn(updatedAuthor)
    }

    @Test
    fun `should update author successfully`() {
        val requestBody =
            """
            {
                "full_name": "Updated J.R.R. Tolkien",
                "pseudonym": "Updated Tolkien",
                "biography": "Updated English writer and philologist",
                "email": "updated.tolkien@example.com",
                "website": "https://www.updated-tolkiensociety.org"
            }
            """.trimIndent()

        mockMvc
            .perform(
                MockMvcRequestBuilders
                    .put("/api/v1/authors/{id}", TEST_AUTHOR_ID)
                    .with(jwt().authorities(SimpleGrantedAuthority("ROLE_ADMIN")))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody),
            ).andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(TEST_AUTHOR_ID))
            .andExpect(MockMvcResultMatchers.jsonPath("$.full_name").value("Updated J.R.R. Tolkien"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.pseudonym").value("Updated Tolkien"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.biography").value("Updated English writer and philologist"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("updated.tolkien@example.com"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.website").value("https://www.updated-tolkiensociety.org"))
    }
}
