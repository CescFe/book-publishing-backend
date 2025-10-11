package org.cescfe.bookpublishing.author.infrastructure.adapters.output.persistence.repository

import org.cescfe.bookpublishing.author.domain.model.Author
import org.cescfe.bookpublishing.author.domain.model.AuthorId
import org.cescfe.bookpublishing.author.domain.model.AuthorRole
import org.cescfe.bookpublishing.author.domain.model.Biography
import org.cescfe.bookpublishing.author.domain.model.Email
import org.cescfe.bookpublishing.author.domain.model.FullName
import org.cescfe.bookpublishing.author.domain.model.Pseudonym
import org.cescfe.bookpublishing.author.domain.model.Website
import org.cescfe.bookpublishing.author.infrastructure.adapters.output.persistence.repository.config.JpaAuthorRepositoryTestConfig
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
    JpaAuthorRepositoryTestConfig::class
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
        val author =
            Author(
                id = AuthorId.generate(),
                fullName = FullName("J.R.R. Tolkien"),
                roles = setOf(AuthorRole.AUTHOR),
                pseudonym = Pseudonym("Tolkien"),
                biography = Biography("English writer and philologist"),
                email = Email("tolkien@example.com"),
                website = Website("https://www.tolkiensociety.org"),
            )

        // When
        val savedAuthor = jpaAuthorRepository.save(author)
        val foundAuthor = jpaAuthorRepository.findById(author.id)

        // Then
        assertNotNull(savedAuthor)
        assertEquals(author.id.value, savedAuthor.id.value)
        assertEquals(author.fullName.value, savedAuthor.fullName.value)
        assertEquals(author.roles, savedAuthor.roles)
        assertEquals(author.pseudonym!!.value, savedAuthor.pseudonym!!.value)
        assertEquals(author.biography!!.value, savedAuthor.biography!!.value)
        assertEquals(author.email!!.value, savedAuthor.email!!.value)
        assertEquals(author.website!!.value, savedAuthor.website!!.value)

        assertNotNull(foundAuthor)
        assertEquals(author.id.value, foundAuthor.id.value)
        assertEquals(author.fullName.value, foundAuthor.fullName.value)
        assertEquals(author.roles, foundAuthor.roles)
    }

    @Test
    fun `should find all authors`() {
        // Given
        val author1 =
            Author(
                id = AuthorId.generate(),
                fullName = FullName("Author One"),
                roles = setOf(AuthorRole.AUTHOR),
            )
        val author2 =
            Author(
                id = AuthorId.generate(),
                fullName = FullName("Author Two"),
                roles = setOf(AuthorRole.AUTHOR, AuthorRole.ILLUSTRATOR),
            )

        // When
        jpaAuthorRepository.save(author1)
        jpaAuthorRepository.save(author2)
        val authors = jpaAuthorRepository.findAll()

        // Then
        assertEquals(2, authors.size)
        assertTrue(authors.any { it.fullName.value == "Author One" })
        assertTrue(authors.any { it.fullName.value == "Author Two" })
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
}
