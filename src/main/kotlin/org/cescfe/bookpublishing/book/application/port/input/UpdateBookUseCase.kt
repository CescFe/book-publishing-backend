package org.cescfe.bookpublishing.book.application.port.input

import org.cescfe.bookpublishing.book.domain.model.Book
import org.cescfe.bookpublishing.book.domain.model.enum.Status
import org.cescfe.bookpublishing.shared.domain.model.enum.Genre
import org.cescfe.bookpublishing.shared.domain.model.enum.Language
import org.cescfe.bookpublishing.shared.domain.model.enum.ReadingLevel
import java.time.LocalDate
import java.util.UUID

interface UpdateBookUseCase {
    fun execute(bookId: String, command: Command): Book

    data class Command(
        val title: String,
        val authorId: UUID,
        val collectionId: UUID,
        val readingLevel: ReadingLevel? = null,
        val primaryLanguage: Language? = null,
        val secondaryLanguages: List<Language>? = null,
        val primaryGenre: Genre? = null,
        val secondaryGenres: List<Genre>? = null,
        val basePrice: Double,
        val vatRate: Double? = null,
        val isbn: String? = null,
        val publicationDate: LocalDate? = null,
        val pageCount: Int? = null,
        val coverImagePath: String? = null,
        val description: String? = null,
        val status: Status? = null,
    )
}
