package org.cescfe.bookpublishing.author.infrastructure.adapters.input.rest.mapper

import org.cescfe.bookpublishing.author.domain.model.Author
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.GetAuthors200ResponseAllOfDataInnerDTO
import org.springframework.stereotype.Component
import java.net.URI
import java.time.OffsetDateTime

@Component
class AuthorRestMapper {
    fun toDto(author: Author): GetAuthors200ResponseAllOfDataInnerDTO =
        GetAuthors200ResponseAllOfDataInnerDTO(
            id = author.id.value,
            fullName = author.fullName.value,
            roles =
                author.roles.map {
                    GetAuthors200ResponseAllOfDataInnerDTO.Roles.forValue(it.value)
                },
            pseudonym = author.pseudonym?.value,
            biography = author.biography?.value,
            email = author.email?.value,
            website = author.website?.value?.let { URI(it) },
            version = 1L,
            createdAt = OffsetDateTime.now(),
            updatedAt = OffsetDateTime.now(),
        )
}
