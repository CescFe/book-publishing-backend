package org.cescfe.bookpublishing.book.application.port.input.mapper

import org.cescfe.bookpublishing.book.application.port.input.CreateBookUseCase
import org.cescfe.bookpublishing.book.domain.model.AuthorIdRef
import org.cescfe.bookpublishing.book.domain.model.BasePrice
import org.cescfe.bookpublishing.book.domain.model.Book
import org.cescfe.bookpublishing.book.domain.model.BookId
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
class CreateBookUseCaseMapper {
    fun toDomain(command: CreateBookUseCase.Command): Book =
        Book(
            id = BookId.generate(),
            title = BookTitle(command.title),
            authorId = AuthorIdRef(command.authorId),
            collectionId = CollectionIdRef(command.collectionId),
            readingLevel = command.readingLevel,
            primaryLanguage = command.primaryLanguage,
            secondaryLanguages = command.secondaryLanguages?.let(::SecondaryLanguages),
            primaryGenre = command.primaryGenre,
            secondaryGenres = command.secondaryGenres?.let(::SecondaryGenres),
            basePrice = BasePrice(command.basePrice),
            vatRate = command.vatRate?.let(::VatRate),
            finalPrice = Book.calculateFinalPrice(command.basePrice, command.vatRate),
            isbn = command.isbn?.let(::ISBN),
            publicationDate = command.publicationDate?.let(::PublicationDate),
            pageCount = command.pageCount?.let(::PageCount),
            coverImagePath = command.coverImagePath?.let(::CoverImagePath),
            description = command.description?.let(::Description),
            status = command.status,
        )
}
