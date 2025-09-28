package org.cescfe.bookpublishing.author.domain.exception

import org.cescfe.bookpublishing.shared.domain.exception.ValidationException

class AuthorValidationException(
    message: String,
    cause: Throwable? = null
) : ValidationException(message, cause) {

    companion object {
        fun invalidFullName(message: String): AuthorValidationException {
            return AuthorValidationException("Invalid full name: $message")
        }

        fun invalidEmail(message: String): AuthorValidationException {
            return AuthorValidationException("Invalid email: $message")
        }

        fun invalidWebsite(message: String): AuthorValidationException {
            return AuthorValidationException("Invalid website: $message")
        }
    }
}
