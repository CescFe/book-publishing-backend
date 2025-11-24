package org.cescfe.bookpublishing.author.application.port.input.usecase

import org.cescfe.bookpublishing.author.application.port.input.DeleteAuthorUseCase
import org.cescfe.bookpublishing.author.domain.exception.AuthorDomainException
import org.cescfe.bookpublishing.author.domain.model.AuthorId
import org.cescfe.bookpublishing.author.domain.port.AuthorRepositoryView
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class DeleteAuthorInteractor(
    private val authorRepository: AuthorRepositoryView,
) : DeleteAuthorUseCase {
    override fun execute(input: DeleteAuthorUseCase.InputValues) {
        val authorId = AuthorId.fromString(input.authorId)

        authorRepository.findById(authorId)
            ?: throw AuthorDomainException.authorNotFound(input.authorId)

        authorRepository.deleteById(authorId)
    }
}
