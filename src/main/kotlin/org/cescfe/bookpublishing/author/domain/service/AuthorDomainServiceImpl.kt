package org.cescfe.bookpublishing.author.domain.service

import org.cescfe.bookpublishing.author.domain.exception.AuthorDomainException
import org.cescfe.bookpublishing.author.domain.port.AuthorRepositoryView
import org.springframework.stereotype.Component

@Component
class AuthorDomainServiceImpl(
    private val authorRepository: AuthorRepositoryView,
) : AuthorDomainService {
    override fun ensureEmailUniqueness(email: String?) {
        if (email != null && authorRepository.existsByEmail(email)) {
            throw AuthorDomainException.emailAlreadyExists(email)
        }
    }

    override fun ensureEmailUniquenessForUpdate(
        email: String?,
        authorId: String,
    ) {
        if (email != null) {
            val existingAuthor = authorRepository.findByEmail(email)
            if (existingAuthor != null && existingAuthor.id.value.toString() != authorId) {
                throw AuthorDomainException.emailAlreadyExists(email)
            }
        }
    }
}
