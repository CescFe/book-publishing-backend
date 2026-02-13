package org.cescfe.bookpublishing.book.application.port.input

import org.cescfe.bookpublishing.book.domain.model.BookSummary
import org.cescfe.bookpublishing.shared.domain.model.NonPaginatedResult

interface ListBooksUseCase {
    fun execute(): NonPaginatedResult<BookSummary>
}
