package org.cescfe.bookpublishing.author.application.port.input.usecase

import org.cescfe.bookpublishing.author.application.port.input.DeleteAuthorUseCase
import org.cescfe.bookpublishing.author.domain.exception.AuthorDomainException
import org.cescfe.bookpublishing.author.domain.model.AuthorId
import org.cescfe.bookpublishing.author.domain.port.AuthorRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class DeleteAuthorInteractor(
    private val authorRepository: AuthorRepository,
) : DeleteAuthorUseCase {
    override fun execute(command: DeleteAuthorUseCase.Command) {
        val authorId = AuthorId.fromString(command.authorId)

        authorRepository.findById(authorId)
            ?: throw AuthorDomainException.authorNotFound(command.authorId)

        authorRepository.deleteById(authorId)
    }
}
