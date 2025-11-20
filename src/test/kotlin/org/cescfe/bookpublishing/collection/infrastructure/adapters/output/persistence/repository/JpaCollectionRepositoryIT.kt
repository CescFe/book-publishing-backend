package org.cescfe.bookpublishing.collection.infrastructure.adapters.output.persistence.repository

import org.cescfe.bookpublishing.collection.domain.model.CollectionId
import org.cescfe.bookpublishing.collection.infrastructure.adapters.output.persistence.repository.config.JpaCollectionRepositoryTestConfig
import org.cescfe.bookpublishing.collection.objectMothers.CollectionObjectMother
import org.cescfe.bookpublishing.shared.infrastructure.adapters.output.persistence.config.TestJpaAuditingConfig
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql
import org.springframework.transaction.annotation.Transactional
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
@Transactional
@Testcontainers
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(
    TestJpaAuditingConfig::class,
    JpaCollectionRepositoryTestConfig::class,
)
class JpaCollectionRepositoryIT {
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

    @Test
    fun `should save and find collection by id`() {
        // Given
        val collection = CollectionObjectMother.createWithAllFields()

        // When
        val savedCollection = jpaCollectionRepository.save(collection)
        val foundCollection = jpaCollectionRepository.findById(collection.id)

        // Then
        assertNotNull(savedCollection)
        assertNotNull(foundCollection)
        assertEquals(collection, foundCollection)
        assertEquals(savedCollection, foundCollection)
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

    @Test
    fun `should return paginated collections`() {
        // Given
        jpaCollectionRepository.save(CollectionObjectMother.create(name = "A"))
        jpaCollectionRepository.save(CollectionObjectMother.create(name = "B"))
        jpaCollectionRepository.save(CollectionObjectMother.create(name = "C"))

        // When
        val firstPage = jpaCollectionRepository.findAllSummary(page = 1, limit = 2)
        val secondPage = jpaCollectionRepository.findAllSummary(page = 2, limit = 2)

        // Then
        assertEquals(2, firstPage.size)
        assertEquals(1, secondPage.size)

        assertEquals("A", firstPage[0].name.value)
        assertEquals("B", firstPage[1].name.value)
        assertEquals("C", secondPage[0].name.value)
    }

    @Test
    fun `should delete collection by id`() {
        // Given
        val collection = CollectionObjectMother.createWithAllFields()
        jpaCollectionRepository.save(collection)

        // Precondition
        assertTrue(jpaCollectionRepository.existsById(collection.id))

        // When
        jpaCollectionRepository.deleteById(collection.id)

        // Then
        assertFalse(jpaCollectionRepository.existsById(collection.id))
        assertNull(jpaCollectionRepository.findById(collection.id))
    }

    @Test
    fun `should return true when collection exists`() {
        // Given
        val collection = CollectionObjectMother.create()
        jpaCollectionRepository.save(collection)

        // Then
        assertTrue(jpaCollectionRepository.existsById(collection.id))
    }

    @Test
    fun `should return false when collection does not exist`() {
        val nonExistentId = CollectionId.generate()

        assertFalse(jpaCollectionRepository.existsById(nonExistentId))
    }

    @Test
    @Sql("/datasets/collections.sql")
    fun `should load collection inserted via SQL script`() {
        // Given
        val id = CollectionId.fromString("00000000-0000-0000-0000-000000000001")

        // When
        val found = jpaCollectionRepository.findById(id)

        // Then
        assertNotNull(found)
        assertEquals("00000000-0000-0000-0000-000000000001", found.id.value.toString())
        assertEquals("SQL Inserted Collection", found.name.value)
        assertEquals("ADULT", found.readingLevel!!.name)
        assertEquals("ENGLISH", found.primaryLanguage!!.name)
        assertEquals(listOf("CATALAN", "SPANISH"), found.secondaryLanguages!!.value.map { it.name })
        assertEquals("FANTASY", found.primaryGenre!!.name)
        assertEquals(listOf("ADVENTURE", "HISTORICAL_FICTION"), found.secondaryGenres!!.value.map { it.name })
    }
}
