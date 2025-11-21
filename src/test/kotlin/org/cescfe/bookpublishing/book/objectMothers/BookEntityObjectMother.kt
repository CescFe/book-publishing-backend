package org.cescfe.bookpublishing.book.objectMothers

import org.cescfe.bookpublishing.book.domain.model.enum.Status
import org.cescfe.bookpublishing.book.infrastructure.adapters.output.persistence.entity.BookEntity
import org.cescfe.bookpublishing.shared.domain.model.enum.Genre
import org.cescfe.bookpublishing.shared.domain.model.enum.Language
import org.cescfe.bookpublishing.shared.domain.model.enum.ReadingLevel
import java.time.LocalDate
import java.util.UUID

object BookEntityObjectMother {
    fun create(
        id: UUID = UUID.randomUUID(),
        title: String = "Test Book",
        authorId: UUID = UUID.randomUUID(),
        collectionId: UUID = UUID.randomUUID(),
        basePrice: Double = 19.99,
        vatRate: Double? = null,
        isbn: String? = null,
        publicationDate: LocalDate? = null,
        pageCount: Int? = null,
        coverImagePath: String? = null,
        description: String? = null,
        readingLevel: ReadingLevel? = null,
        primaryLanguage: Language? = null,
        secondaryLanguages: List<Language>? = null,
        primaryGenre: Genre? = null,
        secondaryGenres: List<Genre>? = null,
        status: Status? = null,
    ): BookEntity {
        val entity =
            BookEntity(
                id = id,
                title = title,
                authorId = authorId,
                collectionId = collectionId,
                basePrice = basePrice,
                vatRate = vatRate,
                isbn = isbn,
                publicationDate = publicationDate,
                pageCount = pageCount,
                coverImagePath = coverImagePath,
                description = description,
                readingLevel = readingLevel,
                primaryLanguage = primaryLanguage,
                secondaryLanguages = secondaryLanguages,
                primaryGenre = primaryGenre,
                secondaryGenres = secondaryGenres,
                status = status,
            )
        return entity
    }

    fun createWithAllFields(): BookEntity =
        create(
            title = "The Lord of the Rings",
            basePrice = 29.99,
            vatRate = 0.04,
            isbn = "9780007141326",
            publicationDate = LocalDate.of(1954, 7, 29),
            pageCount = 1178,
            coverImagePath = "/images/lotr-cover.jpg",
            description = "An epic high fantasy novel written by J.R.R. Tolkien.",
            readingLevel = ReadingLevel.ADULT,
            primaryLanguage = Language.ENGLISH,
            secondaryLanguages = listOf(Language.CATALAN, Language.SPANISH),
            primaryGenre = Genre.FANTASY,
            secondaryGenres = listOf(Genre.ADVENTURE, Genre.HISTORICAL_FICTION),
            status = Status.PUBLISHED,
        )

    fun createMinimal(): BookEntity =
        create(
            title = "Simple Book",
            basePrice = 9.99,
        )
}
