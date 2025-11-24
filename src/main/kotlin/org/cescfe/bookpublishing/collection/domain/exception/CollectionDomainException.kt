package org.cescfe.bookpublishing.collection.domain.exception

import org.cescfe.bookpublishing.shared.domain.exception.DomainException

class CollectionDomainException(
    message: String,
    val subType: String,
    cause: Throwable? = null,
) : DomainException(message, cause) {
    companion object {
        // CollectionId exceptions
        fun collectionIdInvalidFormat(id: String): CollectionDomainException =
            CollectionDomainException(
                "Collection id '$id' has invalid format. Expected a valid UUID",
                "COLLECTION_ID_INVALID_FORMAT",
            )

        // CollectionName exceptions
        fun nameCannotBeBlank(): CollectionDomainException =
            CollectionDomainException("Collection name cannot be blank", "NAME_CANNOT_BE_BLANK")

        fun nameTooLong(): CollectionDomainException =
            CollectionDomainException("Collection name must be between 1 and 80 characters", "NAME_TOO_LONG")

        // SecondaryLanguages exceptions
        fun secondaryLanguagesTooMany(): CollectionDomainException =
            CollectionDomainException("Secondary languages cannot exceed 3 items", "SECONDARY_LANGUAGES_TOO_MANY")

        fun secondaryLanguageDuplicated(): CollectionDomainException =
            CollectionDomainException("Secondary languages cannot contain duplicates", "SECONDARY_LANGUAGES_DUPLICATED")

        fun secondaryLanguageSameAsPrimary(): CollectionDomainException =
            CollectionDomainException(
                "Secondary languages cannot contain the primary language",
                "SECONDARY_LANGUAGES_SAME_AS_PRIMARY",
            )

        // SecondaryGenres exceptions
        fun secondaryGenresTooMany(): CollectionDomainException =
            CollectionDomainException("Secondary genres cannot exceed 3 items", "SECONDARY_GENRES_TOO_MANY")

        fun secondaryGenreDuplicated(): CollectionDomainException =
            CollectionDomainException("Secondary genres cannot contain duplicates", "SECONDARY_GENRES_DUPLICATED")

        fun secondaryGenreSameAsPrimary(): CollectionDomainException =
            CollectionDomainException(
                "Secondary genres cannot contain the primary genre",
                "SECONDARY_GENRES_SAME_AS_PRIMARY",
            )

        // Collection not found exception
        fun collectionNotFound(id: String): CollectionDomainException =
            CollectionDomainException("Collection with id $id not found", "COLLECTION_NOT_FOUND")
    }
}
