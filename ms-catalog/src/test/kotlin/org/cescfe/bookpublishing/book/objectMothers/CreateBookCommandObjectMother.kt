package org.cescfe.bookpublishing.book.objectMothers

import org.cescfe.bookpublishing.book.application.port.input.CreateBookUseCase
import org.cescfe.bookpublishing.book.domain.model.enum.Status
import org.cescfe.bookpublishing.shared.domain.model.enum.Genre
import org.cescfe.bookpublishing.shared.domain.model.enum.Language
import org.cescfe.bookpublishing.shared.domain.model.enum.ReadingLevel
import java.time.LocalDate
import java.util.UUID

object CreateBookCommandObjectMother {
    fun create(
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
    ): CreateBookUseCase.Command =
        CreateBookUseCase.Command(
            title = title,
            authorId = authorId,
            collectionId = collectionId,
            readingLevel = readingLevel,
            primaryLanguage = primaryLanguage,
            secondaryLanguages = secondaryLanguages,
            primaryGenre = primaryGenre,
            secondaryGenres = secondaryGenres,
            basePrice = basePrice,
            vatRate = vatRate,
            isbn = isbn,
            publicationDate = publicationDate,
            pageCount = pageCount,
            coverImagePath = coverImagePath,
            description = description,
            status = status,
        )

    fun createWithAllFields(
        authorId: UUID = UUID.randomUUID(),
        collectionId: UUID = UUID.randomUUID(),
    ): CreateBookUseCase.Command =
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

    fun createMinimal(): CreateBookUseCase.Command =
        create(
            title = "Minimal Book",
            basePrice = 9.99,
        )

    fun createWithIsbn(isbn: String?): CreateBookUseCase.Command =
        create(
            title = "Test Book with ISBN",
            isbn = isbn,
        )
}
