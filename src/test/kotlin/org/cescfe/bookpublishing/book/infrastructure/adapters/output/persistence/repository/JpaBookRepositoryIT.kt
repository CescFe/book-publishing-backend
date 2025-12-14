package org.cescfe.bookpublishing.book.infrastructure.adapters.output.persistence.repository

import org.cescfe.bookpublishing.author.infrastructure.adapters.output.persistence.repository.AuthorJpaEntityRepository
import org.cescfe.bookpublishing.author.objectMothers.AuthorEntityObjectMother
import org.cescfe.bookpublishing.book.domain.model.BookId
import org.cescfe.bookpublishing.book.infrastructure.adapters.output.persistence.repository.config.JpaBookRepositoryTestConfig
import org.cescfe.bookpublishing.book.objectMothers.BookObjectMother
import org.cescfe.bookpublishing.collection.infrastructure.adapters.output.persistence.repository.CollectionJpaEntityRepository
import org.cescfe.bookpublishing.collection.objectMothers.CollectionEntityObjectMother
import org.cescfe.bookpublishing.shared.infrastructure.adapters.output.persistence.config.TestJpaAuditingConfig
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.context.annotation.Import
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql
import org.springframework.transaction.annotation.Transactional
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@DataJpaTest
@Transactional
@Testcontainers
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(
    TestJpaAuditingConfig::class,
    JpaBookRepositoryTestConfig::class,
)
class JpaBookRepositoryIT {
    @Autowired
    private lateinit var bookJpaEntityRepository: BookJpaEntityRepository

    @Autowired
    private lateinit var authorJpaEntityRepository: AuthorJpaEntityRepository

    @Autowired
    private lateinit var collectionJpaEntityRepository: CollectionJpaEntityRepository

    @Autowired
    private lateinit var testEntityManager: TestEntityManager

    @Autowired
    private lateinit var jpaBookRepository: JpaBookRepository

    companion object {
        private const val BOOK_ID = "00000000-0000-0000-0000-000000000030"

        @Container
        @ServiceConnection
        @JvmStatic
        val postgres: PostgreSQLContainer<*> =
            PostgreSQLContainer(DockerImageName.parse("postgres:16"))
                .withDatabaseName("testdb")
                .withUsername("test")
                .withPassword("test")
    }

    @Test
    fun `should save and find book by id`() {
        // Given
        val author = AuthorEntityObjectMother.createSimple()
        val savedAuthor = authorJpaEntityRepository.save(author)
        testEntityManager.flush()

        val collection = CollectionEntityObjectMother.createMinimal()
        val savedCollection = collectionJpaEntityRepository.save(collection)
        testEntityManager.flush()

        val book =
            BookObjectMother.createWithAllFields(
                authorId = savedAuthor.id,
                collectionId = savedCollection.id,
            )

        // When
        val savedBook = jpaBookRepository.save(book)
        val foundBook = jpaBookRepository.findById(book.id)

        // Then
        assertNotNull(savedBook)
        assertNotNull(foundBook)

        assertEquals(book.id.value, foundBook.id.value)
        assertEquals(book.title.value, foundBook.title.value)
        assertEquals(book.authorId.value, foundBook.authorId.value)
        assertEquals(book.collectionId.value, foundBook.collectionId.value)
        assertEquals(book.basePrice.value, foundBook.basePrice.value)
        assertEquals(book.vatRate!!.value, foundBook.vatRate!!.value)
        assertEquals(book.finalPrice, foundBook.finalPrice)
        assertEquals(book.isbn!!.value, foundBook.isbn!!.value)
        assertEquals(book.publicationDate!!.value, foundBook.publicationDate!!.value)
        assertEquals(book.pageCount!!.value, foundBook.pageCount!!.value)
        assertEquals(book.coverImagePath!!.value, foundBook.coverImagePath!!.value)
        assertEquals(book.description!!.value, foundBook.description!!.value)
        assertEquals(book.readingLevel, foundBook.readingLevel)
        assertEquals(book.primaryLanguage, foundBook.primaryLanguage)
        assertEquals(book.secondaryLanguages!!.value, foundBook.secondaryLanguages!!.value)
        assertEquals(book.primaryGenre, foundBook.primaryGenre)
        assertEquals(book.secondaryGenres!!.value, foundBook.secondaryGenres!!.value)
        assertEquals(book.status, foundBook.status)

        assertNotNull(foundBook.audit)
        assertNotNull(foundBook.audit.createdAt)
        assertNotNull(foundBook.audit.createdBy)
        assertNotNull(foundBook.audit.updatedAt)
        assertNotNull(foundBook.audit.updatedBy)
    }

    @Test
    fun `should find all books`() {
        // Given
        val author = AuthorEntityObjectMother.createSimple()
        val savedAuthor = authorJpaEntityRepository.save(author)
        testEntityManager.flush()

        val collection = CollectionEntityObjectMother.createMinimal()
        val savedCollection = collectionJpaEntityRepository.save(collection)
        testEntityManager.flush()

        val book1 =
            BookObjectMother.create(
                title = "Book One",
                authorId = savedAuthor.id,
                collectionId = savedCollection.id,
            )
        val book2 =
            BookObjectMother.create(
                title = "Book Two",
                authorId = savedAuthor.id,
                collectionId = savedCollection.id,
            )

        jpaBookRepository.save(book1)
        jpaBookRepository.save(book2)

        // When
        val books = jpaBookRepository.findAllSummary()

        // Then
        assertEquals(2, books.size)
        assertTrue(books.any { it.title.value == "Book One" })
        assertTrue(books.any { it.title.value == "Book Two" })
    }

