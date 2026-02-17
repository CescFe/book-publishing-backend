package org.cescfe.bookpublishing.auth.infrastructure.adapters.input.security

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component

@Component
class JwtAuthenticationEntryPoint : AuthenticationEntryPoint {
    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException,
    ) {
        response.status = HttpServletResponse.SC_UNAUTHORIZED
        response.contentType = "application/json"
        response.writer.write(
            """
            {
                "status": 401,
                "error": "Unauthorized",
                "message": "Invalid or expired token",
                "code": "UNAUTHORIZED"
            }
            """.trimIndent(),
        )
    }
}
