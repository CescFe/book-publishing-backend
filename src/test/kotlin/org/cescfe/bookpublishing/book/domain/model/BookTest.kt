package org.cescfe.bookpublishing.book.domain.model

import org.cescfe.bookpublishing.shared.domain.model.enum.Genre
import org.cescfe.bookpublishing.shared.domain.model.enum.Language
import org.cescfe.bookpublishing.shared.domain.model.enum.ReadingLevel
import org.cescfe.bookpublishing.book.domain.model.enum.Status
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals

class BookTest {
    @Test
    fun `should create book with valid data`() {
        // Given
        val id = BookId.generate()
        val title = BookTitle("The Invisible Life of Addie LaRue")
        val authorIds = AuthorIds(listOf(AuthorIdRef(UUID.randomUUID())))
        val collectionId = CollectionIdRef(UUID.randomUUID())
        val illustratorIds = IllustratorIds(listOf(IllustratorIdRef(UUID.randomUUID())))
        val readingLevel = ReadingLevel.ADULT
        val primaryLanguage = Language.ENGLISH
        val secondaryLanguages = BookSecondaryLanguages(listOf(Language.CATALAN, Language.SPANISH))
        val primaryGenre = Genre.FANTASY
        val secondaryGenres = BookSecondaryGenres(listOf(Genre.ADVENTURE, Genre.HISTORICAL_FICTION))
        val basePrice = BasePrice.fromDouble(19.95)
        val vatRate = VatRate.fromDouble(0.04)
        val isbn = ISBN("9780547928227")
        val publicationDate = PublicationDate.fromString("2020-10-06")
        val pageCount = PageCount(448)
        val coverImagePath = CoverImagePath("covers/addie-larue.jpg")
        val description = Description("A Life No One Will Remember. A Story You Will Never Forget.")
        val status = Status.PUBLISHED

        // When
        val book =
            Book(
                id = id,
                title = title,
                authorIds = authorIds,
                collectionId = collectionId,
                illustratorIds = illustratorIds,
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

        // Then
        assertEquals(id, book.id)
        assertEquals(title, book.title)
        assertEquals(authorIds, book.authorIds)
        assertEquals(collectionId, book.collectionId)
        assertEquals(illustratorIds, book.illustratorIds)
        assertEquals(readingLevel, book.readingLevel)
        assertEquals(primaryLanguage, book.primaryLanguage)
        assertEquals(secondaryLanguages, book.secondaryLanguages)
        assertEquals(primaryGenre, book.primaryGenre)
        assertEquals(secondaryGenres, book.secondaryGenres)
        assertEquals(basePrice, book.basePrice)
        assertEquals(vatRate, book.vatRate)
        assertEquals(isbn, book.isbn)
        assertEquals(publicationDate, book.publicationDate)
        assertEquals(pageCount, book.pageCount)
        assertEquals(coverImagePath, book.coverImagePath)
        assertEquals(description, book.description)
        assertEquals(status, book.status)
    }

    @Test
    fun `should create book with minimal required data`() {
        // Given
        val id = BookId.generate()
        val title = BookTitle("The Invisible Life of Addie LaRue")
        val authorIds = AuthorIds(listOf(AuthorIdRef(UUID.randomUUID())))
        val collectionId = CollectionIdRef(UUID.randomUUID())
        val basePrice = BasePrice.fromDouble(19.95)

        // When
        val book =
            Book(
                id = id,
                title = title,
                authorIds = authorIds,
                collectionId = collectionId,
                basePrice = basePrice,
            )

        // Then
        assertEquals(id, book.id)
        assertEquals(title, book.title)
        assertEquals(authorIds, book.authorIds)
        assertEquals(collectionId, book.collectionId)
        assertEquals(null, book.illustratorIds)
        assertEquals(null, book.readingLevel)
        assertEquals(null, book.primaryLanguage)
        assertEquals(null, book.secondaryLanguages)
        assertEquals(null, book.primaryGenre)
        assertEquals(null, book.secondaryGenres)
        assertEquals(basePrice, book.basePrice)
        assertEquals(null, book.vatRate)
        assertEquals(null, book.isbn)
        assertEquals(null, book.publicationDate)
        assertEquals(null, book.pageCount)
        assertEquals(null, book.coverImagePath)
        assertEquals(null, book.description)
        assertEquals(null, book.status)
    }

    @Test
    fun `should calculate final price with default VAT rate`() {
        // Given
        val book =
            Book(
                id = BookId.generate(),
                title = BookTitle("Test Book"),
                authorIds = AuthorIds(listOf(AuthorIdRef(UUID.randomUUID()))),
                collectionId = CollectionIdRef(UUID.randomUUID()),
                basePrice = BasePrice.fromDouble(100.0),
                vatRate = null,
            )

        // When
        val finalPrice = book.calculateFinalPrice()

        // Then
        assertEquals(104.0, finalPrice)
    }

    @Test
    fun `should calculate final price with custom VAT rate`() {
        // Given
        val book =
            Book(
                id = BookId.generate(),
                title = BookTitle("Test Book"),
                authorIds = AuthorIds(listOf(AuthorIdRef(UUID.randomUUID()))),
                collectionId = CollectionIdRef(UUID.randomUUID()),
                basePrice = BasePrice.fromDouble(100.0),
                vatRate = VatRate.fromDouble(0.21),
            )

        // When
        val finalPrice = book.calculateFinalPrice()

        // Then
        assertEquals(121.0, finalPrice)
    }

    @Test
    fun `should round final price to two decimal places`() {
        // Given
        val book =
            Book(
                id = BookId.generate(),
                title = BookTitle("Test Book"),
                authorIds = AuthorIds(listOf(AuthorIdRef(UUID.randomUUID()))),
                collectionId = CollectionIdRef(UUID.randomUUID()),
                basePrice = BasePrice.fromDouble(19.99),
                vatRate = VatRate.fromDouble(0.04),
            )

        // When
        val finalPrice = book.calculateFinalPrice()

        // Then
        assertEquals(20.79, finalPrice)
    }
}
