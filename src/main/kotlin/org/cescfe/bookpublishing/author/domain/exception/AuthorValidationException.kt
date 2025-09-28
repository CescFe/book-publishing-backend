package org.cescfe.bookpublishing.author.domain.exception

import org.cescfe.bookpublishing.shared.domain.exception.ValidationException

class AuthorValidationException(
    message: String,
    cause: Throwable? = null,
) : ValidationException(message, cause) {
    companion object {
        // FullName exceptions
        fun fullNameCannotBeBlank(): AuthorValidationException = AuthorValidationException("Full name cannot be blank")

        fun fullNameTooLong(): AuthorValidationException =
            AuthorValidationException("Full name must be between 1 and 255 characters")

        // Pseudonym exceptions
        fun pseudonymCannotBeBlank(): AuthorValidationException = AuthorValidationException("Pseudonym cannot be blank")

        fun pseudonymTooLong(): AuthorValidationException =
            AuthorValidationException("Pseudonym must be between 1 and 255 characters")

        // Biography exceptions
        fun biographyTooLong(): AuthorValidationException =
            AuthorValidationException("Biography cannot exceed 2000 characters")

        // Email exceptions
        fun emailCannotBeBlank(): AuthorValidationException = AuthorValidationException("Email cannot be blank")

        fun emailMissingAtSymbol(): AuthorValidationException = AuthorValidationException("Email must contain @ symbol")

        fun emailInvalidFormat(): AuthorValidationException = AuthorValidationException("Email format is invalid")

        // Website exceptions
        fun websiteCannotBeBlank(): AuthorValidationException = AuthorValidationException("Website cannot be blank")

        fun websiteInvalidProtocol(): AuthorValidationException =
            AuthorValidationException("Website must start with http:// or https://")
    }
}
