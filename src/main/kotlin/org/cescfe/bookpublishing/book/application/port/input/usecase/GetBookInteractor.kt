package org.cescfe.bookpublishing.book.application.port.input.usecase

import org.cescfe.bookpublishing.book.application.port.input.GetBookUseCase
import org.cescfe.bookpublishing.book.domain.exception.BookDomainException
import org.cescfe.bookpublishing.book.domain.model.Book
import org.cescfe.bookpublishing.book.domain.model.BookId
import org.cescfe.bookpublishing.book.domain.port.BookRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class GetBookInteractor(
    private val bookRepository: BookRepository,
) : GetBookUseCase {
    override fun execute(query: GetBookUseCase.Query): Book {
        val bookId = BookId.fromString(query.bookId)
        return bookRepository.findById(bookId)
            ?: throw BookDomainException.bookNotFound(query.bookId)
    }
}
