package org.cescfe.bookpublishing.book.application.port.input.usecase

import org.cescfe.bookpublishing.author.domain.model.PaginatedResult
import org.cescfe.bookpublishing.book.application.port.input.ListBooksUseCase
import org.cescfe.bookpublishing.book.application.port.input.mapper.ListBooksUseCaseMapper
import org.cescfe.bookpublishing.book.domain.model.BookSummary
import org.cescfe.bookpublishing.book.domain.port.BookRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ListBooksInteractor(
    private val bookRepository: BookRepository,
    private val mapper: ListBooksUseCaseMapper,
) : ListBooksUseCase {
    override fun execute(query: ListBooksUseCase.Query): PaginatedResult<BookSummary> {
        val books = bookRepository.findAllSummary(query.page, query.limit)
        val totalCount = bookRepository.countAll()

        return mapper.toPaginatedResult(books, totalCount, query.page, query.limit)
    }
}
