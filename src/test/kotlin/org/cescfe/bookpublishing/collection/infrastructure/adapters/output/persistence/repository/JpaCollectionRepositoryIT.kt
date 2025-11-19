package org.cescfe.bookpublishing.collection.infrastructure.adapters.output.persistence.repository

import org.cescfe.bookpublishing.collection.domain.model.CollectionId
import org.cescfe.bookpublishing.collection.infrastructure.adapters.output.persistence.repository.config.JpaCollectionRepositoryTestConfig
import org.cescfe.bookpublishing.collection.objectMothers.CollectionObjectMother
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
    JpaCollectionRepositoryTestConfig::class,
)
class JpaCollectionRepositoryIT {
    @Autowired
    private lateinit var collectionJpaEntityRepository: CollectionJpaEntityRepository

    @Autowired
    private lateinit var testEntityManager: TestEntityManager

    @Autowired
    private lateinit var jpaCollectionRepository: JpaCollectionRepository

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
        collectionJpaEntityRepository.deleteAll()
        testEntityManager.flush()
    }

    @Test
    fun `should save and find collection by id`() {
        // Given
        val collection = CollectionObjectMother.createWithAllFields()

        // When
        val savedCollection = jpaCollectionRepository.save(collection)
        val foundCollection = jpaCollectionRepository.findById(collection.id)

        // Then
        assertNotNull(savedCollection)
        assertEquals(collection.id.value, savedCollection.id.value)
        assertEquals(collection.name.value, savedCollection.name.value)
        assertEquals(collection.readingLevel, savedCollection.readingLevel)
        assertEquals(collection.primaryLanguage, savedCollection.primaryLanguage)
        assertEquals(collection.primaryGenre, savedCollection.primaryGenre)
        assertEquals(collection.secondaryLanguages!!.value, savedCollection.secondaryLanguages!!.value)
        assertEquals(collection.secondaryGenres!!.value, savedCollection.secondaryGenres!!.value)

        assertNotNull(foundCollection)
        assertEquals(collection.id.value, foundCollection.id.value)
        assertEquals(collection.name.value, foundCollection.name.value)
        assertEquals(collection.readingLevel, foundCollection.readingLevel)
        assertEquals(collection.primaryLanguage, foundCollection.primaryLanguage)
        assertEquals(collection.primaryGenre, foundCollection.primaryGenre)
        assertEquals(collection.secondaryLanguages.value, foundCollection.secondaryLanguages!!.value)
        assertEquals(collection.secondaryGenres.value, foundCollection.secondaryGenres!!.value)
    }

    @Test
    fun `should find all collections`() {
        // Given
        val collection1 =
            CollectionObjectMother.create(
                name = "Collection One",
            )
        val collection2 =
            CollectionObjectMother.create(
                name = "Collection Two",
            )

        jpaCollectionRepository.save(collection1)
        jpaCollectionRepository.save(collection2)

        // When
        val collections = jpaCollectionRepository.findAllSummary()

        // Then
        assertEquals(2, collections.size)
        assertTrue(collections.any { it.name.value == "Collection One" })
        assertTrue(collections.any { it.name.value == "Collection Two" })
    }

    @Test
    fun `should return null when collection not found`() {
        // Given
        val nonExistentId = CollectionId.generate()

        // When
        val foundCollection = jpaCollectionRepository.findById(nonExistentId)

        // Then
        assertNull(foundCollection)
    }
}
