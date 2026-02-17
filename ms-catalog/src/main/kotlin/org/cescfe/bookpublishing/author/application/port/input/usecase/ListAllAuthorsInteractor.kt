package org.cescfe.bookpublishing.author.application.port.input.usecase

import org.cescfe.bookpublishing.author.application.port.input.ListAllAuthorsUseCase
import org.cescfe.bookpublishing.author.application.port.input.mapper.ListAuthorsUseCaseMapper
import org.cescfe.bookpublishing.author.domain.model.AuthorSummary
import org.cescfe.bookpublishing.author.domain.port.AuthorRepository
import org.cescfe.bookpublishing.shared.domain.model.NonPaginatedResult
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ListAllAuthorsInteractor(
    private val authorRepository: AuthorRepository,
    private val mapper: ListAuthorsUseCaseMapper,
) : ListAllAuthorsUseCase {
    override fun execute(): NonPaginatedResult<AuthorSummary> {
        val authors = authorRepository.findAllSummary()
        val totalCount = authorRepository.countAll()

        return mapper.toNonPaginatedResult(authors, totalCount)
    }
}
