package org.cescfe.bookpublishing.author.infrastructure.adapters.input.rest.mapper

import org.cescfe.bookpublishing.author.domain.model.Author
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.CreateAuthor201ResponseDTO
import org.springframework.stereotype.Component
import java.net.URI
import java.time.ZoneOffset

@Component
class AuthorRestMapper {
    fun toDto(author: Author): CreateAuthor201ResponseDTO =
        CreateAuthor201ResponseDTO(
            id = author.id.value,
            fullName = author.fullName.value,
            pseudonym = author.pseudonym?.value,
            biography = author.biography?.value,
            email = author.email?.value,
            website = author.website?.value?.let { URI(it) },
            createdAt = author.audit?.createdAt?.atOffset(ZoneOffset.UTC),
            createdBy = author.audit?.createdBy,
            updatedAt = author.audit?.updatedAt?.atOffset(ZoneOffset.UTC),
            updatedBy = author.audit?.updatedBy,
        )
}
