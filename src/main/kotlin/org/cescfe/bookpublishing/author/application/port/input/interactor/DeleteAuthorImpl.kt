package org.cescfe.bookpublishing.author.application.port.input.interactor

import org.cescfe.bookpublishing.author.application.port.input.DeleteAuthorUseCase
import org.cescfe.bookpublishing.author.application.port.input.mapper.DeleteAuthorUseCaseMapper
import org.cescfe.bookpublishing.author.domain.exception.AuthorDomainException
import org.cescfe.bookpublishing.author.domain.model.AuthorRole
import org.cescfe.bookpublishing.author.domain.port.AuthorRepositoryView
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class DeleteAuthorImpl(
    private val authorRepository: AuthorRepositoryView,
    private val mapper: DeleteAuthorUseCaseMapper,
) : DeleteAuthorUseCase {
    override fun execute(input: DeleteAuthorUseCase.InputValues) {
        val authorId = mapper.toDomain(input.authorId)

        val author = authorRepository.findById(authorId)
            ?: throw AuthorDomainException.authorNotFound(input.authorId)

        if (author.roles.size == 1 && author.roles.contains(AuthorRole.AUTHOR)) {
            authorRepository.deleteById(authorId)
        } else {
            authorRepository.removeAuthorRole(authorId)
        }
    }
}
