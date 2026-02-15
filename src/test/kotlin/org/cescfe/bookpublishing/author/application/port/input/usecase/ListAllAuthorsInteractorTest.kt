package org.cescfe.bookpublishing.author.application.port.input.usecase

import org.cescfe.bookpublishing.author.application.port.input.mapper.ListAuthorsUseCaseMapper
import org.cescfe.bookpublishing.author.domain.model.AuthorSummary
import org.cescfe.bookpublishing.author.domain.port.AuthorRepository
import org.cescfe.bookpublishing.author.objectMothers.AuthorSummaryObjectMother
import org.cescfe.bookpublishing.shared.domain.model.NonPaginatedResult
import org.cescfe.bookpublishing.shared.domain.model.NonPaginationMeta
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.Test
import kotlin.test.assertEquals

class ListAllAuthorsInteractorTest {
    private val authorRepository: AuthorRepository = mock()
    private val mapper: ListAuthorsUseCaseMapper = mock()
    private val listAllAuthorsUseCase = ListAllAuthorsInteractor(authorRepository, mapper)

    @Test
    fun `should return non paginated result when authors found`() {
        // Given
        val authors =
            listOf(
                AuthorSummaryObjectMother.createFirstAuthorSummary(),
                AuthorSummaryObjectMother.createSecondAuthorSummary(),
            )
        val totalCount = 2L
        val expectedResult =
            NonPaginatedResult(
                data = authors,
                metadata =
                    NonPaginationMeta(
                        total = 2,
                    ),
            )

        whenever(authorRepository.findAllSummary()).thenReturn(authors)
        whenever(authorRepository.countAll()).thenReturn(totalCount)
        whenever(mapper.toNonPaginatedResult(authors, totalCount)).thenReturn(expectedResult)

        // When
        val result = listAllAuthorsUseCase.execute()

        // Then
        assertEquals(expectedResult, result)
        verify(authorRepository).findAllSummary()
        verify(authorRepository).countAll()
        verify(mapper).toNonPaginatedResult(authors, totalCount)
    }

    @Test
    fun `should return empty non paginated result when no authors found`() {
        // Given
        val emptyAuthors = emptyList<AuthorSummary>()
        val totalCount = 0L
        val expectedResult =
            NonPaginatedResult(
                data = emptyAuthors,
                metadata =
                    NonPaginationMeta(
                        total = 0,
                    ),
            )

        whenever(authorRepository.findAllSummary()).thenReturn(emptyAuthors)
        whenever(authorRepository.countAll()).thenReturn(totalCount)
        whenever(mapper.toNonPaginatedResult(emptyAuthors, totalCount)).thenReturn(expectedResult)

        // When
        val result = listAllAuthorsUseCase.execute()

        // Then
        assertEquals(expectedResult, result)
        verify(authorRepository).findAllSummary()
        verify(authorRepository).countAll()
        verify(mapper).toNonPaginatedResult(emptyAuthors, totalCount)
    }
}
