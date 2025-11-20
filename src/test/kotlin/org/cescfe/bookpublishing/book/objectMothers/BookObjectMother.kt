package org.cescfe.bookpublishing.book.objectMothers

import org.cescfe.bookpublishing.book.domain.model.AuthorIdRef
import org.cescfe.bookpublishing.book.domain.model.BasePrice
import org.cescfe.bookpublishing.book.domain.model.Book
import org.cescfe.bookpublishing.book.domain.model.BookId
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
import org.cescfe.bookpublishing.shared.domain.model.enum.Genre
import org.cescfe.bookpublishing.shared.domain.model.enum.Language
import org.cescfe.bookpublishing.shared.domain.model.enum.ReadingLevel
import java.time.LocalDate
import java.util.UUID

object BookObjectMother {
    fun create(
        id: UUID = UUID.randomUUID(),
        title: String = "Test Book",
        authorId: UUID = UUID.randomUUID(),
        collectionId: UUID = UUID.randomUUID(),
        readingLevel: ReadingLevel? = null,
        primaryLanguage: Language? = null,
        secondaryLanguages: List<Language>? = null,
        primaryGenre: Genre? = null,
        secondaryGenres: List<Genre>? = null,
        basePrice: Double = 19.99,
        vatRate: Double? = null,
        isbn: String? = null,
        publicationDate: LocalDate? = null,
        pageCount: Int? = null,
        coverImagePath: String? = null,
        description: String? = null,
        status: Status? = null,
    ): Book =
        Book(
            id = BookId(id),
            title = BookTitle(title),
            authorId = AuthorIdRef(authorId),
            collectionId = CollectionIdRef(collectionId),
            readingLevel = readingLevel,
            primaryLanguage = primaryLanguage,
            secondaryLanguages = secondaryLanguages?.let { SecondaryLanguages(it) },
            primaryGenre = primaryGenre,
            secondaryGenres = secondaryGenres?.let { SecondaryGenres(it) },
            basePrice = BasePrice(basePrice),
            vatRate = vatRate?.let { VatRate(it) },
            isbn = isbn?.let { ISBN(it) },
            publicationDate = publicationDate?.let { PublicationDate(it) },
            pageCount = pageCount?.let { PageCount(it) },
            coverImagePath = coverImagePath?.let { CoverImagePath(it) },
            description = description?.let { Description(it) },
            status = status,
        )

    fun createWithAllFields(
        authorId: UUID = UUID.randomUUID(),
        collectionId: UUID = UUID.randomUUID(),
    ): Book =
        create(
            title = "The Lord of the Rings",
            authorId = authorId,
            collectionId = collectionId,
            readingLevel = ReadingLevel.ADULT,
            primaryLanguage = Language.ENGLISH,
            secondaryLanguages = listOf(Language.CATALAN, Language.SPANISH),
            primaryGenre = Genre.FANTASY,
            secondaryGenres = listOf(Genre.ADVENTURE, Genre.HISTORICAL_FICTION),
            basePrice = 29.99,
            vatRate = 0.04,
            isbn = "9780007141326",
            publicationDate = LocalDate.of(1954, 7, 29),
            pageCount = 1178,
            coverImagePath = "/images/lotr-cover.jpg",
            description = "An epic high fantasy novel written by J.R.R. Tolkien.",
            status = Status.PUBLISHED,
        )

    fun createMinimal(): Book =
        create(
            title = "Minimal Book",
            basePrice = 9.99,
        )
}
