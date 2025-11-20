package org.cescfe.bookpublishing.book.infrastructure.adapters.output.persistence.entity

import com.vladmihalcea.hibernate.type.json.JsonBinaryType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.cescfe.bookpublishing.book.domain.model.enum.Status
import org.cescfe.bookpublishing.shared.domain.model.enum.Genre
import org.cescfe.bookpublishing.shared.domain.model.enum.Language
import org.cescfe.bookpublishing.shared.domain.model.enum.ReadingLevel
import org.cescfe.bookpublishing.shared.infrastructure.adapters.output.AuditableEntity
import org.hibernate.annotations.Type
import java.time.LocalDate
import java.util.UUID

@Entity
@Table(name = "book", schema = "publishing")
data class BookEntity(
    @Id
    @Column(name = "id", columnDefinition = "UUID")
    val id: UUID,
    @Column(name = "title", nullable = false)
    val title: String,
    @Column(name = "author_id", nullable = false, columnDefinition = "UUID")
    val authorId: UUID,
    @Column(name = "collection_id", nullable = false, columnDefinition = "UUID")
    val collectionId: UUID,
    @Column(name = "base_price", nullable = false)
    val basePrice: Double,
    @Column(name = "vat_rate")
    val vatRate: Double?,
    @Column(name = "final_price", nullable = false)
    val finalPrice: Double,
    @Column(name = "isbn")
    val isbn: String?,
    @Column(name = "publication_date")
    val publicationDate: LocalDate?,
    @Column(name = "page_count")
    val pageCount: Int?,
    @Column(name = "cover_image_path")
    val coverImagePath: String?,
    @Column(name = "description")
    val description: String?,
    @Enumerated(EnumType.STRING)
    @Column(name = "reading_level")
    val readingLevel: ReadingLevel?,
    @Enumerated(EnumType.STRING)
    @Column(name = "primary_language")
    val primaryLanguage: Language?,
    @Type(JsonBinaryType::class)
    @Column(name = "secondary_languages", columnDefinition = "jsonb")
    val secondaryLanguages: List<Language>?,
    @Enumerated(EnumType.STRING)
    @Column(name = "primary_genre")
    val primaryGenre: Genre?,
    @Type(JsonBinaryType::class)
    @Column(name = "secondary_genres", columnDefinition = "jsonb")
    val secondaryGenres: List<Genre>?,
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    val status: Status?,
) : AuditableEntity()
