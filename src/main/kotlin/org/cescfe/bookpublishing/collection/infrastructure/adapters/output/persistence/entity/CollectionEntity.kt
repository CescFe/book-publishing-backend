package org.cescfe.bookpublishing.collection.infrastructure.adapters.output.persistence.entity

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
import java.util.List
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
    @Column(name = "secondary_languages", columnDefinition = "VARCHAR(20)[]")
    val secondaryLanguages: List<String>?,
    @Enumerated(EnumType.STRING)
    @Column(name = "primary_genre")
    val primaryGenre: Genre?,
    @Column(name = "secondary_genres", columnDefinition = "VARCHAR(50)[]")
    val secondaryGenres: List<String>?,
) : AuditableEntity() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CollectionEntity

        if (id != other.id) return false
        if (name != other.name) return false
        if (readingLevel != other.readingLevel) return false
        if (primaryLanguage != other.primaryLanguage) return false
        if (primaryGenre != other.primaryGenre) return false
        if (secondaryLanguages != other.secondaryLanguages) return false
        if (secondaryGenres != other.secondaryGenres) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + (readingLevel?.hashCode() ?: 0)
        result = 31 * result + (primaryLanguage?.hashCode() ?: 0)
        result = 31 * result + (primaryGenre?.hashCode() ?: 0)
        result = 31 * result + (secondaryLanguages?.hashCode() ?: 0)
        result = 31 * result + (secondaryGenres?.hashCode() ?: 0)
        return result
    }
}
