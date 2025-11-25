package org.cescfe.bookpublishing.author.infrastructure.adapters.input.rest

import org.cescfe.bookpublishing.author.application.port.input.GetAuthorUseCase
import org.cescfe.bookpublishing.author.domain.exception.AuthorDomainException
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
class GetAuthorControllerIT {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var getAuthorUseCase: GetAuthorUseCase

    companion object {
        private const val URI = "/api/v1/authors/%s"
        private const val ROLE = "ROLE_USER"
        private const val TEST_AUTHOR_ID = "123e4567-e89b-12d3-a456-426614174000"
    }

    @BeforeEach
    fun setup() {
        val testAuthor =
            Author(
                id = AuthorId(UUID.fromString(TEST_AUTHOR_ID)),
                fullName = FullName("J.R.R. Tolkien"),
                pseudonym = Pseudonym("Tolkien"),
                biography = Biography("English writer and philologist"),
                email = Email("tolkien@example.com"),
                website = Website("https://www.tolkiensociety.org"),
            )
        whenever(getAuthorUseCase.execute(any())).thenReturn(testAuthor)
    }

    @Test
    fun `should get author by id successfully`() {
        mockMvc
            .perform(
                MockMvcRequestBuilders
                    .get(String.format(URI, TEST_AUTHOR_ID))
                    .with(jwt().authorities(SimpleGrantedAuthority(ROLE))),
            ).andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(TEST_AUTHOR_ID))
            .andExpect(MockMvcResultMatchers.jsonPath("$.full_name").value("J.R.R. Tolkien"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.pseudonym").value("Tolkien"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.biography").value("English writer and philologist"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("tolkien@example.com"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.website").value("https://www.tolkiensociety.org"))
    }

    @Test
    fun `should return not found when author does not exist`() {
        whenever(getAuthorUseCase.execute(any())).thenThrow(
            AuthorDomainException.authorNotFound(TEST_AUTHOR_ID),
        )

        mockMvc
            .perform(
                MockMvcRequestBuilders
                    .get(String.format(URI, TEST_AUTHOR_ID))
                    .with(jwt().authorities(SimpleGrantedAuthority(ROLE))),
            ).andExpect(MockMvcResultMatchers.status().isNotFound)
    }
}
