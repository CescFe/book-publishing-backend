package org.cescfe.bookpublishing.book.infrastructure.adapters.input.rest.mapper

import org.cescfe.bookpublishing.book.domain.model.Book
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.CreateBooks201ResponseDTO
import org.springframework.stereotype.Component
import java.net.URI

@Component
class BookRestMapper {
    fun toDto(domain: Book): CreateBooks201ResponseDTO =
        CreateBooks201ResponseDTO(
            id = domain.id.value,
            title = domain.title.value,
            authorId = domain.authorId.value,
            collectionId = domain.collectionId.value,
            readingLevel =
                domain.readingLevel?.let {
                    CreateBooks201ResponseDTO.ReadingLevel.valueOf(it.name)
                },
            primaryLanguage =
                domain.primaryLanguage?.let {
                    CreateBooks201ResponseDTO.PrimaryLanguage.valueOf(it.name)
                },
            secondaryLanguages =
                domain.secondaryLanguages?.value?.map { lang ->
                    CreateBooks201ResponseDTO.SecondaryLanguages.valueOf(lang.name)
                },
            primaryGenre =
                domain.primaryGenre?.let {
                    CreateBooks201ResponseDTO.PrimaryGenre.valueOf(it.name)
                },
            secondaryGenres =
                domain.secondaryGenres?.value?.map { genre ->
                    CreateBooks201ResponseDTO.SecondaryGenres.valueOf(genre.name)
                },
            basePrice = domain.basePrice.value,
            vatRate = domain.vatRate?.value,
            isbn = domain.isbn?.value,
            publicationDate = domain.publicationDate?.value,
            pageCount = domain.pageCount?.value,
            coverImageUrl = domain.coverImagePath?.let { URI.create(it.value) },
            description = domain.description?.value,
        )
}
