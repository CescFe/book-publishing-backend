package org.cescfe.bookpublishing.author.infrastructure.adapters.output.persistence.mapper

import org.cescfe.bookpublishing.author.domain.model.AuthorId
import org.cescfe.bookpublishing.author.domain.model.AuthorRole
import org.cescfe.bookpublishing.author.domain.model.Biography
import org.cescfe.bookpublishing.author.domain.model.Email
import org.cescfe.bookpublishing.author.domain.model.FullName
import org.cescfe.bookpublishing.author.domain.model.Pseudonym
import org.cescfe.bookpublishing.author.domain.model.Website
import org.cescfe.bookpublishing.author.infrastructure.adapters.output.persistence.exception.RoleNotFoundException
import org.cescfe.bookpublishing.author.infrastructure.adapters.output.persistence.objectMothers.AuthorEntityObjectMother
import org.cescfe.bookpublishing.author.infrastructure.adapters.output.persistence.objectMothers.AuthorObjectMother
import org.cescfe.bookpublishing.author.infrastructure.adapters.output.persistence.objectMothers.RoleEntityObjectMother
import org.cescfe.bookpublishing.author.infrastructure.adapters.output.persistence.repository.RoleJpaEntityRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class AuthorMapperTest {
    private val roleJpaEntityRepository: RoleJpaEntityRepository = mock()
    private val authorMapper = AuthorMapper(roleJpaEntityRepository)

    @Test
    fun `fromDomain should map author domain to entity correctly`() {
        // Given
        val author = AuthorObjectMother.createTolkien()
        val authorRoleEntity = RoleEntityObjectMother.createAuthorRole()
        val translatorRoleEntity = RoleEntityObjectMother.createTranslatorRole()

        whenever(roleJpaEntityRepository.findByName("AUTHOR")).thenReturn(authorRoleEntity)
        whenever(roleJpaEntityRepository.findByName("TRANSLATOR")).thenReturn(translatorRoleEntity)

        // When
        val result = authorMapper.fromDomain(author)

        // Then
        assertEquals(author.id.value, result.id)
        assertEquals(author.fullName.value, result.fullName)
        assertEquals(author.pseudonym!!.value, result.pseudonym)
        assertEquals(author.biography!!.value, result.biography)
        assertEquals(author.email!!.value, result.email)
        assertEquals(author.website!!.value, result.website)
        assertEquals(1L, result.version)
        assertEquals(author.roles.size, result.personRoles.size)

        val roleNames = result.personRoles.map { it.role.name }.toSet()
        assertTrue(roleNames.contains("AUTHOR"))
    }

    @Test
    fun `fromDomain should map author with minimal data correctly`() {
        // Given
        val author = AuthorObjectMother.create(fullName = "Simple Author")

        whenever(roleJpaEntityRepository.findByName("AUTHOR"))
            .thenReturn(RoleEntityObjectMother.createAuthorRole())

        // When
        val result = authorMapper.fromDomain(author)

        // Then
        assertEquals(author.id.value, result.id)
        assertEquals(author.fullName.value, result.fullName)
        assertNull(result.pseudonym)
        assertNull(result.biography)
        assertNull(result.email)
        assertNull(result.website)
        assertEquals(author.roles.size, result.personRoles.size)
        assertEquals(
            author.roles.first().name,
            result.personRoles
                .first()
                .role.name,
        )
    }

    @Test
    fun `fromDomain should throw RoleNotFoundException when role not found`() {
        // Given
        val author = AuthorObjectMother.create()

        whenever(roleJpaEntityRepository.findByName("AUTHOR")).thenReturn(null)

        // When & Then
        val exception =
            assertThrows<RoleNotFoundException> {
                authorMapper.fromDomain(author)
            }
        assertEquals("Role 'AUTHOR' not found in database", exception.message)
    }

    @Test
    fun `toDomain should map entity to author domain correctly`() {
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
        assertEquals(entity.personRoles.size, result.roles.size)
        assertTrue(result.roles.contains(AuthorRole.AUTHOR))
        assertTrue(result.roles.contains(AuthorRole.ILLUSTRATOR))
    }

    @Test
    fun `toDomain should map entity with minimal data correctly`() {
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
        assertEquals(1, result.roles.size)
        assertTrue(result.roles.contains(AuthorRole.AUTHOR))
    }

    @Test
    fun `should maintain bidirectional mapping consistency`() {
        // Given
        val originalAuthor =
            AuthorObjectMother.create(
                fullName = "Test Author",
                pseudonym = "Test Pseudonym",
                email = "test@example.com",
            )

        whenever(roleJpaEntityRepository.findByName("AUTHOR"))
            .thenReturn(RoleEntityObjectMother.createAuthorRole())

        // When
        val entity = authorMapper.fromDomain(originalAuthor)
        val mappedBackAuthor = authorMapper.toDomain(entity)

        // Then
        assertEquals(originalAuthor.id, mappedBackAuthor.id)
        assertEquals(originalAuthor.fullName, mappedBackAuthor.fullName)
        assertEquals(originalAuthor.roles, mappedBackAuthor.roles)
        assertEquals(originalAuthor.pseudonym, mappedBackAuthor.pseudonym)
        assertEquals(originalAuthor.email, mappedBackAuthor.email)
    }
}
