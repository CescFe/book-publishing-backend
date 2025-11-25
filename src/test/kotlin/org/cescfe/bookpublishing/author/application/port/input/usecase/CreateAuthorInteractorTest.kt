package org.cescfe.bookpublishing.author.application.port.input.usecase

import org.cescfe.bookpublishing.author.application.port.input.mapper.CreateAuthorUseCaseMapper
import org.cescfe.bookpublishing.author.domain.exception.AuthorDomainException
import org.cescfe.bookpublishing.author.domain.port.AuthorRepository
import org.cescfe.bookpublishing.author.domain.service.AuthorDomainService
import org.cescfe.bookpublishing.author.objectMothers.AuthorObjectMother
import org.cescfe.bookpublishing.author.objectMothers.CreateAuthorCommandObjectMother
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

class CreateAuthorInteractorTest {
    private val authorRepository = mock<AuthorRepository>()
    private val mapper = mock<CreateAuthorUseCaseMapper>()
    private val authorDomainService = mock<AuthorDomainService>()
    private val createAuthorUseCase = CreateAuthorInteractor(authorRepository, mapper, authorDomainService)

    companion object {
        private const val TOLKIEN_EMAIL = "tolkien@example.com"
        private const val EXISTING_EMAIL = "existing@example.com"
        private const val EXPECTED_ERROR_MESSAGE = "Author with email '$EXISTING_EMAIL' already exists"
        private const val EXPECTED_ERROR_SUBTYPE = "EMAIL_ALREADY_EXISTS"
    }

    @Test
    fun `should create author successfully`() {
        // Given
        val input = CreateAuthorCommandObjectMother.createTolkien()
        val expectedAuthor = AuthorObjectMother.createTolkien()

        whenever(mapper.toDomain(input)).thenReturn(expectedAuthor)
        whenever(authorRepository.save(any())).thenReturn(expectedAuthor)

        // When
        val result = createAuthorUseCase.execute(input)

        // Then
        assertEquals(expectedAuthor, result)

        verify(authorDomainService).ensureEmailUniqueness(TOLKIEN_EMAIL)
        verify(mapper).toDomain(input)
        verify(authorRepository).save(any())
    }

    @Test
    fun `should create author without optional fields`() {
        // Given
        val input = CreateAuthorCommandObjectMother.createMinimal()
        val expectedAuthor = AuthorObjectMother.createMinimal()

        whenever(mapper.toDomain(input)).thenReturn(expectedAuthor)
        whenever(authorRepository.save(any())).thenReturn(expectedAuthor)

        // When
        val result = createAuthorUseCase.execute(input)

        // Then
        assertEquals(expectedAuthor, result)

        verify(authorDomainService).ensureEmailUniqueness(null)
        verify(mapper).toDomain(input)
        verify(authorRepository).save(expectedAuthor)
    }

    @Test
    fun `should throw exception when email already exists`() {
        // Given
        val input = CreateAuthorCommandObjectMother.createWithEmail(EXISTING_EMAIL)

        whenever(authorDomainService.ensureEmailUniqueness(EXISTING_EMAIL))
            .thenThrow(AuthorDomainException.emailAlreadyExists(EXISTING_EMAIL))

        // When & Then
        val exception =
            assertThrows<AuthorDomainException> {
                createAuthorUseCase.execute(input)
            }

        assertEquals(EXPECTED_ERROR_MESSAGE, exception.message)
        assertEquals(EXPECTED_ERROR_SUBTYPE, exception.subType)
        verify(authorDomainService).ensureEmailUniqueness(EXISTING_EMAIL)
        verify(mapper, never()).toDomain(any())
        verify(authorRepository, never()).save(any())
    }
}
