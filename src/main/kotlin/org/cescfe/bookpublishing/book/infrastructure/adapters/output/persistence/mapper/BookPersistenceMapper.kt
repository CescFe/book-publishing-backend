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
import org.cescfe.bookpublishing.book.domain.model.enum.Status
import org.cescfe.bookpublishing.book.infrastructure.adapters.output.persistence.entity.BookEntity
import org.cescfe.bookpublishing.book.infrastructure.adapters.output.persistence.projection.BookSummaryProjection
import org.cescfe.bookpublishing.book.infrastructure.adapters.output.persistence.projection.BookWithRelationsProjection
import org.cescfe.bookpublishing.shared.domain.model.Metadata
import org.cescfe.bookpublishing.shared.domain.model.enum.Genre
import org.cescfe.bookpublishing.shared.domain.model.enum.Language
import org.cescfe.bookpublishing.shared.domain.model.enum.ReadingLevel
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

    fun toDomainSummaryWithRelations(projection: BookSummaryProjection): BookSummary =
        BookSummary(
            id = BookId(projection.id),
            title = BookTitle(projection.title),
            authorId = AuthorIdRef(projection.authorId),
            authorName = projection.authorName,
            collectionId = CollectionIdRef(projection.collectionId),
            collectionName = projection.collectionName,
            basePrice = BasePrice(projection.basePrice),
            finalPrice = projection.finalPrice,
            isbn = projection.isbn?.let { ISBN(it) },
            status = projection.status,
        )

    fun toDomainWithRelations(projection: BookWithRelationsProjection): Book =
        Book(
            id = BookId(projection.id),
            title = BookTitle(projection.title),
            authorId = AuthorIdRef(projection.authorId),
            authorName = projection.authorName,
            collectionId = CollectionIdRef(projection.collectionId),
            collectionName = projection.collectionName,
            readingLevel = projection.readingLevel?.let { ReadingLevel.valueOf(it) },
            primaryLanguage = projection.primaryLanguage?.let { Language.valueOf(it) },
            secondaryLanguages = parseSecondaryLanguages(projection.secondaryLanguages),
            primaryGenre = projection.primaryGenre?.let { Genre.valueOf(it) },
            secondaryGenres = parseSecondaryGenres(projection.secondaryGenres),
            basePrice = BasePrice(projection.basePrice),
            vatRate = projection.vatRate?.let { VatRate(it) },
            finalPrice = projection.finalPrice,
            isbn = projection.isbn?.let { ISBN(it) },
            publicationDate = projection.publicationDate?.let { PublicationDate(it) },
            pageCount = projection.pageCount?.let { PageCount(it) },
            coverImagePath = projection.coverImagePath?.let { CoverImagePath(it) },
            description = projection.description?.let { Description(it) },
            status = projection.status?.let { Status.valueOf(it) },
            audit =
                Metadata(
                    createdAt = projection.createdAt,
                    createdBy = projection.createdBy,
                    updatedAt = projection.updatedAt,
                    updatedBy = projection.updatedBy,
                ),
        )

    private fun parseSecondaryLanguages(json: String?): SecondaryLanguages? {
        if (json.isNullOrBlank()) return null
        val languages =
            json
                .removeSurrounding("[", "]")
                .split(",")
                .map { it.trim().removeSurrounding("\"") }
                .filter { it.isNotBlank() }
                .map { Language.valueOf(it) }
        return if (languages.isEmpty()) null else SecondaryLanguages(languages)
    }

    private fun parseSecondaryGenres(json: String?): SecondaryGenres? {
        if (json.isNullOrBlank()) return null
        val genres =
            json
                .removeSurrounding("[", "]")
                .split(",")
                .map { it.trim().removeSurrounding("\"") }
                .filter { it.isNotBlank() }
                .map { Genre.valueOf(it) }
        return if (genres.isEmpty()) null else SecondaryGenres(genres)
    }
}
