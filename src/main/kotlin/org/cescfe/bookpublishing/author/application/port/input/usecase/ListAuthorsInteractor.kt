package org.cescfe.bookpublishing.author.application.port.input.usecase

import org.cescfe.bookpublishing.author.application.port.input.ListAuthorsUseCase
import org.cescfe.bookpublishing.author.application.port.input.mapper.ListAuthorsUseCaseMapper
import org.cescfe.bookpublishing.author.domain.model.AuthorSummary
import org.cescfe.bookpublishing.author.domain.model.PaginatedResult
import org.cescfe.bookpublishing.author.domain.port.AuthorRepositoryView
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ListAuthorsInteractor(
    private val authorRepository: AuthorRepositoryView,
    private val mapper: ListAuthorsUseCaseMapper,
) : ListAuthorsUseCase {
    override fun execute(input: ListAuthorsUseCase.InputValues): PaginatedResult<AuthorSummary> {
        val authors = authorRepository.findAllSummary(input.page, input.limit)
        val totalCount = authorRepository.countAll()

        return mapper.toPaginatedResult(authors, totalCount, input.page, input.limit)
    }
}
