package org.cescfe.bookpublishing.author.domain.exception

import org.cescfe.bookpublishing.shared.domain.exception.DomainException

class AuthorDomainException(
    message: String,
    cause: Throwable? = null,
) : DomainException(message, cause) {
    companion object {
        // FullName exceptions
        fun fullNameCannotBeBlank(): AuthorDomainException = AuthorDomainException("Full name cannot be blank")

        fun fullNameTooLong(): AuthorDomainException =
            AuthorDomainException("Full name must be between 1 and 255 characters")

        // Pseudonym exceptions
        fun pseudonymCannotBeBlank(): AuthorDomainException = AuthorDomainException("Pseudonym cannot be blank")

        fun pseudonymTooLong(): AuthorDomainException =
            AuthorDomainException("Pseudonym must be between 1 and 255 characters")

        // Biography exceptions
        fun biographyTooLong(): AuthorDomainException = AuthorDomainException("Biography cannot exceed 2000 characters")

        // Email exceptions
        fun emailCannotBeBlank(): AuthorDomainException = AuthorDomainException("Email cannot be blank")

        fun emailMissingAtSymbol(): AuthorDomainException = AuthorDomainException("Email must contain @ symbol")

        fun emailInvalidFormat(): AuthorDomainException = AuthorDomainException("Email format is invalid")

        // Website exceptions
        fun websiteCannotBeBlank(): AuthorDomainException = AuthorDomainException("Website cannot be blank")

        fun websiteInvalidProtocol(): AuthorDomainException =
            AuthorDomainException("Website must start with http:// or https://")

        // Author roles exceptions
        fun emptyRoles(): AuthorDomainException = AuthorDomainException("Author must have at least one role")

        fun missingAuthorRole(): AuthorDomainException = AuthorDomainException("Author must have AUTHOR role")

        // Author not found exception
        fun authorNotFound(id: String): AuthorDomainException = AuthorDomainException("Author with id $id not found")
    }
}
