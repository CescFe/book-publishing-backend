package org.cescfe.bookpublishing.book.infrastructure.adapters.output.persistence.projection

import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

interface BookWithRelationsProjection {
    val id: UUID
    val title: String
    val authorId: UUID
    val authorName: String
    val collectionId: UUID
    val collectionName: String
    val basePrice: Double
    val vatRate: Double?
    val finalPrice: Double
    val isbn: String?
    val publicationDate: LocalDate?
    val pageCount: Int?
    val coverImagePath: String?
    val description: String?
    val readingLevel: String?
    val primaryLanguage: String?
    val secondaryLanguages: String?
    val primaryGenre: String?
    val secondaryGenres: String?
    val status: String?
    val createdAt: LocalDateTime?
    val createdBy: String?
    val updatedAt: LocalDateTime?
    val updatedBy: String?
}
