package org.cescfe.bookpublishing.author.domain.exception

import org.cescfe.bookpublishing.shared.domain.exception.DomainException

abstract class AuthorDomainException(
    message: String,
    cause: Throwable? = null
) : DomainException(message, cause)
