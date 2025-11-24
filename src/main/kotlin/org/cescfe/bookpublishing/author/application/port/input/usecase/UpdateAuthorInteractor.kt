package org.cescfe.bookpublishing.author.application.port.input.usecase

import org.cescfe.bookpublishing.author.application.port.input.UpdateAuthorUseCase
import org.cescfe.bookpublishing.author.application.port.input.mapper.UpdateAuthorUseCaseMapper
import org.cescfe.bookpublishing.author.domain.exception.AuthorDomainException
import org.cescfe.bookpublishing.author.domain.model.Author
import org.cescfe.bookpublishing.author.domain.model.AuthorId
import org.cescfe.bookpublishing.author.domain.port.AuthorRepository
import org.cescfe.bookpublishing.author.domain.service.AuthorDomainService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UpdateAuthorInteractor(
    private val authorRepository: AuthorRepository,
    private val mapper: UpdateAuthorUseCaseMapper,
    private val authorDomainService: AuthorDomainService,
) : UpdateAuthorUseCase {
    override fun execute(command: UpdateAuthorUseCase.Command): Author {
        val authorId = AuthorId.fromString(command.authorId)
        val existingAuthor =
            authorRepository.findById(authorId)
                ?: throw AuthorDomainException.authorNotFound(command.authorId)

        authorDomainService.ensureEmailUniquenessForUpdate(command.email, command.authorId)
        val updatedAuthor = mapper.toDomain(command, existingAuthor)

        return authorRepository.save(updatedAuthor)
    }
}
