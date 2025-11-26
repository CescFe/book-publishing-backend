package org.cescfe.bookpublishing.book.infrastructure.adapters.input.rest

import org.cescfe.bookpublishing.author.domain.model.PaginatedResult
import org.cescfe.bookpublishing.author.domain.model.PaginationMeta
import org.cescfe.bookpublishing.book.application.port.input.ListBooksUseCase
import org.cescfe.bookpublishing.book.objectMothers.BookSummaryObjectMother
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
class ListBooksControllerIT {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var listBooksUseCase: ListBooksUseCase

    companion object {
        private const val URI = "/api/v1/books"
        private const val ROLE = "ROLE_USER"
        private const val BOOK_ID = "477537ff-7e8b-4930-bd41-d7f3589120b1"
    }

    @BeforeEach
    fun setup() {
        val testBookSummary = BookSummaryObjectMother.createFirstBookSummary()

        val paginatedResult =
            PaginatedResult(
                data = listOf(testBookSummary),
                metadata =
                    PaginationMeta(
                        total = 1L,
                        page = 1,
                        limit = 20,
                        totalPages = 1,
                    ),
            )

        whenever(listBooksUseCase.execute(any())).thenReturn(paginatedResult)
    }

    @Test
    fun `should list books successfully`() {
        mockMvc
            .perform(
                MockMvcRequestBuilders
                    .get(URI)
                    .with(jwt().authorities(SimpleGrantedAuthority(ROLE)))
                    .param("page", "1")
                    .param("limit", "20"),
            ).andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.data").isArray)
            .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id").value(BOOK_ID))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].title").value("The Hobbit"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.meta.total").value(1))
            .andExpect(MockMvcResultMatchers.jsonPath("$.meta.page").value(1))
            .andExpect(MockMvcResultMatchers.jsonPath("$.meta.limit").value(20))
            .andExpect(MockMvcResultMatchers.jsonPath("$.meta.total_pages").value(1))
    }
}
