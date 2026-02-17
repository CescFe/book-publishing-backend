package org.cescfe.bookpublishing.book.domain.model

import org.cescfe.bookpublishing.book.domain.model.enum.Status

data class BookSummary(
    val id: BookId,
    val title: BookTitle,
    val authorId: AuthorIdRef,
    val authorName: String,
    val collectionId: CollectionIdRef,
    val collectionName: String,
    val basePrice: BasePrice,
    val finalPrice: Double,
    val isbn: ISBN? = null,
    val status: Status? = null,
)
