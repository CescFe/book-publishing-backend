package org.cescfe.bookpublishing.book.infrastructure.adapters.output.persistence.mapper

import org.cescfe.bookpublishing.book.domain.model.AuthorIdRef
import org.cescfe.bookpublishing.book.domain.model.BasePrice
import org.cescfe.bookpublishing.book.domain.model.Book
import org.cescfe.bookpublishing.book.domain.model.BookId
import org.cescfe.bookpublishing.book.domain.model.BookSummary
import org.cescfe.bookpublishing.book.domain.model.BookTitle
import org.cescfe.bookpublishing.book.domain.model.CollectionIdRef
import org.cescfe.bookpublishing.book.domain.model.CoverImagePath
import org.cescfe.bookpublishing.book.domain.model.Description
import org.cescfe.bookpublishing.book.domain.model.ISBN
import org.cescfe.bookpublishing.book.domain.model.PageCount
import org.cescfe.bookpublishing.book.domain.model.PublicationDate
import org.cescfe.bookpublishing.book.domain.model.SecondaryGenres
import org.cescfe.bookpublishing.book.domain.model.SecondaryLanguages
import org.cescfe.bookpublishing.book.domain.model.VatRate
import org.cescfe.bookpublishing.book.infrastructure.adapters.output.persistence.entity.BookEntity
import org.cescfe.bookpublishing.book.infrastructure.adapters.output.persistence.projection.BookSummaryProjection
import org.cescfe.bookpublishing.shared.domain.model.Metadata
import org.springframework.stereotype.Component

@Component
class BookPersistenceMapper {
    fun fromDomain(book: Book): BookEntity =
        BookEntity(
            id = book.id.value,
            title = book.title.value,
            authorId = book.authorId.value,
            collectionId = book.collectionId.value,
            readingLevel = book.readingLevel,
            primaryLanguage = book.primaryLanguage,
            secondaryLanguages = book.secondaryLanguages?.value,
            primaryGenre = book.primaryGenre,
            secondaryGenres = book.secondaryGenres?.value,
            basePrice = book.basePrice.value,
            finalPrice = book.finalPrice,
            vatRate = book.vatRate?.value,
            isbn = book.isbn?.value,
            publicationDate = book.publicationDate?.value,
            pageCount = book.pageCount?.value,
            coverImagePath = book.coverImagePath?.value,
            description = book.description?.value,
            status = book.status,
        )

    fun toDomain(entity: BookEntity): Book =
        Book(
            id = BookId(entity.id),
            title = BookTitle(entity.title),
            authorId = AuthorIdRef(entity.authorId),
            collectionId = CollectionIdRef(entity.collectionId),
            readingLevel = entity.readingLevel,
            primaryLanguage = entity.primaryLanguage,
            secondaryLanguages = entity.secondaryLanguages?.let { SecondaryLanguages(it) },
            primaryGenre = entity.primaryGenre,
            secondaryGenres = entity.secondaryGenres?.let { SecondaryGenres(it) },
            basePrice = BasePrice(entity.basePrice),
            vatRate = entity.vatRate?.let { VatRate(it) },
            finalPrice = entity.finalPrice,
            isbn = entity.isbn?.let { ISBN(it) },
            publicationDate = entity.publicationDate?.let { PublicationDate(it) },
            pageCount = entity.pageCount?.let { PageCount(it) },
            coverImagePath = entity.coverImagePath?.let { CoverImagePath(it) },
            description = entity.description?.let { Description(it) },
            status = entity.status,
            audit =
                Metadata(
                    createdAt = entity.createdAt,
                    createdBy = entity.createdBy,
                    updatedAt = entity.updatedAt,
                    updatedBy = entity.updatedBy,
                ),
        )

    fun toDomainSummary(projection: BookSummaryProjection): BookSummary =
        BookSummary(
            id = BookId(projection.id),
            title = BookTitle(projection.title),
            authorId = AuthorIdRef(projection.authorId),
            collectionId = CollectionIdRef(projection.collectionId),
            basePrice = BasePrice(projection.basePrice),
            finalPrice = projection.finalPrice,
            status = projection.status,
        )
}
