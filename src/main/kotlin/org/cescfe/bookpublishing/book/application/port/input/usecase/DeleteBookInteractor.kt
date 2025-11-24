package org.cescfe.bookpublishing.book.application.port.input.usecase

import org.cescfe.bookpublishing.book.application.port.input.DeleteBookUseCase
import org.cescfe.bookpublishing.book.domain.exception.BookDomainException
import org.cescfe.bookpublishing.book.domain.model.BookId
import org.cescfe.bookpublishing.book.domain.port.BookRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class DeleteBookInteractor(
    private val bookRepository: BookRepository,
) : DeleteBookUseCase {
    override fun execute(command: DeleteBookUseCase.Command) {
        val bookId = BookId.fromString(command.bookId)

        bookRepository.findById(bookId)
            ?: throw BookDomainException.bookNotFound(command.bookId)

        bookRepository.deleteById(bookId)
    }
}
