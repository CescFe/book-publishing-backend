package org.cescfe.bookpublishing.book.infrastructure.adapters.output.persistence.repository

import org.cescfe.bookpublishing.author.infrastructure.adapters.output.persistence.repository.AuthorJpaEntityRepository
import org.cescfe.bookpublishing.author.objectMothers.AuthorEntityObjectMother
import org.cescfe.bookpublishing.book.domain.model.BookId
import org.cescfe.bookpublishing.book.infrastructure.adapters.output.persistence.repository.config.JpaBookRepositoryTestConfig
import org.cescfe.bookpublishing.book.objectMothers.BookObjectMother
import org.cescfe.bookpublishing.collection.infrastructure.adapters.output.persistence.repository.CollectionJpaEntityRepository
import org.cescfe.bookpublishing.collection.objectMothers.CollectionEntityObjectMother
import org.cescfe.bookpublishing.shared.infrastructure.adapters.output.persistence.config.TestJpaAuditingConfig
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@DataJpaTest
@Testcontainers
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(
    TestJpaAuditingConfig::class,
    JpaBookRepositoryTestConfig::class,
)
class JpaBookCollectionRepositoryIT {
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
        @Container
        @ServiceConnection
        @JvmStatic
        val postgres: PostgreSQLContainer<*> =
            PostgreSQLContainer(DockerImageName.parse("postgres:16"))
                .withDatabaseName("testdb")
                .withUsername("test")
                .withPassword("test")
    }

    @AfterEach
    fun cleanUp() {
        bookJpaEntityRepository.deleteAll()
        collectionJpaEntityRepository.deleteAll()
        authorJpaEntityRepository.deleteAll()
        testEntityManager.flush()
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
        assertEquals(book, foundBook)
        assertEquals(savedBook, foundBook)
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
    fun `should return null when book not found`() {
        // Given
        val nonExistentId = BookId.generate()

        // When
        val foundBook = jpaBookRepository.findById(nonExistentId)

        // Then
        assertNull(foundBook)
    }
}
