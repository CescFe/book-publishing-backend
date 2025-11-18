package org.cescfe.bookpublishing.book.domain.exception

import org.cescfe.bookpublishing.shared.domain.exception.DomainException

class BookDomainException(
    message: String,
    val exceptionSubType: String,
    cause: Throwable? = null,
) : DomainException(message, cause) {
    companion object {
        // BookTitle exceptions
        fun titleCannotBeBlank(): BookDomainException =
            BookDomainException("Book title cannot be blank", "TITLE_CANNOT_BE_BLANK")

        fun titleTooLong(): BookDomainException =
            BookDomainException("Book title must be between 1 and 200 characters", "TITLE_TOO_LONG")

        // BasePrice exceptions
        fun basePriceCannotBeNegative(): BookDomainException =
            BookDomainException("Base price cannot be negative", "BASE_PRICE_CANNOT_BE_NEGATIVE")

        fun basePriceInvalidPrecision(): BookDomainException =
            BookDomainException("Base price must have at most 2 decimal places", "BASE_PRICE_INVALID_PRECISION")

        // VatRate exceptions
        fun vatRateOutOfRange(): BookDomainException =
            BookDomainException("VAT rate must be between 0 and 1", "VAT_RATE_OUT_OF_RANGE")

        fun vatRateInvalidPrecision(): BookDomainException =
            BookDomainException("VAT rate must have at most 2 decimal places", "VAT_RATE_INVALID_PRECISION")

        // ISBN exceptions
        fun isbnInvalidFormat(): BookDomainException =
            BookDomainException(
                "ISBN must be a valid ISBN-13 format (starting with 978 or 979 followed by 10 digits)",
                "ISBN_INVALID_FORMAT",
            )

        // PublicationDate exceptions
        fun publicationDateInvalidFormat(): BookDomainException =
            BookDomainException("Publication date must be a valid date", "PUBLICATION_DATE_INVALID_FORMAT")

        // PageCount exceptions
        fun pageCountMustBePositive(): BookDomainException =
            BookDomainException("Page count must be at least 1", "PAGE_COUNT_MUST_BE_POSITIVE")

        // CoverImagePath exceptions
        fun coverImagePathTooLong(): BookDomainException =
            BookDomainException("Cover image path cannot exceed 255 characters", "COVER_IMAGE_PATH_TOO_LONG")

        // Description exceptions
        fun descriptionTooLong(): BookDomainException =
            BookDomainException("Description cannot exceed 2000 characters", "DESCRIPTION_TOO_LONG")

        // SecondaryLanguages exceptions
        fun secondaryLanguagesTooMany(): BookDomainException =
            BookDomainException("Secondary languages cannot exceed 3 items", "SECONDARY_LANGUAGES_TOO_MANY")

        fun secondaryLanguageDuplicated(): BookDomainException =
            BookDomainException("Secondary languages cannot contain duplicates", "SECONDARY_LANGUAGES_DUPLICATED")

        fun secondaryLanguageSameAsPrimary(): BookDomainException =
            BookDomainException(
                "Secondary languages cannot contain the primary language",
                "SECONDARY_LANGUAGES_SAME_AS_PRIMARY",
            )

        // SecondaryGenres exceptions
        fun secondaryGenresTooMany(): BookDomainException =
            BookDomainException("Secondary genres cannot exceed 3 items", "SECONDARY_GENRES_TOO_MANY")

        fun secondaryGenreDuplicated(): BookDomainException =
            BookDomainException("Secondary genres cannot contain duplicates", "SECONDARY_GENRES_DUPLICATED")

        fun secondaryGenreSameAsPrimary(): BookDomainException =
            BookDomainException(
                "Secondary genres cannot contain the primary genre",
                "SECONDARY_GENRES_SAME_AS_PRIMARY",
            )

        // Book not found exception
        fun bookNotFound(id: String): BookDomainException =
            BookDomainException("Book with id $id not found", "BOOK_NOT_FOUND")
    }
}
