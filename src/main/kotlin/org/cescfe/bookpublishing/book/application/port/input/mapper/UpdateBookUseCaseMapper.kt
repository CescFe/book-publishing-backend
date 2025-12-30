package org.cescfe.bookpublishing.book.application.port.input.mapper

import org.cescfe.bookpublishing.book.application.port.input.UpdateBookUseCase
import org.cescfe.bookpublishing.book.domain.model.AuthorIdRef
import org.cescfe.bookpublishing.book.domain.model.BasePrice
import org.cescfe.bookpublishing.book.domain.model.Book
import org.cescfe.bookpublishing.book.domain.model.BookTitle
import org.cescfe.bookpublishing.book.domain.model.CollectionIdRef
import org.cescfe.bookpublishing.book.domain.model.CoverImagePath
import org.cescfe.bookpublishing.book.domain.model.Description
import org.cescfe.bookpublishing.book.domain.model.ISBN
import org.cescfe.bookpublishing.book.domain.model.PageCount
import org.cescfe.bookpublishing.book.domain.model.PublicationDate
import org.cescfe.bookpublishing.book.domain.model.SecondaryGenres
import org.cescfe.bookpublishing.book.domain.model.SecondaryLanguages
import org.cescfe.bookpublishing.book.domain.model.VatRate
import org.springframework.stereotype.Component

@Component
class UpdateBookUseCaseMapper {
    fun toDomain(
        input: UpdateBookUseCase.Command,
        existingBook: Book,
    ): Book =
        Book(
            id = existingBook.id,
            title = BookTitle(input.title),
            authorId = AuthorIdRef(input.authorId),
            collectionId = CollectionIdRef(input.collectionId),
            readingLevel = input.readingLevel,
            primaryLanguage = input.primaryLanguage,
            secondaryLanguages = input.secondaryLanguages?.let(::SecondaryLanguages),
            primaryGenre = input.primaryGenre,
            secondaryGenres = input.secondaryGenres?.let(::SecondaryGenres),
            basePrice = BasePrice(input.basePrice),
            vatRate = input.vatRate?.let(::VatRate),
            finalPrice = Book.calculateFinalPrice(input.basePrice, input.vatRate),
            isbn = input.isbn?.let(::ISBN),
            publicationDate = input.publicationDate?.let(::PublicationDate),
            pageCount = input.pageCount?.let(::PageCount),
            coverImagePath = input.coverImagePath?.let(::CoverImagePath),
            description = input.description?.let(::Description),
            status = Book.defaultStatus(input.status),
            audit = existingBook.audit,
        )
}