    @Test
    fun `should find all books with author and collection names`() {
        // Given
        val author = AuthorEntityObjectMother.createSimple()
        val savedAuthor = authorJpaEntityRepository.save(author)
        testEntityManager.flush()

        val collection = CollectionEntityObjectMother.createMinimal()
        val savedCollection = collectionJpaEntityRepository.save(collection)
        testEntityManager.flush()

        val book = BookObjectMother.create(
            title = "Test Book",
            authorId = savedAuthor.id,
            collectionId = savedCollection.id,
        )
        jpaBookRepository.save(book)
        testEntityManager.flush()

        // When
        val summaries = bookJpaEntityRepository.findAllProjectedByOrderByTitleAsc()

        // Then
        assertEquals(1, summaries.size)
        val summary = summaries.first()

        assertEquals(book.title.value, summary.title)
        assertEquals(savedAuthor.id, summary.authorId)
        assertEquals(savedAuthor.fullName, summary.authorName)
        assertEquals(savedCollection.id, summary.collectionId)
        assertEquals(savedCollection.name, summary.collectionName)
    }

    @Test
    fun `should find paginated books with author and collection names`() {
        // Given
        val author = AuthorEntityObjectMother.createSimple()
        val savedAuthor = authorJpaEntityRepository.save(author)
        testEntityManager.flush()

        val collection = CollectionEntityObjectMother.createMinimal()
        val savedCollection = collectionJpaEntityRepository.save(collection)
        testEntityManager.flush()

        val book1 = BookObjectMother.create(
            title = "Alpha Book",
            authorId = savedAuthor.id,
            collectionId = savedCollection.id,
        )
        val book2 = BookObjectMother.create(
            title = "Beta Book",
            authorId = savedAuthor.id,
            collectionId = savedCollection.id,
        )
        jpaBookRepository.save(book1)
        jpaBookRepository.save(book2)
        testEntityManager.flush()

        // When
        val pageable = PageRequest.of(0, 10)
        val summaries = bookJpaEntityRepository.findAllProjectedByOrderByTitleAsc(pageable)

        // Then
        assertEquals(2, summaries.size)

        assertEquals("Alpha Book", summaries[0].title)
        assertEquals("Beta Book", summaries[1].title)

        summaries.forEach { summary ->
            assertEquals(savedAuthor.fullName, summary.authorName)
            assertEquals(savedCollection.name, summary.collectionName)
        }
    }

    @Test
    fun `should return null when book not found`() {
        // Given
        val nonExistentId = BookId.generate()

        // When
        val foundBook = jpaBookRepository.findById(nonExistentId)

        // Then
        assertNull(foundBook)
    }

    @Test
    @Sql("/datasets/books.sql")
    fun `should load book from SQL script`() {
        // When
        val found =
            jpaBookRepository.findById(
                BookId.fromString(BOOK_ID),
            )

        // Then
        assertNotNull(found)
        assertEquals(UUID.fromString(BOOK_ID), found.id.value)
        assertEquals("SQL Inserted Book", found.title.value)
        assertEquals(
            UUID.fromString("00000000-0000-0000-0000-000000000010"),
            found.authorId.value,
        )
        assertEquals(
            UUID.fromString("00000000-0000-0000-0000-000000000020"),
            found.collectionId.value,
        )
        assertEquals(19.99, found.basePrice.value)
        assertEquals(0.04, found.vatRate!!.value)
        assertEquals(20.79, found.finalPrice)
        assertEquals("9784567890123", found.isbn!!.value)
        assertEquals("2024-01-01", found.publicationDate!!.value.toString())
        assertEquals(350, found.pageCount!!.value)
        assertEquals("/covers/sql-book.jpg", found.coverImagePath!!.value)
        assertEquals("SQL Book Description", found.description!!.value)
        assertEquals("ADULT", found.readingLevel!!.name)
        assertEquals("ENGLISH", found.primaryLanguage!!.name)
        assertEquals("[SPANISH, CATALAN]", found.secondaryLanguages!!.value.toString())
        assertEquals("FANTASY", found.primaryGenre!!.name)
        assertEquals("[ADVENTURE]", found.secondaryGenres!!.value.toString())
        assertEquals("PUBLISHED", found.status!!.name)
    }

    @Test
    @Sql("/datasets/books.sql")
    fun `should validate audit fields from sql dataset`() {
        // When
        val entity =
            bookJpaEntityRepository
                .findById(
                    UUID.fromString(BOOK_ID),
                ).orElse(null)

        // Then
        assertNotNull(entity)
        assertNotNull(entity.createdAt)
        assertNotNull(entity.updatedAt)
        assertEquals("test-user", entity.createdBy)
        assertEquals("test-user", entity.updatedBy)
        assertTrue(entity.createdAt!! <= entity.updatedAt!!)
    }
}
