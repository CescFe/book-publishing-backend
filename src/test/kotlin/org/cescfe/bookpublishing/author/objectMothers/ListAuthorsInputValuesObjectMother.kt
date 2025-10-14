package org.cescfe.bookpublishing.author.objectMothers

import org.cescfe.bookpublishing.author.application.port.input.ListAuthorsUseCase

object ListAuthorsInputValuesObjectMother {
    fun create(
        page: Int = 1,
        limit: Int = 20,
    ): ListAuthorsUseCase.InputValues =
        ListAuthorsUseCase.InputValues(
            page = page,
            limit = limit,
        )

    fun createWithPagination(
        page: Int,
        limit: Int,
    ): ListAuthorsUseCase.InputValues = create(page = page, limit = limit)

    fun createFirstPage(): ListAuthorsUseCase.InputValues = create(page = 1, limit = 10)

    fun createSecondPage(): ListAuthorsUseCase.InputValues = create(page = 2, limit = 10)

    fun createWithLargeLimit(): ListAuthorsUseCase.InputValues = create(page = 1, limit = 100)
}
