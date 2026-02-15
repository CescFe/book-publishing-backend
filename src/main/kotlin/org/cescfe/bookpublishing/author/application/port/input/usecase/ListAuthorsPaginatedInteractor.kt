package org.cescfe.bookpublishing.author.application.port.input.usecase

import org.cescfe.bookpublishing.author.application.port.input.ListAuthorsPaginatedUseCase
import org.cescfe.bookpublishing.author.application.port.input.mapper.ListAuthorsUseCaseMapper
import org.cescfe.bookpublishing.author.domain.model.AuthorSummary
import org.cescfe.bookpublishing.author.domain.port.AuthorRepository
import org.cescfe.bookpublishing.shared.domain.model.PaginatedResult
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ListAuthorsPaginatedInteractor(
    private val authorRepository: AuthorRepository,
    private val mapper: ListAuthorsUseCaseMapper,
) : ListAuthorsPaginatedUseCase {
    override fun execute(query: ListAuthorsPaginatedUseCase.Query): PaginatedResult<AuthorSummary> {
        val authors = authorRepository.findAllSummary(query.page, query.limit)
        val totalCount = authorRepository.countAll()

        return mapper.toPaginatedResult(authors, totalCount, query.page, query.limit)
    }
}
