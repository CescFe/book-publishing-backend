package org.cescfe.bookpublishing.book.objectMothers

import org.cescfe.bookpublishing.book.domain.model.AuthorIdRef
import org.cescfe.bookpublishing.book.domain.model.BasePrice
import org.cescfe.bookpublishing.book.domain.model.BookId
import org.cescfe.bookpublishing.book.domain.model.BookSummary
import org.cescfe.bookpublishing.book.domain.model.BookTitle
import org.cescfe.bookpublishing.book.domain.model.CollectionIdRef
import org.cescfe.bookpublishing.book.domain.model.ISBN
import org.cescfe.bookpublishing.book.domain.model.enum.Status
import java.util.UUID

object BookSummaryObjectMother {
    private const val BOOK_ID_CONTRACT_TEST = "477537ff-7e8b-4930-bd41-d7f3589120b1"

    fun create(
        id: UUID = UUID.randomUUID(),
        title: String = "Test Book",
        authorId: UUID = UUID.randomUUID(),
        collectionId: UUID = UUID.randomUUID(),
        basePrice: Double = 19.23,
        isbn: String? = null,
        status: String? = null,
    ): BookSummary =
        BookSummary(
            id = BookId(id),
            title = BookTitle(title),
            authorId = AuthorIdRef(authorId),
            collectionId = CollectionIdRef(collectionId),
            basePrice = BasePrice(basePrice),
            isbn = isbn?.let(::ISBN),
            status = status?.let(Status::valueOf),
        )

    fun createFirstBookSummary(): BookSummary =
        create(
            id = UUID.fromString(BOOK_ID_CONTRACT_TEST),
            title = "The Hobbit",
            authorId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000"),
            collectionId = UUID.fromString("223e4567-e89b-12d3-a456-426614174000"),
            basePrice = 25.50,
            isbn = "9783161484100",
            status = "PUBLISHED",
        )

    fun createSecondBookSummary(): BookSummary =
        create(
            id = UUID.fromString("12345678-1234-1234-1234-123456789012"),
            title = "Minimal Book",
            authorId = UUID.fromString("323e4567-e89b-12d3-a456-426614174000"),
            collectionId = UUID.fromString("423e4567-e89b-12d3-a456-426614174000"),
            basePrice = 15.00,
        )
}
