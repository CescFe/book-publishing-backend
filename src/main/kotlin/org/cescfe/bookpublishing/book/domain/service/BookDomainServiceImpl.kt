package org.cescfe.bookpublishing.book.domain.service

import org.cescfe.bookpublishing.book.domain.exception.BookDomainException
import org.cescfe.bookpublishing.book.domain.port.BookRepository
import org.springframework.stereotype.Component

@Component
class BookDomainServiceImpl(
    private val bookRepository: BookRepository,
) : BookDomainService {
    override fun ensureIsbnUniqueness(isbn: String?) {
        if (isbn != null && bookRepository.existsByIsbn(isbn)) {
            throw BookDomainException.isbnAlreadyExists(isbn)
        }
    }

    override fun ensureIsbnUniquenessForUpdate(
        isbn: String?,
        bookId: String,
    ) {
        if (isbn != null) {
            val existingBook = bookRepository.findByIsbn(isbn)
            if (existingBook != null && existingBook.id.value.toString() != bookId) {
                throw BookDomainException.isbnAlreadyExists(isbn)
            }
        }
    }
}
