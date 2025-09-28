package org.cescfe.bookpublishing.author.infrastructure.adapters.input.rest

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest(ListAuthorsController::class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ListAuthorsControllerIT {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `should return 501 Not Implemented when calling getAuthors endpoint`() {
        mockMvc
            .perform(
                MockMvcRequestBuilders
                    .get("/api/v1/authors")
                    .param("page", "1")
                    .param("limit", "10"),
            ).andExpect(MockMvcResultMatchers.status().isNotImplemented)
            .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data").isArray)
            .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty)
            .andExpect(MockMvcResultMatchers.jsonPath("$.meta.total").value(0))
            .andExpect(MockMvcResultMatchers.jsonPath("$.meta.page").value(1))
            .andExpect(MockMvcResultMatchers.jsonPath("$.meta.limit").value(10))
            .andExpect(MockMvcResultMatchers.jsonPath("$.meta.total_pages").value(0))
    }

    @Test
    fun `should return 501 Not Implemented with search parameter`() {
        mockMvc
            .perform(
                MockMvcRequestBuilders
                    .get("/api/v1/authors")
                    .param("page", "1")
                    .param("limit", "10")
                    .param("search", "test"),
            ).andExpect(MockMvcResultMatchers.status().isNotImplemented)
            .andExpect(MockMvcResultMatchers.jsonPath("$.data").isArray)
            .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty)
            .andExpect(MockMvcResultMatchers.jsonPath("$.meta.total").value(0))
            .andExpect(MockMvcResultMatchers.jsonPath("$.meta.page").value(1))
            .andExpect(MockMvcResultMatchers.jsonPath("$.meta.limit").value(10))
            .andExpect(MockMvcResultMatchers.jsonPath("$.meta.total_pages").value(0))
    }

    @Test
    fun `should return 501 Not Implemented with different pagination parameters`() {
        mockMvc
            .perform(
                MockMvcRequestBuilders
                    .get("/api/v1/authors")
                    .param("page", "2")
                    .param("limit", "20"),
            ).andExpect(MockMvcResultMatchers.status().isNotImplemented)
            .andExpect(MockMvcResultMatchers.jsonPath("$.data").isArray)
            .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty)
            .andExpect(MockMvcResultMatchers.jsonPath("$.meta.total").value(0))
            .andExpect(MockMvcResultMatchers.jsonPath("$.meta.page").value(2))
            .andExpect(MockMvcResultMatchers.jsonPath("$.meta.limit").value(20))
            .andExpect(MockMvcResultMatchers.jsonPath("$.meta.total_pages").value(0))
    }
}
