package org.cescfe.bookpublishing.author.domain.model

import org.cescfe.bookpublishing.author.domain.exception.AuthorDomainException
import java.util.UUID

data class Author(
    val id: AuthorId,
    val fullName: FullName,
    val pseudonym: Pseudonym? = null,
    val biography: Biography? = null,
    val email: Email? = null,
    val website: Website? = null,
)

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
        require(value.isNotBlank()) { throw AuthorDomainException.fullNameCannotBeBlank() }
        require(value.length in 1..255) { throw AuthorDomainException.fullNameTooLong() }
    }
}

@JvmInline
value class Pseudonym(
    val value: String,
) {
    init {
        require(value.isNotBlank()) { throw AuthorDomainException.pseudonymCannotBeBlank() }
        require(value.length in 1..255) { throw AuthorDomainException.pseudonymTooLong() }
    }
}

@JvmInline
value class Biography(
    val value: String,
) {
    init {
        require(value.length <= 2000) { throw AuthorDomainException.biographyTooLong() }
    }
}

@JvmInline
value class Email(
    val value: String,
) {
    init {
        require(value.isNotBlank()) { throw AuthorDomainException.emailCannotBeBlank() }
        require(value.contains("@")) { throw AuthorDomainException.emailMissingAtSymbol() }
        require(value.matches(EMAIL_REGEX)) { throw AuthorDomainException.emailInvalidFormat() }
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
        require(value.isNotBlank()) { throw AuthorDomainException.websiteCannotBeBlank() }
        require(value.startsWith("http://") || value.startsWith("https://")) {
            throw AuthorDomainException.websiteInvalidProtocol()
        }
    }
}
