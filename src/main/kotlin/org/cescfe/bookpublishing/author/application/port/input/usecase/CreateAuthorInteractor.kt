package org.cescfe.bookpublishing.author.application.port.input.usecase

import org.cescfe.bookpublishing.author.application.port.input.CreateAuthorUseCase
import org.cescfe.bookpublishing.author.application.port.input.mapper.CreateAuthorUseCaseMapper
import org.cescfe.bookpublishing.author.domain.model.Author
import org.cescfe.bookpublishing.author.domain.port.AuthorRepository
import org.cescfe.bookpublishing.author.domain.service.AuthorDomainService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CreateAuthorInteractor(
    private val authorRepository: AuthorRepository,
    private val mapper: CreateAuthorUseCaseMapper,
    private val authorDomainService: AuthorDomainService,
) : CreateAuthorUseCase {
    override fun execute(command: CreateAuthorUseCase.Command): Author {
        authorDomainService.ensureEmailUniqueness(command.email)
        val author = mapper.toDomain(command)
        return authorRepository.save(author)
    }
}
