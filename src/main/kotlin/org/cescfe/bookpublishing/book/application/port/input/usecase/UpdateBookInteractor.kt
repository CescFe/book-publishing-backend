package org.cescfe.bookpublishing.book.application.port.input.usecase

import org.cescfe.bookpublishing.book.application.port.input.UpdateBookUseCase
import org.cescfe.bookpublishing.book.application.port.input.mapper.UpdateBookUseCaseMapper
import org.cescfe.bookpublishing.book.domain.exception.BookDomainException
import org.cescfe.bookpublishing.book.domain.model.Book
import org.cescfe.bookpublishing.book.domain.model.BookId
import org.cescfe.bookpublishing.book.domain.port.BookRepository
import org.cescfe.bookpublishing.book.domain.service.BookDomainService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UpdateBookInteractor(
    private val bookRepository: BookRepository,
    private val mapper: UpdateBookUseCaseMapper,
    private val bookDomainService: BookDomainService,
) : UpdateBookUseCase {
    override fun execute(command: UpdateBookUseCase.Command): Book {
        val bookId = BookId.fromString(command.bookId)
        val existingBook =
            bookRepository.findById(bookId)
                ?: throw BookDomainException.bookNotFound(command.bookId)

        bookDomainService.ensureIsbnUniquenessForUpdate(command.isbn, command.bookId)
        val updatedBook = mapper.toDomain(command, existingBook)

        return bookRepository.save(updatedBook)
    }
}
