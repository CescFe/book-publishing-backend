package org.cescfe.bookpublishing.book.domain.service

interface BookDomainService {
    fun ensureIsbnUniqueness(isbn: String?)

    fun ensureIsbnUniquenessForUpdate(
        isbn: String?,
        bookId: String,
    )
}
