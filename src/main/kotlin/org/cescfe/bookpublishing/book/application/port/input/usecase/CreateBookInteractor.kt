package org.cescfe.bookpublishing.book.application.port.input.usecase

import org.cescfe.bookpublishing.book.application.port.input.CreateBookUseCase
import org.cescfe.bookpublishing.book.application.port.input.mapper.CreateBookUseCaseMapper
import org.cescfe.bookpublishing.book.domain.model.Book
import org.cescfe.bookpublishing.book.domain.port.BookRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CreateBookInteractor(
    private val bookRepository: BookRepository,
    private val mapper: CreateBookUseCaseMapper,
) : CreateBookUseCase {
    override fun execute(command: CreateBookUseCase.Command): Book {
        val book = mapper.toDomain(command)
        return bookRepository.save(book)
    }
}
