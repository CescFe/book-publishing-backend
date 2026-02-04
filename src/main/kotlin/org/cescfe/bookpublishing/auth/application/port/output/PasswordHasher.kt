package org.cescfe.bookpublishing.auth.application.port.output

import org.cescfe.bookpublishing.auth.domain.model.PasswordHash

interface PasswordHasher {
    fun hash(rawPassword: String): PasswordHash

    fun matches(
        rawPassword: String,
        passwordHash: PasswordHash,
    ): Boolean
}
