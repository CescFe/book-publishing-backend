package org.cescfe.bookpublishing.book.application.port.input.usecase

import org.cescfe.bookpublishing.book.application.port.input.CreateBookUseCase
import org.cescfe.bookpublishing.book.application.port.input.mapper.CreateBookUseCaseMapper
import org.cescfe.bookpublishing.book.domain.model.Book
import org.cescfe.bookpublishing.book.domain.port.BookRepository
import org.cescfe.bookpublishing.book.domain.service.BookDomainService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CreateBookInteractor(
    private val bookRepository: BookRepository,
    private val mapper: CreateBookUseCaseMapper,
    private val bookDomainService: BookDomainService,
) : CreateBookUseCase {
    override fun execute(command: CreateBookUseCase.Command): Book {
        bookDomainService.ensureIsbnUniqueness(command.isbn)
        val book = mapper.toDomain(command)
        val savedBook = bookRepository.save(book)
        return bookRepository.findById(savedBook.id)!!
    }
}
