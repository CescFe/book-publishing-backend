package org.cescfe.bookpublishing.book.domain.model

import org.cescfe.bookpublishing.book.domain.model.enum.Status

data class BookSummary(
    val id: BookId,
    val title: BookTitle,
    val authorId: AuthorIdRef,
    val collectionId: CollectionIdRef,
    val basePrice: BasePrice,
    val isbn: ISBN? = null,
    val status: Status? = null,
)
