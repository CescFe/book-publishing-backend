package org.cescfe.bookpublishing.collection.domain.model

import org.cescfe.bookpublishing.collection.domain.exception.CollectionDomainException
import org.cescfe.bookpublishing.shared.domain.model.Metadata
import org.cescfe.bookpublishing.shared.domain.model.enum.Genre
import org.cescfe.bookpublishing.shared.domain.model.enum.Language
import org.cescfe.bookpublishing.shared.domain.model.enum.ReadingLevel
import java.util.UUID

data class Collection(
    val id: CollectionId,
    val name: CollectionName,
    val readingLevel: ReadingLevel? = null,
    val primaryLanguage: Language? = null,
    val secondaryLanguages: SecondaryLanguages? = null,
    val primaryGenre: Genre? = null,
    val secondaryGenres: SecondaryGenres? = null,
    val audit: Metadata? = null,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Collection) return false

        return id == other.id &&
            name == other.name &&
            readingLevel == other.readingLevel &&
            primaryLanguage == other.primaryLanguage &&
            secondaryLanguages == other.secondaryLanguages &&
            primaryGenre == other.primaryGenre &&
            secondaryGenres == other.secondaryGenres
    }

    override fun hashCode(): Int {
        return id.hashCode() +
            name.hashCode() +
            (readingLevel?.hashCode() ?: 0) +
            (primaryLanguage?.hashCode() ?: 0) +
            (secondaryLanguages?.hashCode() ?: 0) +
            (primaryGenre?.hashCode() ?: 0) +
            (secondaryGenres?.hashCode() ?: 0)
    }
}

@JvmInline
value class CollectionId(
    val value: UUID,
) {
    companion object {
        fun generate(): CollectionId = CollectionId(UUID.randomUUID())

        fun fromString(value: String): CollectionId = CollectionId(UUID.fromString(value))
    }
}

@JvmInline
value class CollectionName(
    val value: String,
) {
    init {
        require(value.isNotBlank()) { throw CollectionDomainException.nameCannotBeBlank() }
        require(value.length in 1..80) { throw CollectionDomainException.nameTooLong() }
    }
}

@JvmInline
value class SecondaryLanguages(
    val value: List<Language>,
) {
    init {
        require(value.size <= 3) { throw CollectionDomainException.secondaryLanguagesTooMany() }
        require(value.size == value.distinct().size) {
            throw CollectionDomainException.secondaryLanguageDuplicated()
        }
    }

    fun validateNotContainingPrimary(primaryLanguage: Language) {
        require(!value.contains(primaryLanguage)) {
            throw CollectionDomainException.secondaryLanguageSameAsPrimary()
        }
    }
}

@JvmInline
value class SecondaryGenres(
    val value: List<Genre>,
) {
    init {
        require(value.size <= 3) { throw CollectionDomainException.secondaryGenresTooMany() }
        require(value.size == value.distinct().size) {
            throw CollectionDomainException.secondaryGenreDuplicated()
        }
    }

    fun validateNotContainingPrimary(primaryGenre: Genre) {
        require(!value.contains(primaryGenre)) {
            throw CollectionDomainException.secondaryGenreSameAsPrimary()
        }
    }
}
