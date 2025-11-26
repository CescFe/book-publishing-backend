package org.cescfe.bookpublishing.auth.infrastructure.adapters.input.rest

import org.cescfe.bookpublishing.auth.application.port.input.LoginUseCase
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
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
class LoginControllerIT {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var loginUseCase: LoginUseCase

    companion object {
        private const val URI = "/api/v1/auth/login"
    }

    @Test
    fun `should login successfully`() {
        val requestBody =
            """
            {
                "username": "user",
                "password": "password"
            }
            """.trimIndent()

        val output =
            LoginUseCase.OutputValues(
                accessToken = "token",
                expiresIn = 3600,
                scope = "read",
                userId = "user-id",
            )

        whenever(loginUseCase.execute(any())).thenReturn(output)

        mockMvc
            .perform(
                MockMvcRequestBuilders
                    .post(URI)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody),
            ).andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.access_token").value("token"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.token_type").value("Bearer"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.expires_in").value(3600))
            .andExpect(MockMvcResultMatchers.jsonPath("$.scope").value("read"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.user_id").value("user-id"))
    }
}
