package org.cescfe.bookpublishing.book.objectMothers

import org.cescfe.bookpublishing.book.application.port.input.UpdateBookUseCase
import org.cescfe.bookpublishing.shared.domain.model.enum.Genre
import org.cescfe.bookpublishing.shared.domain.model.enum.Language
import org.cescfe.bookpublishing.shared.domain.model.enum.ReadingLevel
import java.time.LocalDate
import java.util.UUID

object UpdateBookCommandObjectMother {
    private val TEST_AUTHOR_ID = UUID.randomUUID()
    private val TEST_COLLECTION_ID = UUID.randomUUID()

    fun create(
        title: String = "Test Book",
        authorId: UUID = TEST_AUTHOR_ID,
        collectionId: UUID = TEST_COLLECTION_ID,
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
    ): UpdateBookUseCase.Command =
        UpdateBookUseCase.Command(
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
        )
}
