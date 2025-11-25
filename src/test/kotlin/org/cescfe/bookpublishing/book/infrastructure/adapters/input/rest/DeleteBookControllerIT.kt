package org.cescfe.bookpublishing.book.infrastructure.adapters.input.rest

import org.cescfe.bookpublishing.book.application.port.input.DeleteBookUseCase
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
class DeleteBookControllerIT {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var deleteBookUseCase: DeleteBookUseCase

    companion object {
        private const val URI = "/api/v1/books/%s"
        private const val ROLE = "ROLE_ADMIN"
        private const val TEST_BOOK_ID = "123e4567-e89b-12d3-a456-426614174000"
    }

    @BeforeEach
    fun setup() {
        doAnswer { }.whenever(deleteBookUseCase).execute(any())
    }

    @Test
    fun `should delete book successfully`() {
        mockMvc
            .perform(
                MockMvcRequestBuilders
                    .delete(String.format(URI, TEST_BOOK_ID))
                    .with(jwt().authorities(SimpleGrantedAuthority(ROLE))),
            ).andExpect(MockMvcResultMatchers.status().isNoContent)
    }
}
