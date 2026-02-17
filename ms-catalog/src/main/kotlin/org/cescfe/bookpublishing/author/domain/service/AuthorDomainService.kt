package org.cescfe.bookpublishing.author.domain.service

interface AuthorDomainService {
    fun ensureEmailUniqueness(email: String?)

    fun ensureEmailUniquenessForUpdate(
        email: String?,
        authorId: String,
    )
}
