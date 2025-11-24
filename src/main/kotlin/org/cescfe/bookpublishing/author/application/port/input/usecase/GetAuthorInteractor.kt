package org.cescfe.bookpublishing.author.application.port.input.usecase

import org.cescfe.bookpublishing.author.application.port.input.GetAuthorUseCase
import org.cescfe.bookpublishing.author.domain.exception.AuthorDomainException
import org.cescfe.bookpublishing.author.domain.model.Author
import org.cescfe.bookpublishing.author.domain.model.AuthorId
import org.cescfe.bookpublishing.author.domain.port.AuthorRepositoryView
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class GetAuthorInteractor(
    private val authorRepository: AuthorRepositoryView,
) : GetAuthorUseCase {
    override fun execute(query: GetAuthorUseCase.Query): Author {
        val authorId = AuthorId.fromString(query.authorId)
        return authorRepository.findById(authorId)
            ?: throw AuthorDomainException.authorNotFound(query.authorId)
    }
}
