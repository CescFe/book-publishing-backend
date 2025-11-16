package org.cescfe.bookpublishing.author.application.port.input.interactor

import org.cescfe.bookpublishing.author.application.port.input.mapper.CreateAuthorUseCaseMapper
import org.cescfe.bookpublishing.author.domain.exception.AuthorDomainException
import org.cescfe.bookpublishing.author.domain.port.AuthorRepositoryView
import org.cescfe.bookpublishing.author.domain.service.AuthorDomainService
import org.cescfe.bookpublishing.author.objectMothers.AuthorObjectMother
import org.cescfe.bookpublishing.author.objectMothers.CreateAuthorInputValuesObjectMother
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

class CreateAuthorImplTest {
    private val authorRepository = mock<AuthorRepositoryView>()
    private val mapper = mock<CreateAuthorUseCaseMapper>()
    private val authorDomainService = mock<AuthorDomainService>()
    private val createAuthorUseCase = CreateAuthorImpl(authorRepository, mapper, authorDomainService)

    companion object {
        private const val TOLKIEN_EMAIL = "tolkien@example.com"
        private const val EXISTING_EMAIL = "existing@example.com"
        private const val EXPECTED_ERROR_MESSAGE = "Author with email '$EXISTING_EMAIL' already exists"
    }

    @Test
    fun `should create author successfully`() {
        // Given
        val input = CreateAuthorInputValuesObjectMother.createTolkien()
        val expectedAuthor = AuthorObjectMother.createTolkien()

        whenever(mapper.toDomain(input)).thenReturn(expectedAuthor)
        whenever(authorRepository.save(any())).thenReturn(expectedAuthor)

        // When
        val result = createAuthorUseCase.execute(input)

        // Then
        assertNotNull(result)
        assertEquals(expectedAuthor.fullName.value, result.fullName.value)
        assertEquals(expectedAuthor.pseudonym!!.value, result.pseudonym!!.value)
        assertEquals(expectedAuthor.biography!!.value, result.biography!!.value)
        assertEquals(expectedAuthor.email!!.value, result.email!!.value)
        assertEquals(expectedAuthor.website!!.value, result.website!!.value)

        verify(authorDomainService).ensureEmailUniqueness(TOLKIEN_EMAIL)
        verify(mapper).toDomain(input)
        verify(authorRepository).save(any())
    }

    @Test
    fun `should throw exception when email already exists`() {
        // Given
        val input = CreateAuthorInputValuesObjectMother.createWithEmail(EXISTING_EMAIL)

        whenever(authorDomainService.ensureEmailUniqueness(EXISTING_EMAIL))
            .thenThrow(AuthorDomainException.emailAlreadyExists(EXISTING_EMAIL))

        // When & Then
        val exception =
            assertThrows<AuthorDomainException> {
                createAuthorUseCase.execute(input)
            }

        assertEquals(EXPECTED_ERROR_MESSAGE, exception.message)
        verify(authorDomainService).ensureEmailUniqueness(EXISTING_EMAIL)
        verify(mapper, never()).toDomain(any())
        verify(authorRepository, never()).save(any())
    }

    @Test
    fun `should create author without optional fields`() {
        // Given
        val input = CreateAuthorInputValuesObjectMother.createMinimal()
        val expectedAuthor = AuthorObjectMother.createMinimal()

        whenever(mapper.toDomain(input)).thenReturn(expectedAuthor)
        whenever(authorRepository.save(any())).thenReturn(expectedAuthor)

        // When
        val result = createAuthorUseCase.execute(input)

        // Then
        assertNotNull(result)
        assertEquals(expectedAuthor.fullName.value, result.fullName.value)
        assertEquals(null, result.pseudonym)
        assertEquals(null, result.biography)
        assertEquals(null, result.email)
        assertEquals(null, result.website)

        verify(authorDomainService).ensureEmailUniqueness(null)
        verify(mapper).toDomain(input)
        verify(authorRepository).save(expectedAuthor)
    }

    @Test
    fun `should throw AuthorDomainException when email already exists`() {
        // Given
        val existingEmail = "existing@example.com"
        val input =
            CreateAuthorInputValuesObjectMother.create(
                fullName = "Test Author",
                email = existingEmail,
            )

        whenever(authorDomainService.ensureEmailUniqueness(existingEmail))
            .thenThrow(AuthorDomainException.emailAlreadyExists(existingEmail))

        // When & Then
        val exception =
            assertThrows<AuthorDomainException> {
                createAuthorUseCase.execute(input)
            }
        assertEquals("Author with email '$existingEmail' already exists", exception.message)
        verify(authorDomainService).ensureEmailUniqueness(existingEmail)
        verify(mapper, never()).toDomain(any())
        verify(authorRepository, never()).save(any())
    }
}
