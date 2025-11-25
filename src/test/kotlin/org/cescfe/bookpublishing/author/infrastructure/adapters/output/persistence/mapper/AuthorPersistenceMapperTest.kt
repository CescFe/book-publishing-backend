package org.cescfe.bookpublishing.author.infrastructure.adapters.output.persistence.mapper

import org.cescfe.bookpublishing.author.domain.model.AuthorId
import org.cescfe.bookpublishing.author.domain.model.Biography
import org.cescfe.bookpublishing.author.domain.model.Email
import org.cescfe.bookpublishing.author.domain.model.FullName
import org.cescfe.bookpublishing.author.domain.model.Pseudonym
import org.cescfe.bookpublishing.author.domain.model.Website
import org.cescfe.bookpublishing.author.objectMothers.AuthorEntityObjectMother
import org.cescfe.bookpublishing.author.objectMothers.AuthorObjectMother
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class AuthorPersistenceMapperTest {
    private val authorMapper = AuthorPersistenceMapper()

    @Test
    fun `should map author domain to entity correctly`() {
        // Given
        val author = AuthorObjectMother.createWithAllFields()

        // When
        val result = authorMapper.fromDomain(author)

        // Then
        assertEquals(author.id.value, result.id)
        assertEquals(author.fullName.value, result.fullName)
        assertEquals(author.pseudonym!!.value, result.pseudonym)
        assertEquals(author.biography!!.value, result.biography)
        assertEquals(author.email!!.value, result.email)
        assertEquals(author.website!!.value, result.website)
    }

    @Test
    fun `should map author domain with minimal data correctly`() {
        // Given
        val author = AuthorObjectMother.create(fullName = "Simple Author")

        // When
        val result = authorMapper.fromDomain(author)

        // Then
        assertEquals(author.id.value, result.id)
        assertEquals(author.fullName.value, result.fullName)
        assertNull(result.pseudonym)
        assertNull(result.biography)
        assertNull(result.email)
        assertNull(result.website)
    }

    @Test
    fun `should map entity to author domain correctly`() {
        // Given
        val entity = AuthorEntityObjectMother.createTolkien()

        // When
        val result = authorMapper.toDomain(entity)

        // Then
        assertEquals(AuthorId(entity.id), result.id)
        assertEquals(FullName(entity.fullName), result.fullName)
        assertEquals(Pseudonym(entity.pseudonym!!), result.pseudonym)
        assertEquals(Biography(entity.biography!!), result.biography)
        assertEquals(Email(entity.email!!), result.email)
        assertEquals(Website(entity.website!!), result.website)
        assertEquals(entity.createdAt, result.audit?.createdAt)
        assertEquals(entity.createdBy, result.audit?.createdBy)
        assertEquals(entity.updatedAt, result.audit?.updatedAt)
        assertEquals(entity.updatedBy, result.audit?.updatedBy)
    }

    @Test
    fun `should map entity with minimal data correctly`() {
        // Given
        val entity = AuthorEntityObjectMother.createSimple()

        // When
        val result = authorMapper.toDomain(entity)

        // Then
        assertEquals(AuthorId(entity.id), result.id)
        assertEquals(FullName(entity.fullName), result.fullName)
        assertNull(result.pseudonym)
        assertNull(result.biography)
        assertNull(result.email)
        assertNull(result.website)
    }
}
