package org.cescfe.bookpublishing.book.infrastructure.adapters.output.persistence.mapper

import org.cescfe.bookpublishing.book.domain.model.enum.Status
import org.cescfe.bookpublishing.book.infrastructure.adapters.output.persistence.projection.BookSummaryProjection
import org.cescfe.bookpublishing.book.infrastructure.adapters.output.persistence.projection.BookWithRelationsProjection
import org.cescfe.bookpublishing.book.objectMothers.BookEntityObjectMother
import org.cescfe.bookpublishing.book.objectMothers.BookObjectMother
import org.cescfe.bookpublishing.shared.domain.model.enum.Genre
import org.cescfe.bookpublishing.shared.domain.model.enum.Language
import org.cescfe.bookpublishing.shared.domain.model.enum.ReadingLevel
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID
import java.util.UUID.randomUUID
import kotlin.test.assertEquals

class BookPersistenceMapperTest {
    private val bookMapper = BookPersistenceMapper()

    @Test
    fun `should map book domain to entity correctly`() {
        // Given
        val book = BookObjectMother.createWithAllFields()

        // When
        val result = bookMapper.fromDomain(book)

        // Then
        assertEquals(book.id.value, result.id)
        assertEquals(book.title.value, result.title)
        assertEquals(book.authorId.value, result.authorId)
        assertEquals(book.collectionId.value, result.collectionId)
        assertEquals(book.readingLevel, result.readingLevel)
        assertEquals(book.primaryLanguage, result.primaryLanguage)
        assertEquals(
            book.secondaryLanguages!!.value,
            result.secondaryLanguages,
        )
        assertEquals(book.primaryGenre, result.primaryGenre)
        assertEquals(
            book.secondaryGenres!!.value,
            result.secondaryGenres,
        )
        assertEquals(book.basePrice.value, result.basePrice)
        assertEquals(book.vatRate!!.value, result.vatRate)
        assertEquals(book.isbn!!.value, result.isbn)
        assertEquals(book.publicationDate!!.value, result.publicationDate)
        assertEquals(book.pageCount!!.value, result.pageCount)
        assertEquals(book.coverImagePath!!.value, result.coverImagePath)
        assertEquals(book.description!!.value, result.description)
        assertEquals(book.status, result.status)
    }

    @Test
    fun `should map book with minimal data correctly`() {
        // Given
        val book = BookObjectMother.createMinimal()

        // When
        val result = bookMapper.fromDomain(book)

        // Then
        assertEquals(book.id.value, result.id)
        assertEquals(book.title.value, result.title)
        assertEquals(book.authorId.value, result.authorId)
        assertEquals(book.collectionId.value, result.collectionId)
        assertEquals(book.basePrice.value, result.basePrice)
        assertEquals(book.status, result.status)
    }

    @Test
    fun `should map entity to book domain correctly`() {
        // Given
        val entity = BookEntityObjectMother.createWithAllFields()

        // When
        val result = bookMapper.toDomain(entity)

        // Then
        assertEquals(entity.id, result.id.value)
        assertEquals(entity.title, result.title.value)
        assertEquals(entity.authorId, result.authorId.value)
        assertEquals(entity.collectionId, result.collectionId.value)
        assertEquals(entity.readingLevel, result.readingLevel)
        assertEquals(entity.primaryLanguage, result.primaryLanguage)
        assertEquals(
            entity.secondaryLanguages,
            result.secondaryLanguages!!.value,
        )
        assertEquals(entity.primaryGenre, result.primaryGenre)
        assertEquals(
            entity.secondaryGenres,
            result.secondaryGenres!!.value,
        )
        assertEquals(entity.basePrice, result.basePrice.value)
        assertEquals(entity.vatRate, result.vatRate!!.value)
        assertEquals(entity.isbn, result.isbn!!.value)
        assertEquals(entity.publicationDate, result.publicationDate!!.value)
        assertEquals(entity.pageCount, result.pageCount!!.value)
        assertEquals(entity.coverImagePath, result.coverImagePath!!.value)
        assertEquals(entity.description, result.description!!.value)
        assertEquals(entity.status, result.status)
        assertEquals(entity.createdAt, result.audit?.createdAt)
        assertEquals(entity.createdBy, result.audit?.createdBy)
        assertEquals(entity.updatedAt, result.audit?.updatedAt)
        assertEquals(entity.updatedBy, result.audit?.updatedBy)
    }

