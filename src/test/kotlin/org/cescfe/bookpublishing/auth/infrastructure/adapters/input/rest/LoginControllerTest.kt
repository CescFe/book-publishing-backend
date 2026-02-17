package org.cescfe.bookpublishing.auth.infrastructure.adapters.input.rest

import org.cescfe.bookpublishing.auth.application.port.input.LoginUseCase
import org.cescfe.bookpublishing.ms.catalog.infrastructure.openapi.http.inbound.model.Login200ResponseDTO
import org.cescfe.bookpublishing.ms.catalog.infrastructure.openapi.http.inbound.model.LoginRequestDTO
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.springframework.http.HttpStatus

class LoginControllerTest {
    private lateinit var loginUseCase: LoginUseCase
    private lateinit var loginController: LoginController

    @BeforeEach
    fun setup() {
        loginUseCase = mock()
        loginController = LoginController(loginUseCase)
    }

    @Test
    fun `should return 200 and token when login is successful`() {
        // Given
        val requestDTO =
            LoginRequestDTO(
                username = "user",
                password = "password",
            )
        val expectedOutput =
            LoginUseCase.OutputValues(
                accessToken = "token",
                expiresIn = 3600,
                scope = "read",
                userId = "user-id",
            )

        whenever(loginUseCase.execute(any())).thenReturn(expectedOutput)

        // When
        val response = loginController.login(requestDTO)

        // Then
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals("token", response.body?.accessToken)
        assertEquals(Login200ResponseDTO.TokenType.BEARER, response.body?.tokenType)
        assertEquals(3600, response.body?.expiresIn)
        assertEquals("read", response.body?.scope)
        assertEquals("user-id", response.body?.userId)
    }
}
