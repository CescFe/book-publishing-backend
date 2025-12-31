package org.cescfe.bookpublishing.book.infrastructure.adapters.input.rest

import org.cescfe.bookpublishing.book.application.port.input.UpdateBookUseCase
import org.cescfe.bookpublishing.book.objectMothers.BookObjectMother
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
class UpdateBookControllerIT {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var updateBookUseCase: UpdateBookUseCase

    companion object {
        private const val URI = "/api/v1/books/%s"
        private const val ROLE = "ROLE_ADMIN"
        private const val TEST_BOOK_ID = "223e4567-e89b-12d3-a456-426614174000"
    }

    @BeforeEach
    fun setup() {
        val testBook = BookObjectMother.createForControllerIT()
        whenever(updateBookUseCase.execute(any(), any())).thenReturn(testBook)
    }

    @Test
    fun `should update book successfully`() {
        val requestBody =
            """
            {
                "title": "Updated The Lord of the Rings",
                "author_id": "223e4567-e89b-12d3-a456-426614174001",
                "collection_id": "223e4567-e89b-12d3-a456-426614174002",
                "reading_level": "ADULT",
                "primary_language": "ENGLISH",
                "secondary_languages": ["CATALAN", "SPANISH"],
                "primary_genre": "FANTASY",
                "secondary_genres": ["ADVENTURE"],
                "base_price": 35.99,
                "vat_rate": 0.04,
                "isbn": "9780007141326",
                "publication_date": "1954-07-29",
                "page_count": 1178,
                "cover_image_path": "/images/lotr-cover.jpg",
                "description": "An updated epic high fantasy novel."
            }
            """.trimIndent()

        mockMvc
            .perform(
                MockMvcRequestBuilders
                    .put(String.format(URI, TEST_BOOK_ID))
                    .with(jwt().authorities(SimpleGrantedAuthority(ROLE)))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody),
            ).andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(TEST_BOOK_ID))
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("The Lord of the Rings"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.author.id").value("223e4567-e89b-12d3-a456-426614174001"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.author.name").value("J.R.R. Tolkien"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.collection.id").value("223e4567-e89b-12d3-a456-426614174002"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.collection.name").value("The Lord of the Rings"))
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
            ).andExpect(MockMvcResultMatchers.jsonPath("$.status").value("PUBLISHED"))
    }
}
