package org.cescfe.bookpublishing.auth.application.port.output

import org.cescfe.bookpublishing.auth.domain.model.UserId
import org.cescfe.bookpublishing.auth.domain.model.Username
import org.cescfe.bookpublishing.auth.domain.model.enum.Permission
import org.cescfe.bookpublishing.auth.domain.model.enum.Role
import java.time.Instant

data class TokenPayload(
    val userId: UserId,
    val username: Username,
    val roles: Set<Role>,
    val permissions: Set<Permission>,
    val expiresAt: Instant,
)

interface TokenService {
    fun issueToken(payload: TokenPayload): String

    fun parseToken(token: String): TokenPayload
}
