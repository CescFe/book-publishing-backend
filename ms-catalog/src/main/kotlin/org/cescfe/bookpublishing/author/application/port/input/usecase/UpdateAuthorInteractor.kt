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
    override fun execute(
        authorId: String,
        command: UpdateAuthorUseCase.Command,
    ): Author {
        val authorIdDomain = AuthorId.fromString(authorId)
        val existingAuthor =
            authorRepository.findById(authorIdDomain)
                ?: throw AuthorDomainException.authorNotFound(authorId)

        authorDomainService.ensureEmailUniquenessForUpdate(command.email, authorId)
        val updatedAuthor = mapper.toDomain(command, existingAuthor)

        return authorRepository.save(updatedAuthor)
    }
}
