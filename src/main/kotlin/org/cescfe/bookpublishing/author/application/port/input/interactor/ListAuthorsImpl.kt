package org.cescfe.bookpublishing.author.application.port.input.interactor

import org.cescfe.bookpublishing.author.application.port.input.ListAuthorsUseCase
import org.cescfe.bookpublishing.author.application.port.input.mapper.ListAuthorsUseCaseMapper
import org.cescfe.bookpublishing.author.domain.model.Author
import org.cescfe.bookpublishing.author.domain.model.PaginatedResult
import org.cescfe.bookpublishing.author.domain.model.SearchCriteria
import org.cescfe.bookpublishing.author.domain.port.AuthorRepositoryView
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ListAuthorsImpl(
    private val authorRepository: AuthorRepositoryView,
    private val mapper: ListAuthorsUseCaseMapper,
) : ListAuthorsUseCase {
    override fun execute(input: ListAuthorsUseCase.InputValues): PaginatedResult<Author> {
        val searchCriteria = mapper.toSearchCriteria(input)

        return if (searchCriteria.searchTerm.isNullOrBlank()) {
            handleWithoutSearch(searchCriteria)
        } else {
            handleWithSearch(searchCriteria)
        }
    }

    private fun handleWithoutSearch(searchCriteria: SearchCriteria): PaginatedResult<Author> {
        val allAuthors = authorRepository.findAll()

        val startIndex = (searchCriteria.page - 1) * searchCriteria.limit
        val endIndex = minOf(startIndex + searchCriteria.limit, allAuthors.size)
        val paginatedAuthors = allAuthors.subList(startIndex, endIndex)

        return mapper.toPaginatedResult(
            authors = paginatedAuthors,
            totalCount = allAuthors.size.toLong(),
            page = searchCriteria.page,
            limit = searchCriteria.limit,
        )
    }

    private fun handleWithSearch(searchCriteria: SearchCriteria): PaginatedResult<Author> {
        val authors = authorRepository.findAll(searchCriteria)
        val totalCount = authorRepository.count(searchCriteria)

        return mapper.toPaginatedResult(authors, totalCount, searchCriteria.page, searchCriteria.limit)
    }
}
