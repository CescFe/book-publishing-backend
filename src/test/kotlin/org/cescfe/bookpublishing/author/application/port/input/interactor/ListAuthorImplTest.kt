package org.cescfe.bookpublishing.author.application.port.input.interactor

import org.cescfe.bookpublishing.author.application.port.input.mapper.ListAuthorsUseCaseMapper
import org.cescfe.bookpublishing.author.domain.model.Author
import org.cescfe.bookpublishing.author.domain.model.PaginatedResult
import org.cescfe.bookpublishing.author.domain.model.PaginationMeta
import org.cescfe.bookpublishing.author.domain.port.AuthorRepositoryView
import org.cescfe.bookpublishing.author.objectMothers.AuthorObjectMother
import org.cescfe.bookpublishing.author.objectMothers.ListAuthorsInputValuesObjectMother
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.Test
import kotlin.test.assertEquals

class ListAuthorImplTest {
    private val authorRepository = mock<AuthorRepositoryView>()
    private val mapper = mock<ListAuthorsUseCaseMapper>()
    private val listAuthorsUseCase = ListAuthorsImpl(authorRepository, mapper)

    @Test
    fun `should return paginated result when authors found`() {
        // Given
        val input = ListAuthorsInputValuesObjectMother.create(page = 1, limit = 2)
        val authors =
            listOf(
                AuthorObjectMother.createTolkien(),
                AuthorObjectMother.createMinimal(),
            )
        val totalCount = 2L
        val expectedResult =
            PaginatedResult(
                data = authors,
                meta =
                    PaginationMeta(
                        total = 2,
                        page = 1,
                        limit = 2,
                        totalPages = 1,
                    ),
            )

        whenever(authorRepository.findAll(input.page, input.limit)).thenReturn(authors)
        whenever(authorRepository.countAll()).thenReturn(totalCount)
        whenever(mapper.toPaginatedResult(authors, totalCount, input.page, input.limit))
            .thenReturn(expectedResult)

        // When
        val result = listAuthorsUseCase.execute(input)

        // Then
        assertEquals(expectedResult, result)
        verify(authorRepository).findAll(input.page, input.limit)
        verify(authorRepository).countAll()
        verify(mapper).toPaginatedResult(authors, totalCount, input.page, input.limit)
    }

    @Test
    fun `should return empty result when no authors found`() {
        // Given
        val input = ListAuthorsInputValuesObjectMother.create(page = 1, limit = 10)
        val emptyAuthors = emptyList<Author>()
        val totalCount = 0L
        val expectedResult =
            PaginatedResult(
                data = emptyAuthors,
                meta =
                    PaginationMeta(
                        total = 0,
                        page = 1,
                        limit = 10,
                        totalPages = 0,
                    ),
            )

        whenever(authorRepository.findAll(input.page, input.limit)).thenReturn(emptyAuthors)
        whenever(authorRepository.countAll()).thenReturn(totalCount)
        whenever(mapper.toPaginatedResult(emptyAuthors, totalCount, input.page, input.limit))
            .thenReturn(expectedResult)

        // When
        val result = listAuthorsUseCase.execute(input)

        // Then
        assertEquals(expectedResult, result)
        verify(authorRepository).findAll(input.page, input.limit)
        verify(authorRepository).countAll()
        verify(mapper).toPaginatedResult(emptyAuthors, totalCount, input.page, input.limit)
    }

    @Test
    fun `should handle pagination correctly for second page`() {
        // Given
        val input = ListAuthorsInputValuesObjectMother.create(page = 2, limit = 1)
        val authors = listOf(AuthorObjectMother.createMinimal())
        val totalCount = 3L
        val expectedResult =
            PaginatedResult(
                data = authors,
                meta =
                    PaginationMeta(
                        total = 3,
                        page = 2,
                        limit = 1,
                        totalPages = 3,
                    ),
            )

        whenever(authorRepository.findAll(input.page, input.limit)).thenReturn(authors)
        whenever(authorRepository.countAll()).thenReturn(totalCount)
        whenever(mapper.toPaginatedResult(authors, totalCount, input.page, input.limit))
            .thenReturn(expectedResult)

        // When
        val result = listAuthorsUseCase.execute(input)

        // Then
        assertEquals(expectedResult, result)
        verify(authorRepository).findAll(input.page, input.limit)
        verify(authorRepository).countAll()
        verify(mapper).toPaginatedResult(authors, totalCount, input.page, input.limit)
    }
}
