package org.cescfe.bookpublishing.book.infrastructure.adapters.input.rest.mapper

import org.cescfe.bookpublishing.book.domain.model.Book
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.CreateBook201ResponseDTO
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.GetBooks200ResponseAllOfDataInnerAuthorDTO
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.GetBooks200ResponseAllOfDataInnerCollectionDTO
import org.springframework.stereotype.Component
import java.time.ZoneOffset

@Component
class BookRestMapper {
    fun toDto(domain: Book): CreateBook201ResponseDTO =
        CreateBook201ResponseDTO(
            id = domain.id.value,
            title = domain.title.value,
            author =
                GetBooks200ResponseAllOfDataInnerAuthorDTO(
                    id = domain.authorId.value,
                    name = domain.authorName,
                ),
            collection =
                GetBooks200ResponseAllOfDataInnerCollectionDTO(
                    id = domain.collectionId.value,
                    name = domain.collectionName,
                ),
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
            finalPrice = domain.finalPrice,
            isbn = domain.isbn?.value,
            publicationDate = domain.publicationDate?.value,
            pageCount = domain.pageCount?.value,
            coverImagePath = domain.coverImagePath?.value,
            description = domain.description?.value,
            status = domain.status?.let { CreateBook201ResponseDTO.Status.valueOf(it.name) },
            createdAt = domain.audit?.createdAt?.atOffset(ZoneOffset.UTC),
            createdBy = domain.audit?.createdBy,
            updatedAt = domain.audit?.updatedAt?.atOffset(ZoneOffset.UTC),
            updatedBy = domain.audit?.updatedBy,
        )
}
