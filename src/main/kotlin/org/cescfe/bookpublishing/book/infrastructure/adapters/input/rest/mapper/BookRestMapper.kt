package org.cescfe.bookpublishing.book.infrastructure.adapters.input.rest.mapper

import org.cescfe.bookpublishing.book.domain.model.Book
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.CreateBook201ResponseDTO
import org.springframework.stereotype.Component

@Component
class BookRestMapper {
    fun toDto(domain: Book): CreateBook201ResponseDTO =
        CreateBook201ResponseDTO(
            id = domain.id.value,
            title = domain.title.value,
            authorId = domain.authorId.value,
            collectionId = domain.collectionId.value,
            readingLevel =
                domain.readingLevel?.let {
                    CreateBook201ResponseDTO.ReadingLevel.valueOf(it.name)
                },
            primaryLanguage =
                domain.primaryLanguage?.let {
                    CreateBook201ResponseDTO.PrimaryLanguage.valueOf(it.name)
                },
            secondaryLanguages =
                domain.secondaryLanguages?.value?.map { lang ->
                    CreateBook201ResponseDTO.SecondaryLanguages.valueOf(lang.name)
                },
            primaryGenre =
                domain.primaryGenre?.let {
                    CreateBook201ResponseDTO.PrimaryGenre.valueOf(it.name)
                },
            secondaryGenres =
                domain.secondaryGenres?.value?.map { genre ->
                    CreateBook201ResponseDTO.SecondaryGenres.valueOf(genre.name)
                },
            basePrice = domain.basePrice.value,
            vatRate = domain.vatRate?.value,
            isbn = domain.isbn?.value,
            publicationDate = domain.publicationDate?.value,
            pageCount = domain.pageCount?.value,
            coverImagePath = domain.coverImagePath?.value,
            description = domain.description?.value,
        )
}
