package org.cescfe.bookpublishing.auth.infrastructure.adapters.output.security

import org.cescfe.bookpublishing.auth.application.port.output.PasswordHasher
import org.cescfe.bookpublishing.auth.domain.model.PasswordHash
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class SpringPasswordHasher(
    private val passwordEncoder: PasswordEncoder,
) : PasswordHasher {
    override fun hash(rawPassword: String): PasswordHash =
        PasswordHash(passwordEncoder.encode(rawPassword))

    override fun matches(
        rawPassword: String,
        passwordHash: PasswordHash,
    ): Boolean = passwordEncoder.matches(rawPassword, passwordHash.value)
}
