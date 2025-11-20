package org.cescfe.bookpublishing.author.infrastructure.adapters.output.persistence.repository

import org.cescfe.bookpublishing.author.domain.model.AuthorId
import org.cescfe.bookpublishing.author.infrastructure.adapters.output.persistence.repository.config.JpaAuthorRepositoryTestConfig
import org.cescfe.bookpublishing.author.objectMothers.AuthorObjectMother
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
import org.springframework.test.context.jdbc.Sql
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@DataJpaTest
@Testcontainers
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(
    TestJpaAuditingConfig::class,
    JpaAuthorRepositoryTestConfig::class,
)
class JpaAuthorRepositoryIT {
    @Autowired
    private lateinit var authorJpaEntityRepository: AuthorJpaEntityRepository

    @Autowired
    private lateinit var testEntityManager: TestEntityManager

    @Autowired
    private lateinit var jpaAuthorRepository: JpaAuthorRepository

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
        authorJpaEntityRepository.deleteAll()
        testEntityManager.flush()
    }

    @Test
    fun `should save and find author by id`() {
        // Given
        val author = AuthorObjectMother.createTolkien()

        // When
        val savedAuthor = jpaAuthorRepository.save(author)
        val foundAuthor = jpaAuthorRepository.findById(author.id)

        // Then
        assertNotNull(savedAuthor)
        assertNotNull(foundAuthor)
        assertEquals(author, foundAuthor)
        assertEquals(savedAuthor, foundAuthor)
    }

    @Test
    fun `should find all authors`() {
        // Given
        val author1 =
            AuthorObjectMother.create(
                fullName = "Author One",
            )
        val author2 =
            AuthorObjectMother.create(
                fullName = "Author Two",
            )

        jpaAuthorRepository.save(author1)
        jpaAuthorRepository.save(author2)

        // When
        val authors = jpaAuthorRepository.findAllSummary()

        // Then
        assertEquals(2, authors.size)
        assertTrue(authors.any { it.fullName.value == "Author One" })
        assertTrue(authors.any { it.fullName.value == "Author Two" })
    }

    @Test
    fun `should return paginated authors`() {
        // Given
        jpaAuthorRepository.save(AuthorObjectMother.create(fullName = "A"))
        jpaAuthorRepository.save(AuthorObjectMother.create(fullName = "B"))
        jpaAuthorRepository.save(AuthorObjectMother.create(fullName = "C"))

        // When
        val firstPage = jpaAuthorRepository.findAllSummary(page = 1, limit = 2)
        val secondPage = jpaAuthorRepository.findAllSummary(page = 2, limit = 2)

        // Then
        assertEquals(2, firstPage.size)
        assertEquals(1, secondPage.size)

        assertEquals("A", firstPage[0].fullName.value)
        assertEquals("B", firstPage[1].fullName.value)
        assertEquals("C", secondPage[0].fullName.value)
    }

    @Test
    fun `should find author by email`() {
        // Given
        val author = AuthorObjectMother.create(email = "test@example.com")
        jpaAuthorRepository.save(author)

        // When
        val found = jpaAuthorRepository.findByEmail("test@example.com")

        // Then
        assertNotNull(found)
        assertEquals(author, found)
    }

    @Test
    fun `should return null when email does not exist`() {
        // When / Then
        assertNull(jpaAuthorRepository.findByEmail("missing@example.com"))
    }

    @Test
    fun `should check if author exists by email`() {
        // Given
        val author = AuthorObjectMother.create(email = "email@domain.com")
        jpaAuthorRepository.save(author)

        // When / Then
        assertTrue(jpaAuthorRepository.existsByEmail("email@domain.com"))
        assertFalse(jpaAuthorRepository.existsByEmail("other@domain.com"))
    }

    @Test
    fun `should return null when author not found`() {
        // Given
        val nonExistentId = AuthorId.generate()

        // When
        val foundAuthor = jpaAuthorRepository.findById(nonExistentId)

        // Then
        assertNull(foundAuthor)
    }

    @Test
    fun `should delete author by id`() {
        // Given
        val author = AuthorObjectMother.createTolkien()
        jpaAuthorRepository.save(author)

        assertTrue(jpaAuthorRepository.existsById(author.id))

        // When
        jpaAuthorRepository.deleteById(author.id)

        // Then
        assertFalse(jpaAuthorRepository.existsById(author.id))
        assertNull(jpaAuthorRepository.findById(author.id))
    }

    @Test
    @Sql("/datasets/authors.sql")
    fun `should load author inserted via SQL script`() {
        val id = AuthorId.fromString("00000000-0000-0000-0000-000000000001")

        val found = jpaAuthorRepository.findById(id)

        assertNotNull(found)
        assertEquals("00000000-0000-0000-0000-000000000001", found.id.value.toString())
        assertEquals("SQL Inserted Author", found.fullName.value)
        assertEquals("SQL Pseudonym", found.pseudonym!!.value)
        assertEquals("SQL Biography", found.biography!!.value)
        assertEquals("author@example.com", found.email!!.value)
        assertEquals("https://www.example.com", found.website!!.value)
    }
}
