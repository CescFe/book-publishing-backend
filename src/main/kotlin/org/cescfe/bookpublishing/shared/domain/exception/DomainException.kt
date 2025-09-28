package org.cescfe.bookpublishing.shared.domain.exception

abstract class DomainException(
    message: String,
    cause: Throwable? = null
) : RuntimeException(message, cause)
