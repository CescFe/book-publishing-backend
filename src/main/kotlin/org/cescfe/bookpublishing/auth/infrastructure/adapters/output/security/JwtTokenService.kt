package org.cescfe.bookpublishing.auth.infrastructure.adapters.output.security

import org.cescfe.bookpublishing.auth.application.port.output.TokenPayload
import org.cescfe.bookpublishing.auth.application.port.output.TokenService
import org.cescfe.bookpublishing.auth.domain.model.UserId
import org.cescfe.bookpublishing.auth.domain.model.Username
import org.cescfe.bookpublishing.shared.infrastructure.adapters.input.security.JwtUtil
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class JwtTokenService(
    private val jwtUtil: JwtUtil,
) : TokenService {
    override fun issueToken(payload: TokenPayload): String {
        val userDetails =
            User
                .withUsername(payload.username.value)
                .password("")
                .authorities(payload.roles.map { SimpleGrantedAuthority("ROLE_${it.name}") })
                .build()

        return jwtUtil.generateToken(userDetails)
    }

    override fun parseToken(token: String): TokenPayload {
        val username = jwtUtil.getUsernameFromToken(token)
        val expiresAt = jwtUtil.getExpirationInstant(token)
        val userId = UserId(UUID.nameUUIDFromBytes(username.toByteArray()))

        return TokenPayload(
            userId = userId,
            username = Username(username),
            roles = emptySet(),
            permissions = emptySet(),
            expiresAt = expiresAt,
        )
    }

    override fun getExpirationTime(): Long = jwtUtil.getExpirationTime()
}
