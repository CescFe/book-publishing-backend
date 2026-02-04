package org.cescfe.bookpublishing.auth.infrastructure.adapters.output.security

import org.cescfe.bookpublishing.auth.application.port.output.TokenPayload
import org.cescfe.bookpublishing.auth.domain.model.UserId
import org.cescfe.bookpublishing.auth.domain.model.Username
import org.cescfe.bookpublishing.auth.domain.model.enum.Permission
import org.cescfe.bookpublishing.auth.domain.model.enum.Role
import org.cescfe.bookpublishing.shared.infrastructure.adapters.input.security.JwtUtil
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.time.Instant
import java.util.UUID

class JwtTokenServiceTest {
    @Test
    fun `should issue token using jwt util`() {
        val jwtUtil = mock<JwtUtil>()
        whenever(jwtUtil.generateToken(org.mockito.kotlin.any())).thenReturn("token")
        val tokenService = JwtTokenService(jwtUtil)

        val payload =
            TokenPayload(
                userId = UserId(UUID.randomUUID()),
                username = Username("user@example.com"),
                roles = setOf(Role.ADMIN),
                permissions = emptySet(),
                expiresAt = Instant.now(),
            )

        val token = tokenService.issueToken(payload)

        assertEquals("token", token)

        val userDetailsCaptor = argumentCaptor<org.springframework.security.core.userdetails.UserDetails>()
        org.mockito.kotlin
            .verify(jwtUtil)
            .generateToken(userDetailsCaptor.capture())
        val userDetails = userDetailsCaptor.firstValue

        assertEquals("user@example.com", userDetails.username)
        assertTrue(userDetails.authorities.any { it.authority == "ROLE_ADMIN" })
    }

    @Test
    fun `should parse token using jwt util`() {
        val jwtUtil = mock<JwtUtil>()
        whenever(jwtUtil.getUsernameFromToken("token")).thenReturn("user@example.com")
        val expiration = Instant.parse("2030-01-01T00:00:00Z")
        whenever(jwtUtil.getExpirationInstant("token")).thenReturn(expiration)
        val tokenService = JwtTokenService(jwtUtil)

        val payload = tokenService.parseToken("token")

        assertEquals(Username("user@example.com"), payload.username)
        assertEquals(expiration, payload.expiresAt)
        assertEquals(emptySet<Role>(), payload.roles)
        assertEquals(emptySet<Permission>(), payload.permissions)

        val expectedUserId = UserId(UUID.nameUUIDFromBytes("user@example.com".toByteArray()))
        assertEquals(expectedUserId, payload.userId)
    }
}
