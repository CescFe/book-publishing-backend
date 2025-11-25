package org.cescfe.bookpublishing.author.application.port.input.usecase

import org.cescfe.bookpublishing.author.application.port.input.mapper.UpdateAuthorUseCaseMapper
import org.cescfe.bookpublishing.author.domain.exception.AuthorDomainException
import org.cescfe.bookpublishing.author.domain.model.AuthorId
import org.cescfe.bookpublishing.author.domain.port.AuthorRepository
import org.cescfe.bookpublishing.author.domain.service.AuthorDomainService
import org.cescfe.bookpublishing.author.objectMothers.AuthorObjectMother
import org.cescfe.bookpublishing.author.objectMothers.UpdateAuthorCommandObjectMother
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.Test
import kotlin.test.assertEquals

class UpdateAuthorInteractorTest {
    private val authorRepository = mock<AuthorRepository>()
    private val mapper = mock<UpdateAuthorUseCaseMapper>()
    private val authorDomainService = mock<AuthorDomainService>()
    private val updateAuthorUseCase = UpdateAuthorInteractor(authorRepository, mapper, authorDomainService)

    companion object {
        private const val EXISTING_EMAIL = "existing@example.com"
        private const val EXISTING_ID = "477537ff-7e8b-4930-bd41-d7f3589120b1"
    }

    @Test
    fun `should update author successfully`() {
        // Given
        val input = UpdateAuthorCommandObjectMother.createWithTolkienId()
        val authorId = AuthorId.fromString(input.authorId)
        val existingAuthor = AuthorObjectMother.createTolkien()
        val updatedAuthor =
            AuthorObjectMother.create(
                id = authorId.value,
                fullName = "Updated Name",
            )

        whenever(authorRepository.findById(authorId)).thenReturn(existingAuthor)
        whenever(authorDomainService.ensureEmailUniquenessForUpdate(input.email, input.authorId)).then { }
        whenever(mapper.toDomain(input, existingAuthor)).thenReturn(updatedAuthor)
        whenever(authorRepository.save(updatedAuthor)).thenReturn(updatedAuthor)

        // When
        val result = updateAuthorUseCase.execute(input)

        // Then
        assertEquals(updatedAuthor, result)
        verify(authorRepository).findById(authorId)
        verify(authorDomainService).ensureEmailUniquenessForUpdate(input.email, input.authorId)
        verify(mapper).toDomain(input, existingAuthor)
        verify(authorRepository).save(updatedAuthor)
    }

    @Test
    fun `should throw exception when author not found`() {
        // Given
        val input = UpdateAuthorCommandObjectMother.createWithTolkienId()
        val authorId = AuthorId.fromString(input.authorId)

        whenever(authorRepository.findById(authorId)).thenReturn(null)

        // When & Then
        val exception =
            assertThrows<AuthorDomainException> {
                updateAuthorUseCase.execute(input)
            }
        assertEquals("Author with id ${input.authorId} not found", exception.message)
        verify(authorRepository).findById(authorId)
    }

    @Test
    fun `should throw exception when email already exists for another author`() {
        // Given
        val input =
            UpdateAuthorCommandObjectMother.create(
                authorId = EXISTING_ID,
                email = EXISTING_EMAIL,
            )
        val authorId = AuthorId.fromString(input.authorId)
        val existingAuthor = AuthorObjectMother.createTolkien()

        whenever(authorRepository.findById(authorId)).thenReturn(existingAuthor)
        whenever(authorDomainService.ensureEmailUniquenessForUpdate(EXISTING_EMAIL, input.authorId))
            .thenThrow(AuthorDomainException.emailAlreadyExists(EXISTING_EMAIL))

        // When & Then
        assertThrows<AuthorDomainException> {
            updateAuthorUseCase.execute(input)
        }
        verify(authorRepository).findById(authorId)
        verify(authorDomainService).ensureEmailUniquenessForUpdate(EXISTING_EMAIL, input.authorId)
    }
}
