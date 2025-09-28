package org.cescfe.bookpublishing.shared.domain.exception

abstract class ValidationException(
    message: String,
    cause: Throwable? = null,
) : DomainException(message, cause)
