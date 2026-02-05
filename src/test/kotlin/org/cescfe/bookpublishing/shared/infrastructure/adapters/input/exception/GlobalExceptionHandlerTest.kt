package org.cescfe.bookpublishing.shared.infrastructure.adapters.input.exception

import org.cescfe.bookpublishing.auth.domain.exception.AuthDomainException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.web.context.request.ServletWebRequest

class GlobalExceptionHandlerTest {
    private val handler = GlobalExceptionHandler()

    // AuthDomainExceptions
    @Test
    fun `should map invalid credentials to unauthorized`() {
        val request = MockHttpServletRequest("POST", "/api/v1/auth/login")
        val webRequest = ServletWebRequest(request)
        val exception = AuthDomainException.invalidCredentials()

        val response = handler.handleAuthDomainException(exception, webRequest)

        assertEquals(HttpStatus.UNAUTHORIZED, response.statusCode)
        val body = response.body
        assertNotNull(body)
        assertEquals("INVALID_CREDENTIALS", body?.code)
    }

    @Test
    fun `should map other auth domain errors to bad request`() {
        val request = MockHttpServletRequest("POST", "/api/v1/auth/login")
        val webRequest = ServletWebRequest(request)
        val exception = AuthDomainException.usernameCannotBeBlank()

        val response = handler.handleAuthDomainException(exception, webRequest)

        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        val body = response.body
        assertNotNull(body)
        assertEquals("USERNAME_CANNOT_BE_BLANK", body?.code)
    }
}
