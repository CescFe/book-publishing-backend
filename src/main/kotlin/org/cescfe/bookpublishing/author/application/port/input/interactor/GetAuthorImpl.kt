package org.cescfe.bookpublishing.author.application.port.input.interactor

import org.cescfe.bookpublishing.author.application.port.input.GetAuthorUseCase
import org.cescfe.bookpublishing.author.application.port.input.mapper.GetAuthorUseCaseMapper
import org.cescfe.bookpublishing.author.domain.exception.AuthorDomainException
import org.cescfe.bookpublishing.author.domain.model.Author
import org.cescfe.bookpublishing.author.domain.port.AuthorRepositoryView
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class GetAuthorImpl(
    private val authorRepository: AuthorRepositoryView,
    private val mapper: GetAuthorUseCaseMapper,
) : GetAuthorUseCase {
    override fun execute(input: GetAuthorUseCase.InputValues): Author {
        val authorId = mapper.toDomain(input.authorId)
        return authorRepository.findById(authorId)
            ?: throw AuthorDomainException.authorNotFound(input.authorId)
    }
}
