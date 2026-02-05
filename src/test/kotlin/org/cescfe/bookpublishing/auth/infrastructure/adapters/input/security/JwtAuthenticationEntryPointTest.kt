package org.cescfe.bookpublishing.auth.infrastructure.adapters.input.security

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.security.authentication.BadCredentialsException

class JwtAuthenticationEntryPointTest {
    private lateinit var entryPoint: JwtAuthenticationEntryPoint
    private lateinit var request: MockHttpServletRequest
    private lateinit var response: MockHttpServletResponse

    @BeforeEach
    fun setup() {
        entryPoint = JwtAuthenticationEntryPoint()
        request = MockHttpServletRequest()
        response = MockHttpServletResponse()
    }

    @Test
    fun `should return 401 status`() {
        // Given
        val authException = BadCredentialsException("Invalid credentials")

        // When
        entryPoint.commence(request, response, authException)

        // Then
        assertEquals(401, response.status)
        assertEquals("application/json", response.contentType)
        val responseBody = response.contentAsString
        assertTrue(responseBody.contains("\"status\": 401"))
        assertTrue(responseBody.contains("\"error\": \"Unauthorized\""))
        assertTrue(responseBody.contains("\"message\": \"Invalid or expired token\""))
        assertTrue(responseBody.contains("\"code\": \"UNAUTHORIZED\""))
    }
}
