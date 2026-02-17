package org.cescfe.bookpublishing.book.infrastructure.adapters.input.rest

import org.cescfe.bookpublishing.book.application.port.input.ListAllBooksUseCase
import org.cescfe.bookpublishing.book.objectMothers.BookSummaryObjectMother
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
class ListAllBooksControllerIT {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var listAllBooksUseCase: ListAllBooksUseCase

    companion object {
        private const val URI = "/api/v1/books/all"
        private const val ROLE = "ROLE_USER"
        private const val BOOK_ID = "477537ff-7e8b-4930-bd41-d7f3589120b1"
    }

    @BeforeEach
    fun setup() {
        val testBookSummary = BookSummaryObjectMother.createFirstBookSummary()
        val result =
            NonPaginatedResult(
                data = listOf(testBookSummary),
                metadata =
                    NonPaginationMeta(
                        total = 1L,
                    ),
            )

        whenever(listAllBooksUseCase.execute()).thenReturn(result)
    }

    @Test
    fun `should list all books successfully`() {
        mockMvc
            .perform(
                MockMvcRequestBuilders
                    .get(URI)
                    .with(jwt().authorities(SimpleGrantedAuthority(ROLE))),
            ).andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.data").isArray)
            .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id").value(BOOK_ID))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].title").value("The Hobbit"))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.data[0].author.id").value("123e4567-e89b-12d3-a456-426614174000"),
            ).andExpect(MockMvcResultMatchers.jsonPath("$.data[0].author.name").value("J.R.R. Tolkien"))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.data[0].collection.id").value("223e4567-e89b-12d3-a456-426614174000"),
            ).andExpect(MockMvcResultMatchers.jsonPath("$.data[0].collection.name").value("The Lord of the Rings"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].base_price").value(25.50))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].final_price").value(26.52))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].isbn").value("9783161484100"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].status").value("PUBLISHED"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.meta.total").value(1))
    }
}
