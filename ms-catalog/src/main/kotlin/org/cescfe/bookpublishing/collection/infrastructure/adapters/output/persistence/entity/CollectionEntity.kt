package org.cescfe.bookpublishing.collection.infrastructure.adapters.output.persistence.entity

import com.vladmihalcea.hibernate.type.json.JsonBinaryType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.cescfe.bookpublishing.shared.domain.model.enum.Genre
import org.cescfe.bookpublishing.shared.domain.model.enum.Language
import org.cescfe.bookpublishing.shared.domain.model.enum.ReadingLevel
import org.cescfe.bookpublishing.shared.infrastructure.adapters.output.AuditableEntity
import org.hibernate.annotations.Type
import java.util.UUID

@Entity
@Table(name = "collection", schema = "publishing")
data class CollectionEntity(
    @Id
    @Column(name = "id", columnDefinition = "UUID")
    val id: UUID,
    @Column(name = "name", nullable = false)
    val name: String,
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
) : AuditableEntity()
