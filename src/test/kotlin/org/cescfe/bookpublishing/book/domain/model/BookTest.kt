package org.cescfe.bookpublishing.book.domain.model

import org.cescfe.bookpublishing.book.domain.model.enum.Status
import org.cescfe.bookpublishing.shared.domain.model.enum.Genre
import org.cescfe.bookpublishing.shared.domain.model.enum.Language
import org.cescfe.bookpublishing.shared.domain.model.enum.ReadingLevel
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNull
import java.util.UUID
import kotlin.test.assertEquals

class BookTest {
    @Test
    fun `should create book with valid data`() {
        // Given
        val id = BookId.generate()
        val title = BookTitle("The Invisible Life of Addie LaRue")
        val authorId = AuthorIdRef(UUID.randomUUID())
        val authorName = "V. E. Schwab"
        val collectionId = CollectionIdRef(UUID.randomUUID())
        val collectionName = "Fantasy Classics"
        val readingLevel = ReadingLevel.ADULT
        val primaryLanguage = Language.ENGLISH
        val secondaryLanguages = SecondaryLanguages(listOf(Language.CATALAN, Language.SPANISH))
        val primaryGenre = Genre.FANTASY
        val secondaryGenres = SecondaryGenres(listOf(Genre.ADVENTURE, Genre.HISTORICAL_FICTION))
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
                authorId = authorId,
                authorName = authorName,
                collectionId = collectionId,
                collectionName = collectionName,
                readingLevel = readingLevel,
                primaryLanguage = primaryLanguage,
                secondaryLanguages = secondaryLanguages,
                primaryGenre = primaryGenre,
                secondaryGenres = secondaryGenres,
                basePrice = basePrice,
                vatRate = vatRate,
                finalPrice = Book.calculateFinalPrice(basePrice.value, vatRate.value),
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
        assertEquals(authorId, book.authorId)
        assertEquals(authorName, book.authorName)
        assertEquals(collectionId, book.collectionId)
        assertEquals(collectionName, book.collectionName)
        assertEquals(readingLevel, book.readingLevel)
        assertEquals(primaryLanguage, book.primaryLanguage)
        assertEquals(secondaryLanguages, book.secondaryLanguages)
        assertEquals(primaryGenre, book.primaryGenre)
        assertEquals(secondaryGenres, book.secondaryGenres)
        assertEquals(basePrice, book.basePrice)
        assertEquals(vatRate, book.vatRate)
        assertEquals(20.75, book.finalPrice)
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
        val authorId = AuthorIdRef(UUID.randomUUID())
        val collectionId = CollectionIdRef(UUID.randomUUID())
        val basePrice = BasePrice.fromDouble(19.95)

        // When
        val book =
            Book(
                id = id,
                title = title,
                authorId = authorId,
                collectionId = collectionId,
                basePrice = basePrice,
                finalPrice = Book.calculateFinalPrice(basePrice.value, null),
            )

        // Then
        assertEquals(id, book.id)
        assertEquals(title, book.title)
        assertEquals(authorId, book.authorId)
        assertEquals(collectionId, book.collectionId)
        assertNull(book.readingLevel)
        assertNull(book.primaryLanguage)
        assertNull(book.secondaryLanguages)
        assertNull(book.primaryGenre)
        assertNull(book.secondaryGenres)
        assertEquals(basePrice, book.basePrice)
        assertNull(book.vatRate)
        assertEquals(20.75, book.finalPrice)
        assertNull(book.isbn)
        assertNull(book.publicationDate)
        assertNull(book.pageCount)
        assertNull(book.coverImagePath)
        assertNull(book.description)
        assertNull(book.status)
        assertNull(book.authorName)
        assertNull(book.collectionName)
    }

    @Test
    fun `should calculate final price with default VAT rate`() {
        // Given
        val book =
            Book(
                id = BookId.generate(),
                title = BookTitle("Test Book"),
                authorId = AuthorIdRef(UUID.randomUUID()),
                collectionId = CollectionIdRef(UUID.randomUUID()),
                basePrice = BasePrice.fromDouble(100.0),
                vatRate = null,
                finalPrice = Book.calculateFinalPrice(100.0, null),
            )

        // When
        val finalPrice = book.finalPrice

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
                authorId = AuthorIdRef(UUID.randomUUID()),
                collectionId = CollectionIdRef(UUID.randomUUID()),
                basePrice = BasePrice.fromDouble(100.0),
                vatRate = VatRate.fromDouble(0.21),
                finalPrice = Book.calculateFinalPrice(100.0, 0.21),
            )

        // When
        val finalPrice = book.finalPrice

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
                authorId = AuthorIdRef(UUID.randomUUID()),
                collectionId = CollectionIdRef(UUID.randomUUID()),
                basePrice = BasePrice.fromDouble(19.99),
                vatRate = VatRate.fromDouble(0.04),
                finalPrice = Book.calculateFinalPrice(19.99, 0.04),
            )

        // When
        val finalPrice = book.finalPrice

        // Then
        assertEquals(20.79, finalPrice)
    }
}
