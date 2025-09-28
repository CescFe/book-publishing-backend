package org.cescfe.bookpublishing.author.infrastructure.adapters.input

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(ListAuthorsController::class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ListAuthorsControllerIT {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `should return 501 Not Implemented when calling getAuthors endpoint`() {
        mockMvc
            .perform(
                get("/api/v1/authors")
                    .param("page", "1")
                    .param("limit", "10"),
            ).andExpect(status().isNotImplemented)
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.data").isArray)
            .andExpect(jsonPath("$.data").isEmpty)
            .andExpect(jsonPath("$.meta.total").value(0))
            .andExpect(jsonPath("$.meta.page").value(1))
            .andExpect(jsonPath("$.meta.limit").value(10))
            .andExpect(jsonPath("$.meta.totalPages").value(0))
    }

    @Test
    fun `should return 501 Not Implemented with search parameter`() {
        mockMvc
            .perform(
                get("/api/v1/authors")
                    .param("page", "1")
                    .param("limit", "10")
                    .param("search", "test"),
            ).andExpect(status().isNotImplemented)
            .andExpect(jsonPath("$.data").isArray)
            .andExpect(jsonPath("$.data").isEmpty)
            .andExpect(jsonPath("$.meta.total").value(0))
            .andExpect(jsonPath("$.meta.page").value(1))
            .andExpect(jsonPath("$.meta.limit").value(10))
    }

    @Test
    fun `should return 501 Not Implemented with different pagination parameters`() {
        mockMvc
            .perform(
                get("/api/v1/authors")
                    .param("page", "2")
                    .param("limit", "20"),
            ).andExpect(status().isNotImplemented)
            .andExpect(jsonPath("$.data").isArray)
            .andExpect(jsonPath("$.data").isEmpty)
            .andExpect(jsonPath("$.meta.total").value(0))
            .andExpect(jsonPath("$.meta.page").value(2))
            .andExpect(jsonPath("$.meta.limit").value(20))
    }
}
