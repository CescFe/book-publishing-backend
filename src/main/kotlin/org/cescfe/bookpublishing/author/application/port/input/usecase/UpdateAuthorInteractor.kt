package org.cescfe.bookpublishing.author.application.port.input.usecase

import org.cescfe.bookpublishing.author.application.port.input.UpdateAuthorUseCase
import org.cescfe.bookpublishing.author.application.port.input.mapper.UpdateAuthorUseCaseMapper
import org.cescfe.bookpublishing.author.domain.exception.AuthorDomainException
import org.cescfe.bookpublishing.author.domain.model.Author
import org.cescfe.bookpublishing.author.domain.model.AuthorId
import org.cescfe.bookpublishing.author.domain.port.AuthorRepositoryView
import org.cescfe.bookpublishing.author.domain.service.AuthorDomainService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UpdateAuthorInteractor(
    private val authorRepository: AuthorRepositoryView,
    private val mapper: UpdateAuthorUseCaseMapper,
    private val authorDomainService: AuthorDomainService,
) : UpdateAuthorUseCase {
    override fun execute(input: UpdateAuthorUseCase.InputValues): Author {
        val authorId = AuthorId.fromString(input.authorId)
        val existingAuthor =
            authorRepository.findById(authorId)
                ?: throw AuthorDomainException.authorNotFound(input.authorId)

        authorDomainService.ensureEmailUniquenessForUpdate(input.email, input.authorId)
        val updatedAuthor = mapper.toDomain(input, existingAuthor)

        return authorRepository.save(updatedAuthor)
    }
}
