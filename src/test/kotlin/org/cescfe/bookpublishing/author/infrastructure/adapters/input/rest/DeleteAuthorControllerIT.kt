package org.cescfe.bookpublishing.author.infrastructure.adapters.input.rest

import org.cescfe.bookpublishing.author.application.port.input.DeleteAuthorUseCase
import org.cescfe.bookpublishing.author.domain.exception.AuthorDomainException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doAnswer
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
class DeleteAuthorControllerIT {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var deleteAuthorUseCase: DeleteAuthorUseCase

    companion object {
        const val TEST_AUTHOR_ID = "123e4567-e89b-12d3-a456-426614174000"
    }

    @BeforeEach
    fun setup() {
        doAnswer { }.whenever(deleteAuthorUseCase).execute(any())
    }

    @Test
    fun `should delete author successfully`() {
        mockMvc
            .perform(
                MockMvcRequestBuilders
                    .delete("/api/v1/authors/{id}", TEST_AUTHOR_ID)
                    .with(jwt().authorities(SimpleGrantedAuthority("ROLE_ADMIN"))),
            ).andExpect(MockMvcResultMatchers.status().isNoContent)
    }

    @Test
    fun `should return not found when deleting non-existent author`() {
        whenever(deleteAuthorUseCase.execute(any())).thenThrow(
            AuthorDomainException.authorNotFound(TEST_AUTHOR_ID),
        )

        mockMvc
            .perform(
                MockMvcRequestBuilders
                    .delete("/api/v1/authors/{id}", TEST_AUTHOR_ID)
                    .with(jwt().authorities(SimpleGrantedAuthority("ROLE_ADMIN"))),
            ).andExpect(MockMvcResultMatchers.status().isNotFound)
    }
}
