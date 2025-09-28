package org.cescfe.bookpublishing.author.domain.model

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
        require(roles.isNotEmpty()) { "Author must have at least one role" }
        require(roles.contains(AuthorRole.AUTHOR)) { "Author must have AUTHOR role" }
    }
}

@JvmInline
value class AuthorId(val value: UUID) {
    companion object {
        fun generate(): AuthorId = AuthorId(UUID.randomUUID())
        fun fromString(value: String): AuthorId = AuthorId(UUID.fromString(value))
    }
}

@JvmInline
value class FullName(val value: String) {
    init {
        require(value.isNotBlank()) { "Full name cannot be blank" }
        require(value.length in 1..255) { "Full name must be between 1 and 255 characters" }
    }
}

@JvmInline
value class Pseudonym(val value: String) {
    init {
        require(value.isNotBlank()) { "Pseudonym cannot be blank" }
        require(value.length in 1..255) { "Pseudonym must be between 1 and 255 characters" }
    }
}

@JvmInline
value class Biography(val value: String) {
    init {
        require(value.length <= 2000) { "Biography cannot exceed 2000 characters" }
    }
}

@JvmInline
value class Email(val value: String) {
    init {
        require(value.isNotBlank()) { "Email cannot be blank" }
        require(value.contains("@")) { "Email must contain @ symbol" }
        require(value.matches(EMAIL_REGEX)) { "Email format is invalid" }
    }

    companion object {
        private val EMAIL_REGEX = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    }
}

@JvmInline
value class Website(val value: String) {
    init {
        require(value.isNotBlank()) { "Website cannot be blank" }
        require(value.startsWith("http://") || value.startsWith("https://")) {
            "Website must start with http:// or https://"
        }
    }
}

enum class AuthorRole(val value: String) {
    AUTHOR("AUTHOR"),
    ILLUSTRATOR("ILLUSTRATOR"),
    TRANSLATOR("TRANSLATOR"),
    CURATOR("CURATOR");

    companion object {
        fun fromString(value: String): AuthorRole {
            return entries.find { it.value == value }
                ?: throw IllegalArgumentException("Unknown author role: $value")
        }
    }
}
