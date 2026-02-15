package org.cescfe.bookpublishing.book.application.port.input.usecase

import org.cescfe.bookpublishing.book.application.port.input.ListBooksPaginatedUseCase
import org.cescfe.bookpublishing.book.application.port.input.mapper.ListBooksUseCaseMapper
import org.cescfe.bookpublishing.book.domain.model.BookSummary
import org.cescfe.bookpublishing.book.domain.port.BookRepository
import org.cescfe.bookpublishing.shared.domain.model.PaginatedResult
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ListBooksPaginatedInteractor(
    private val bookRepository: BookRepository,
    private val mapper: ListBooksUseCaseMapper,
) : ListBooksPaginatedUseCase {
    override fun execute(query: ListBooksPaginatedUseCase.Query): PaginatedResult<BookSummary> {
        val books = bookRepository.findAllSummary(query.page, query.limit)
        val totalCount = bookRepository.countAll()

        return mapper.toPaginatedResult(books, totalCount, query.page, query.limit)
    }
}
