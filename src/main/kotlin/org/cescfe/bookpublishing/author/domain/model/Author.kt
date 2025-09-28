package org.cescfe.bookpublishing.author.domain.model

import org.cescfe.bookpublishing.author.domain.exception.AuthorValidationException
import org.cescfe.bookpublishing.author.domain.exception.InvalidAuthorRolesException
import java.util.UUID

data class Author(
    val id: AuthorId,
    val fullName: FullName,
    val roles: Set<AuthorRole>,
    val pseudonym: Pseudonym? = null,
    val biography: Biography? = null,
    val email: Email? = null,
    val website: Website? = null,
) {
    init {
        require(roles.isNotEmpty()) { throw InvalidAuthorRolesException.emptyRoles() }
        require(roles.contains(AuthorRole.AUTHOR)) { throw InvalidAuthorRolesException.missingAuthorRole() }
    }
}

@JvmInline
value class AuthorId(
    val value: UUID,
) {
    companion object {
        fun generate(): AuthorId = AuthorId(UUID.randomUUID())

        fun fromString(value: String): AuthorId = AuthorId(UUID.fromString(value))
    }
}

@JvmInline
value class FullName(
    val value: String,
) {
    init {
        require(value.isNotBlank()) { throw AuthorValidationException.fullNameCannotBeBlank() }
        require(value.length in 1..255) { throw AuthorValidationException.fullNameTooLong() }
    }
}

@JvmInline
value class Pseudonym(
    val value: String,
) {
    init {
        require(value.isNotBlank()) { throw AuthorValidationException.pseudonymCannotBeBlank() }
        require(value.length in 1..255) { throw AuthorValidationException.pseudonymTooLong() }
    }
}

@JvmInline
value class Biography(
    val value: String,
) {
    init {
        require(value.length <= 2000) { throw AuthorValidationException.biographyTooLong() }
    }
}

@JvmInline
value class Email(
    val value: String,
) {
    init {
        require(value.isNotBlank()) { throw AuthorValidationException.emailCannotBeBlank() }
        require(value.contains("@")) { throw AuthorValidationException.emailMissingAtSymbol() }
        require(value.matches(EMAIL_REGEX)) { throw AuthorValidationException.emailInvalidFormat() }
    }

    companion object {
        private val EMAIL_REGEX = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    }
}

@JvmInline
value class Website(
    val value: String,
) {
    init {
        require(value.isNotBlank()) { throw AuthorValidationException.websiteCannotBeBlank() }
        require(value.startsWith("http://") || value.startsWith("https://")) {
            throw AuthorValidationException.websiteInvalidProtocol()
        }
    }
}

enum class AuthorRole(
    val value: String,
) {
    AUTHOR("AUTHOR"),
    ILLUSTRATOR("ILLUSTRATOR"),
    TRANSLATOR("TRANSLATOR"),
    CURATOR("CURATOR"),
    ;

    companion object {
        fun fromString(value: String): AuthorRole =
            entries.find { it.value == value }
                ?: throw IllegalArgumentException("Unknown author role: $value")
    }
}
