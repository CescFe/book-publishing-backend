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
}
