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
import org.cescfe.bookpublishing.shared.domain.model.Metadata
import org.cescfe.bookpublishing.shared.domain.model.enum.Genre
import org.cescfe.bookpublishing.shared.domain.model.enum.Language
import org.cescfe.bookpublishing.shared.domain.model.enum.ReadingLevel
import java.time.LocalDate
import java.util.UUID

object BookObjectMother {
    private const val BOOK_ID_CONTROLLER_IT = "223e4567-e89b-12d3-a456-426614174000"

    fun create(
        id: UUID = UUID.randomUUID(),
        title: String = "Test Book",
        authorId: UUID = UUID.randomUUID(),
        authorName: String = "Author Name",
        collectionId: UUID = UUID.randomUUID(),
        collectionName: String = "Collection Name",
        readingLevel: ReadingLevel? = null,
        primaryLanguage: Language? = null,
        secondaryLanguages: List<Language>? = null,
        primaryGenre: Genre? = null,
        secondaryGenres: List<Genre>? = null,
        basePrice: Double = 19.99,
        vatRate: Double? = null,
        finalPrice: Double? = null,
        isbn: String? = null,
        publicationDate: LocalDate? = null,
        pageCount: Int? = null,
        coverImagePath: String? = null,
        description: String? = null,
        status: Status? = null,
        audit: Metadata? = null,
    ): Book =
        Book(
            id = BookId(id),
            title = BookTitle(title),
            authorId = AuthorIdRef(authorId),
            authorName = authorName,
            collectionId = CollectionIdRef(collectionId),
            collectionName = collectionName,
            readingLevel = readingLevel,
            primaryLanguage = primaryLanguage,
            secondaryLanguages = secondaryLanguages?.let { SecondaryLanguages(it) },
            primaryGenre = primaryGenre,
            secondaryGenres = secondaryGenres?.let { SecondaryGenres(it) },
            basePrice = BasePrice(basePrice),
            vatRate = vatRate?.let { VatRate(it) },
            finalPrice = Book.calculateFinalPrice(basePrice, vatRate),
            isbn = isbn?.let { ISBN(it) },
            publicationDate = publicationDate?.let { PublicationDate(it) },
            pageCount = pageCount?.let { PageCount(it) },
            coverImagePath = coverImagePath?.let { CoverImagePath(it) },
            description = description?.let { Description(it) },
            status = status,
            audit = audit,
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
            finalPrice = Book.calculateFinalPrice(29.99, 0.04),
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

    // ==============
    // Controller IT
    // ==============
    fun createForControllerIT(): Book =
        create(
            id = UUID.fromString(BOOK_ID_CONTROLLER_IT),
            title = "The Lord of the Rings",
            authorId = UUID.fromString("223e4567-e89b-12d3-a456-426614174001"),
            authorName = "J.R.R. Tolkien",
            collectionId = UUID.fromString("223e4567-e89b-12d3-a456-426614174002"),
            collectionName = "The Lord of the Rings",
            readingLevel = ReadingLevel.ADULT,
            primaryLanguage = Language.ENGLISH,
            secondaryLanguages = listOf(Language.CATALAN, Language.SPANISH),
            primaryGenre = Genre.FANTASY,
            secondaryGenres = listOf(Genre.ADVENTURE, Genre.HISTORICAL_FICTION),
            basePrice = 29.99,
            vatRate = 0.04,
            finalPrice = Book.calculateFinalPrice(29.99, 0.04),
            isbn = "9780007141326",
            publicationDate = LocalDate.of(1954, 7, 29),
            pageCount = 1178,
            coverImagePath = "/images/lotr-cover.jpg",
            description = "An epic high fantasy novel written by J.R.R. Tolkien.",
            status = Status.PUBLISHED,
        )
}
