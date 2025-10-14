package org.cescfe.bookpublishing.author.application.port.input.mapper

import org.cescfe.bookpublishing.author.application.port.input.DeleteAuthorUseCase
import org.cescfe.bookpublishing.author.domain.model.AuthorId
import org.springframework.stereotype.Component

@Component
class DeleteAuthorUseCaseMapper {
    fun toDomain(authorIdString: String): AuthorId = AuthorId.fromString(authorIdString)

    fun toInputValues(authorId: AuthorId): DeleteAuthorUseCase.InputValues =
        DeleteAuthorUseCase.InputValues(authorId.value.toString())
}
