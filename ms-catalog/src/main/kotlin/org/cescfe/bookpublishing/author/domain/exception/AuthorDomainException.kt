package org.cescfe.bookpublishing.author.domain.exception

import org.cescfe.bookpublishing.shared.domain.exception.DomainException

class AuthorDomainException(
    message: String,
    val subType: String,
    cause: Throwable? = null,
) : DomainException(message, cause) {
    companion object {
        // AuthorId exceptions
        fun authorIdInvalidFormat(id: String): AuthorDomainException =
            AuthorDomainException(
                "Author id '$id' has invalid format. Expected a valid UUID",
                "AUTHOR_ID_INVALID_FORMAT",
            )

        // FullName exceptions
        fun fullNameCannotBeBlank(): AuthorDomainException =
            AuthorDomainException("Full name cannot be blank", "FULL_NAME_CANNOT_BE_BLANK")

        fun fullNameTooLong(): AuthorDomainException =
            AuthorDomainException("Full name must be between 1 and 255 characters", "FULL_NAME_TOO_LONG")

        // Pseudonym exceptions
        fun pseudonymCannotBeBlank(): AuthorDomainException =
            AuthorDomainException("Pseudonym cannot be blank", "PSEUDONYM_CANNOT_BE_BLANK")

        fun pseudonymTooLong(): AuthorDomainException =
            AuthorDomainException("Pseudonym must be between 1 and 255 characters", "PSEUDONYM_TOO_LONG")

        // Biography exceptions
        fun biographyTooLong(): AuthorDomainException =
            AuthorDomainException("Biography cannot exceed 2000 characters", "BIOGRAPHY_TOO_LONG")

        // Email exceptions
        fun emailCannotBeBlank(): AuthorDomainException =
            AuthorDomainException("Email cannot be blank", "EMAIL_CANNOT_BE_BLANK")

        fun emailMissingAtSymbol(): AuthorDomainException =
            AuthorDomainException("Email must contain @ symbol", "EMAIL_MISSING_AT_SYMBOL")

        fun emailInvalidFormat(): AuthorDomainException =
            AuthorDomainException("Email format is invalid", "EMAIL_INVALID_FORMAT")

        fun emailAlreadyExists(email: String): AuthorDomainException =
            AuthorDomainException("Author with email '$email' already exists", "EMAIL_ALREADY_EXISTS")

        // Website exceptions
        fun websiteCannotBeBlank(): AuthorDomainException =
            AuthorDomainException("Website cannot be blank", "WEBSITE_CANNOT_BE_BLANK")

        fun websiteInvalidProtocol(): AuthorDomainException =
            AuthorDomainException("Website must start with http:// or https://", "WEBSITE_INVALID_PROTOCOL")

        // Author not found exception
        fun authorNotFound(id: String): AuthorDomainException =
            AuthorDomainException("Author with id $id not found", "AUTHOR_NOT_FOUND")
    }
}
