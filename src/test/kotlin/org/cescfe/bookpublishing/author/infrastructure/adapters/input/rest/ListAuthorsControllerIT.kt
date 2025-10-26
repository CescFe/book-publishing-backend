package org.cescfe.bookpublishing.author.infrastructure.adapters.input.rest

import org.cescfe.bookpublishing.author.application.port.input.ListAuthorsUseCase
import org.cescfe.bookpublishing.author.domain.model.AuthorId
import org.cescfe.bookpublishing.author.domain.model.AuthorRole
import org.cescfe.bookpublishing.author.domain.model.AuthorSummary
import org.cescfe.bookpublishing.author.domain.model.Email
import org.cescfe.bookpublishing.author.domain.model.FullName
import org.cescfe.bookpublishing.author.domain.model.PaginatedResult
import org.cescfe.bookpublishing.author.domain.model.PaginationMeta
import org.cescfe.bookpublishing.author.domain.model.Pseudonym
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
class ListAuthorsControllerIT {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var listAuthorsUseCase: ListAuthorsUseCase

    companion object {
        const val TEST_AUTHOR_ID = "123e4567-e89b-12d3-a456-426614174000"
    }

    @BeforeEach
    fun setup() {
        val testAuthorSummary =
            AuthorSummary(
                id = AuthorId(UUID.fromString(TEST_AUTHOR_ID)),
                fullName = FullName("J.R.R. Tolkien"),
                roles = setOf(AuthorRole.AUTHOR, AuthorRole.ILLUSTRATOR),
                pseudonym = Pseudonym("Tolkien"),
                email = Email("tolkien@example.com"),
            )

        val paginatedResult =
            PaginatedResult(
                data = listOf(testAuthorSummary),
                meta =
                    PaginationMeta(
                        total = 1L,
                        page = 1,
                        limit = 20,
                        totalPages = 1,
                    ),
            )
        whenever(listAuthorsUseCase.execute(any())).thenReturn(paginatedResult)
    }

    @Test
    fun `should list authors successfully`() {
        mockMvc
            .perform(
                MockMvcRequestBuilders
                    .get("/api/v1/authors")
                    .with(jwt().authorities(SimpleGrantedAuthority("ROLE_ADMIN")))
                    .param("page", "1")
                    .param("limit", "20"),
            ).andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.data").isArray)
            .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id").value(TEST_AUTHOR_ID))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].full_name").value("J.R.R. Tolkien"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].roles").isArray)
            .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].roles[0]").value("AUTHOR"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].roles[1]").value("ILLUSTRATOR"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.meta.total").value(1))
            .andExpect(MockMvcResultMatchers.jsonPath("$.meta.page").value(1))
            .andExpect(MockMvcResultMatchers.jsonPath("$.meta.limit").value(20))
            .andExpect(MockMvcResultMatchers.jsonPath("$.meta.total_pages").value(1))
    }

    @Test
    fun `should return empty list when no authors exist`() {
        val emptyResult =
            PaginatedResult(
                data = listOf<AuthorSummary>(),
                meta =
                    PaginationMeta(
                        total = 0L,
                        page = 1,
                        limit = 20,
                        totalPages = 0,
                    ),
            )
        whenever(listAuthorsUseCase.execute(any())).thenReturn(emptyResult)

        mockMvc
            .perform(
                MockMvcRequestBuilders
                    .get("/api/v1/authors")
                    .with(jwt().authorities(SimpleGrantedAuthority("ROLE_ADMIN"))),
            ).andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.data").isArray)
            .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty)
            .andExpect(MockMvcResultMatchers.jsonPath("$.meta.total").value(0))
    }
}
