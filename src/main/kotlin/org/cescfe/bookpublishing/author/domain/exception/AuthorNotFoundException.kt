package org.cescfe.bookpublishing.author.domain.exception

import org.cescfe.bookpublishing.author.domain.model.AuthorId
import org.cescfe.bookpublishing.shared.domain.exception.DomainException

class AuthorNotFoundException(
    message: String,
    cause: Throwable? = null,
) : DomainException(
        message,
        cause,
    ) {
    companion object {
        fun byId(id: AuthorId): AuthorNotFoundException =
            AuthorNotFoundException("Author with id ${id.value} not found")
    }
}
