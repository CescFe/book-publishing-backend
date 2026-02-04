package org.cescfe.bookpublishing.auth.domain.model

import org.cescfe.bookpublishing.auth.domain.exception.AuthDomainException
import org.cescfe.bookpublishing.auth.domain.model.enum.Permission
import org.cescfe.bookpublishing.auth.domain.model.enum.Role
import java.util.UUID

data class Account(
    val id: UserId,
    val username: Username,
    val passwordHash: PasswordHash,
    val roles: Set<Role> = emptySet(),
    val permissions: Set<Permission> = emptySet(),
)

@JvmInline
value class UserId(
    val value: UUID,
) {
    companion object {
        private val UUID_REGEX = Regex("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$")

        fun generate(): UserId = UserId(UUID.randomUUID())

        fun fromString(value: String): UserId {
            require(value.matches(UUID_REGEX)) {
                throw AuthDomainException.userIdInvalidFormat(value)
            }
            return UserId(UUID.fromString(value))
        }
    }
}

@JvmInline
value class Username(
    val value: String,
) {
    init {
        require(value.isNotBlank()) { throw AuthDomainException.usernameCannotBeBlank() }
        require(value.length <= 255) { throw AuthDomainException.usernameTooLong() }
    }
}

@JvmInline
value class PasswordHash(
    val value: String,
) {
    init {
        require(value.isNotBlank()) { throw AuthDomainException.passwordHashCannotBeBlank() }
    }
}
