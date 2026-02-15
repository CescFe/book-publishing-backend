package org.cescfe.bookpublishing.author.infrastructure.adapters.input.rest

import org.cescfe.bookpublishing.author.application.port.input.ListAllAuthorsUseCase
import org.cescfe.bookpublishing.author.objectMothers.AuthorSummaryObjectMother
import org.cescfe.bookpublishing.shared.domain.model.NonPaginatedResult
import org.cescfe.bookpublishing.shared.domain.model.NonPaginationMeta
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
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
class ListAllAuthorsControllerIT {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var listAllAuthorsUseCase: ListAllAuthorsUseCase

    companion object {
        private const val URI = "/api/v1/authors/all"
        private const val ROLE = "ROLE_USER"
        private const val AUTHOR_ID = "477537ff-7e8b-4930-bd41-d7f3589120b1"
    }

    @BeforeEach
    fun setup() {
        val testAuthorSummary = AuthorSummaryObjectMother.createFirstAuthorSummary()
        val result =
            NonPaginatedResult(
                data = listOf(testAuthorSummary),
                metadata =
                    NonPaginationMeta(
                        total = 1L,
                    ),
            )

        whenever(listAllAuthorsUseCase.execute()).thenReturn(result)
    }

    @Test
    fun `should list all authors successfully`() {
        mockMvc
            .perform(
                MockMvcRequestBuilders
                    .get(URI)
                    .with(jwt().authorities(SimpleGrantedAuthority(ROLE))),
            ).andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.data").isArray)
            .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id").value(AUTHOR_ID))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].full_name").value("J.R.R. Tolkien"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].pseudonym").value("Tolkien"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].email").value("tolkien@example.com"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.meta.total").value(1))
    }
}
