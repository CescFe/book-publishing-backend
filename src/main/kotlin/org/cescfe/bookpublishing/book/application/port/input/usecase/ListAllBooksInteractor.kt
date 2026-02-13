package org.cescfe.bookpublishing.book.application.port.input.usecase

import org.cescfe.bookpublishing.book.application.port.input.ListAllBooksUseCase
import org.cescfe.bookpublishing.book.application.port.input.mapper.ListBooksUseCaseMapper
import org.cescfe.bookpublishing.book.domain.model.BookSummary
import org.cescfe.bookpublishing.book.domain.port.BookRepository
import org.cescfe.bookpublishing.shared.domain.model.NonPaginatedResult
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ListAllBooksInteractor(
    private val bookRepository: BookRepository,
    private val mapper: ListBooksUseCaseMapper,
) : ListAllBooksUseCase {
    override fun execute(): NonPaginatedResult<BookSummary> {
        val books = bookRepository.findAllSummary()
        val totalCount = bookRepository.countAll()

        return mapper.toNonPaginatedResult(books, totalCount)
    }
}
