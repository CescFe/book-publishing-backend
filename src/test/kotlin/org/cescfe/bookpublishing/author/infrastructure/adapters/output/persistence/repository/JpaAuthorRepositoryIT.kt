package org.cescfe.bookpublishing.author.infrastructure.adapters.output.persistence.repository

import org.cescfe.bookpublishing.author.domain.model.AuthorId
import org.cescfe.bookpublishing.author.infrastructure.adapters.output.persistence.entity.PersonRoleEntity
import org.cescfe.bookpublishing.author.infrastructure.adapters.output.persistence.entity.PersonRoleId
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
    JpaAuthorRepositoryTestConfig::class,
)
class JpaAuthorRepositoryIT {
    @Autowired
    private lateinit var authorJpaEntityRepository: AuthorJpaEntityRepository

    @Autowired
    private lateinit var testEntityManager: TestEntityManager

    @Autowired
    private lateinit var jpaAuthorRepository: JpaAuthorRepository

    @Autowired
    private lateinit var roleJpaEntityRepository: RoleJpaEntityRepository

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

        val authorRole = roleJpaEntityRepository.findByName("AUTHOR")
        if (authorRole != null) {
            val authorEntity = authorJpaEntityRepository.findById(savedAuthor.id.value).orElseThrow()
            authorEntity.personRoles.add(
                PersonRoleEntity(
                    id = PersonRoleId(savedAuthor.id.value, authorRole.id),
                    person = authorEntity,
                    role = authorRole,
                ),
            )
            authorJpaEntityRepository.save(authorEntity)
            testEntityManager.flush()
        }

        val foundAuthor = jpaAuthorRepository.findById(author.id)

        // Then
        assertNotNull(savedAuthor)
        assertEquals(author.id.value, savedAuthor.id.value)
        assertEquals(author.fullName.value, savedAuthor.fullName.value)
        assertEquals(author.pseudonym!!.value, savedAuthor.pseudonym!!.value)
        assertEquals(author.biography!!.value, savedAuthor.biography!!.value)
        assertEquals(author.email!!.value, savedAuthor.email!!.value)
        assertEquals(author.website!!.value, savedAuthor.website!!.value)

        assertNotNull(foundAuthor)
        assertEquals(author.id.value, foundAuthor.id.value)
        assertEquals(author.fullName.value, foundAuthor.fullName.value)
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

        // When
        val savedAuthor1 = jpaAuthorRepository.save(author1)
        val savedAuthor2 = jpaAuthorRepository.save(author2)

        val authorRole = roleJpaEntityRepository.findByName("AUTHOR")
        if (authorRole != null) {
            val authorEntity1 = authorJpaEntityRepository.findById(savedAuthor1.id.value).orElseThrow()
            val authorEntity2 = authorJpaEntityRepository.findById(savedAuthor2.id.value).orElseThrow()

            authorEntity1.personRoles.add(
                PersonRoleEntity(
                    id = PersonRoleId(savedAuthor1.id.value, authorRole.id),
                    person = authorEntity1,
                    role = authorRole,
                ),
            )
            authorEntity2.personRoles.add(
                PersonRoleEntity(
                    id = PersonRoleId(savedAuthor2.id.value, authorRole.id),
                    person = authorEntity2,
                    role = authorRole,
                ),
            )

            authorJpaEntityRepository.save(authorEntity1)
            authorJpaEntityRepository.save(authorEntity2)
            testEntityManager.flush()
        }
        val authors = jpaAuthorRepository.findAllSummary()

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
