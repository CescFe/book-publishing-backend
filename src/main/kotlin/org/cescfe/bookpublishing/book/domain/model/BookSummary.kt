package org.cescfe.bookpublishing.book.domain.model

import org.cescfe.bookpublishing.book.domain.model.enum.Status

data class BookSummary(
    val id: BookId,
    val title: BookTitle,
    val collectionId: CollectionIdRef,
    val author: AuthorIdRef,
    val basePrice: BasePrice,
    val isbn: ISBN? = null,
    val status: Status? = null,
)
