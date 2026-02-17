package org.cescfe.bookpublishing.book.application.port.input

import org.cescfe.bookpublishing.book.domain.model.Book

interface GetBookUseCase {
    fun execute(query: Query): Book

    data class Query(
        val bookId: String,
    )
}
