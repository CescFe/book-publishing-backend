package org.cescfe.bookpublishing.book.infrastructure.adapters.input.rest

import org.cescfe.bookpublishing.book.application.port.input.GetBookUseCase
import org.cescfe.bookpublishing.book.objectMothers.BookObjectMother
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
class GetBookControllerIT {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var getBookUseCase: GetBookUseCase

    companion object {
        private const val URI = "/api/v1/books/%s"
        private const val ROLE = "ROLE_USER"
        private const val TEST_BOOK_ID = "223e4567-e89b-12d3-a456-426614174000"
    }

    @BeforeEach
    fun setup() {
        val testBook = BookObjectMother.createForControllerIT()
        whenever(getBookUseCase.execute(any())).thenReturn(testBook)
    }

    @Test
    fun `should get book by ID successfully`() {
        mockMvc
            .perform(
                MockMvcRequestBuilders
                    .get(String.format(URI, TEST_BOOK_ID))
                    .with(jwt().authorities(SimpleGrantedAuthority(ROLE))),
            ).andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(TEST_BOOK_ID))
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("The Lord of the Rings"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.author_id").value("223e4567-e89b-12d3-a456-426614174001"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.collection_id").value("223e4567-e89b-12d3-a456-426614174002"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.reading_level").value("ADULT"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.primary_language").value("ENGLISH"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.secondary_languages[0]").value("CATALAN"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.secondary_languages[1]").value("SPANISH"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.primary_genre").value("FANTASY"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.secondary_genres[0]").value("ADVENTURE"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.secondary_genres[1]").value("HISTORICAL_FICTION"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.base_price").value(29.99))
            .andExpect(MockMvcResultMatchers.jsonPath("$.vat_rate").value(0.04))
            .andExpect(MockMvcResultMatchers.jsonPath("$.final_price").value(31.19))
            .andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value("9780007141326"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.publication_date").value("1954-07-29"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.page_count").value(1178))
            .andExpect(MockMvcResultMatchers.jsonPath("$.cover_image_path").value("/images/lotr-cover.jpg"))
            .andExpect(
                MockMvcResultMatchers
                    .jsonPath(
                        "$.description",
                    ).value("An epic high fantasy novel written by J.R.R. Tolkien."),
            )
    }

    @Test
    fun `should return not found when book does not exist`() {
        whenever(getBookUseCase.execute(any())).thenThrow(
            org.cescfe.bookpublishing.book.domain.exception.BookDomainException
                .bookNotFound(TEST_BOOK_ID),
        )

        mockMvc
            .perform(
                MockMvcRequestBuilders
                    .get(String.format(URI, TEST_BOOK_ID))
                    .with(jwt().authorities(SimpleGrantedAuthority(ROLE))),
            ).andExpect(MockMvcResultMatchers.status().isNotFound)
    }
}
