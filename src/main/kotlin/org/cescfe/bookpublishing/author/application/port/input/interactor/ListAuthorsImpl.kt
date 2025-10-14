package org.cescfe.bookpublishing.author.application.port.input.interactor

import org.cescfe.bookpublishing.author.application.port.input.ListAuthorsUseCase
import org.cescfe.bookpublishing.author.application.port.input.mapper.ListAuthorsUseCaseMapper
import org.cescfe.bookpublishing.author.domain.model.Author
import org.cescfe.bookpublishing.author.domain.model.PaginatedResult
import org.cescfe.bookpublishing.author.domain.port.AuthorRepositoryView
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ListAuthorsImpl(
    private val authorRepository: AuthorRepositoryView,
    private val mapper: ListAuthorsUseCaseMapper,
) : ListAuthorsUseCase {
    override fun execute(input: ListAuthorsUseCase.InputValues): PaginatedResult<Author> {
        val authors = authorRepository.findAll(input.page, input.limit)
        val totalCount = authorRepository.countAll()

        return mapper.toPaginatedResult(authors, totalCount, input.page, input.limit)
    }
}
