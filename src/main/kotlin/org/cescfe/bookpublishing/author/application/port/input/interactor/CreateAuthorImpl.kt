package org.cescfe.bookpublishing.author.application.port.input.interactor

import org.cescfe.bookpublishing.author.application.port.input.CreateAuthorUseCase
import org.cescfe.bookpublishing.author.application.port.input.mapper.CreateAuthorUseCaseMapper
import org.cescfe.bookpublishing.author.domain.model.Author
import org.cescfe.bookpublishing.author.domain.port.AuthorRepositoryView
import org.cescfe.bookpublishing.author.domain.service.AuthorDomainService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CreateAuthorImpl(
    private val authorRepository: AuthorRepositoryView,
    private val mapper: CreateAuthorUseCaseMapper,
    private val authorDomainService: AuthorDomainService,
) : CreateAuthorUseCase {
    override fun execute(input: CreateAuthorUseCase.InputValues): Author {
        authorDomainService.ensureEmailUniqueness(input.email)
        val author = mapper.toDomain(input)
        return authorRepository.save(author)
    }
}
