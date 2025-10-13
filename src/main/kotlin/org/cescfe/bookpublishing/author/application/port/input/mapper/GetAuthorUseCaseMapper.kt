package org.cescfe.bookpublishing.author.application.port.input.mapper

import org.cescfe.bookpublishing.author.application.port.input.GetAuthorUseCase
import org.cescfe.bookpublishing.author.domain.model.AuthorId
import org.springframework.stereotype.Component

@Component
class GetAuthorUseCaseMapper {
    fun toDomain(authorIdString: String): AuthorId = AuthorId.fromString(authorIdString)

    fun toInputValues(authorId: AuthorId): GetAuthorUseCase.InputValues =
        GetAuthorUseCase.InputValues(authorId.value.toString())
}