    @Test
    fun `should map entity with minimal data to book domain correctly`() {
        // Given
        val entity = BookEntityObjectMother.createMinimal()

        // When
        val result = bookMapper.toDomain(entity)

        // Then
        assertEquals(entity.id, result.id.value)
        assertEquals(entity.title, result.title.value)
        assertEquals(entity.authorId, result.authorId.value)
        assertEquals(entity.collectionId, result.collectionId.value)
        assertEquals(entity.basePrice, result.basePrice.value)
        assertEquals(entity.status, result.status)
    }

    @Test
    fun `should map projection to book summary correctly`() {
        // Given
        val projection =
            object : BookSummaryProjection {
                override val id: UUID = randomUUID()
                override val title: String = "Test Book"
                override val authorId: UUID = randomUUID()
                override val authorName: String = "Test Author"
                override val collectionId: UUID = randomUUID()
                override val collectionName: String = "Test Collection"
                override val basePrice: Double = 19.99
                override val finalPrice: Double = 20.79
                override val isbn: String? = null
                override val status = Status.PUBLISHED
            }

        // When
        val result = bookMapper.toDomainSummaryWithRelations(projection)

        // Then
        assertEquals(projection.id, result.id.value)
        assertEquals(projection.title, result.title.value)
        assertEquals(projection.authorId, result.authorId.value)
        assertEquals(projection.authorName, result.authorName)
        assertEquals(projection.collectionId, result.collectionId.value)
        assertEquals(projection.collectionName, result.collectionName)
        assertEquals(projection.basePrice, result.basePrice.value)
        assertEquals(projection.finalPrice, result.finalPrice)
        assertEquals(projection.status, result.status)
    }

    @Test
    fun `should map projection with relations to book domain correctly`() {
        // Given
        val projection =
            object : BookWithRelationsProjection {
                override val id: UUID = randomUUID()
                override val title: String = "Test Book With Relations"
                override val authorId: UUID = randomUUID()
                override val authorName: String = "Miguel de Cervantes"
                override val collectionId: UUID = randomUUID()
                override val collectionName: String = "Clásicos Españoles"
                override val basePrice: Double = 19.99
                override val vatRate: Double = 0.04
                override val finalPrice: Double = 20.79
                override val isbn: String = "9781234567890"
                override val publicationDate: LocalDate = LocalDate.of(2024, 1, 15)
                override val pageCount: Int = 350
                override val coverImagePath: String = "/covers/test.jpg"
                override val description: String = "Test description"
                override val readingLevel: String = "ADULT"
                override val primaryLanguage: String = "SPANISH"
                override val secondaryLanguages: String = "[\"ENGLISH\", \"CATALAN\"]"
                override val primaryGenre: String = "FICTION"
                override val secondaryGenres: String = "[\"ADVENTURE\"]"
                override val status: String = "PUBLISHED"
                override val createdAt: LocalDateTime = LocalDateTime.now()
                override val createdBy: String = "test-user"
                override val updatedAt: LocalDateTime = LocalDateTime.now()
                override val updatedBy: String = "test-user"
            }

        // When
        val result = bookMapper.toDomainWithRelations(projection)

        // Then
        assertEquals(projection.id, result.id.value)
        assertEquals(projection.title, result.title.value)
        assertEquals(projection.authorId, result.authorId.value)
        assertEquals(projection.authorName, result.authorName)
        assertEquals(projection.collectionId, result.collectionId.value)
        assertEquals(projection.collectionName, result.collectionName)
        assertEquals(projection.basePrice, result.basePrice.value)
        assertEquals(projection.vatRate, result.vatRate?.value)
        assertEquals(projection.finalPrice, result.finalPrice)
        assertEquals(projection.isbn, result.isbn?.value)
        assertEquals(projection.publicationDate, result.publicationDate?.value)
        assertEquals(projection.pageCount, result.pageCount?.value)
        assertEquals(projection.coverImagePath, result.coverImagePath?.value)
        assertEquals(projection.description, result.description?.value)
        assertEquals(ReadingLevel.ADULT, result.readingLevel)
        assertEquals(Language.SPANISH, result.primaryLanguage)
        assertEquals(listOf(Language.ENGLISH, Language.CATALAN), result.secondaryLanguages?.value)
        assertEquals(Genre.FICTION, result.primaryGenre)
        assertEquals(listOf(Genre.ADVENTURE), result.secondaryGenres?.value)
        assertEquals(Status.PUBLISHED, result.status)
        assertEquals(projection.createdAt, result.audit?.createdAt)
        assertEquals(projection.createdBy, result.audit?.createdBy)
    }
}
