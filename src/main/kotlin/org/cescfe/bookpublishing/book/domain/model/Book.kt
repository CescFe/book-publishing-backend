package org.cescfe.bookpublishing.book.domain.model

import org.cescfe.bookpublishing.book.domain.exception.BookDomainException
import org.cescfe.bookpublishing.book.domain.model.enum.Status
import org.cescfe.bookpublishing.shared.domain.model.Metadata
import org.cescfe.bookpublishing.shared.domain.model.enum.Genre
import org.cescfe.bookpublishing.shared.domain.model.enum.Language
import org.cescfe.bookpublishing.shared.domain.model.enum.ReadingLevel
import java.time.LocalDate
import java.util.UUID
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

data class Book(
    val id: BookId,
    val title: BookTitle,
    val authorId: AuthorIdRef,
    val collectionId: CollectionIdRef,
    val readingLevel: ReadingLevel? = null,
    val primaryLanguage: Language? = null,
    val secondaryLanguages: SecondaryLanguages? = null,
    val primaryGenre: Genre? = null,
    val secondaryGenres: SecondaryGenres? = null,
    val basePrice: BasePrice,
    val vatRate: VatRate? = null,
    val isbn: ISBN? = null,
    val publicationDate: PublicationDate? = null,
    val pageCount: PageCount? = null,
    val coverImagePath: CoverImagePath? = null,
    val description: Description? = null,
    val status: Status? = null,
    val audit: Metadata? = null,
) {
    val finalPrice: Double
        get() = calculateFinalPrice()

    private fun calculateFinalPrice(): Double {
        val vat = vatRate?.value ?: 0.04
        val finalPrice = basePrice.value * (1 + vat)
        return (finalPrice * 100).roundToInt() / 100.0
    }
}

@JvmInline
value class BookId(
    val value: UUID,
) {
    companion object {
        private val UUID_REGEX = Regex("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$")

        fun generate(): BookId = BookId(UUID.randomUUID())

        fun fromString(value: String): BookId {
            require(value.matches(UUID_REGEX)) {
                throw BookDomainException.bookIdInvalidFormat(value)
            }
            return BookId(UUID.fromString(value))
        }
    }
}

@JvmInline
value class BookTitle(
    val value: String,
) {
    init {
        require(value.isNotBlank()) { throw BookDomainException.titleCannotBeBlank() }
        require(value.length in 1..200) { throw BookDomainException.titleTooLong() }
    }
}

@JvmInline
value class AuthorIdRef(
    val value: UUID,
) {
    companion object {
        fun fromString(value: String): AuthorIdRef = AuthorIdRef(UUID.fromString(value))
    }
}

@JvmInline
value class CollectionIdRef(
    val value: UUID,
) {
    companion object {
        fun fromString(value: String): CollectionIdRef = CollectionIdRef(UUID.fromString(value))
    }
}

@JvmInline
value class BasePrice(
    val value: Double,
) {
    init {
        require(value >= 0.0) {
            throw BookDomainException.basePriceCannotBeNegative()
        }
        val rounded = (value * 100).roundToInt() / 100.0
        require((value - rounded).absoluteValue < 0.001) {
            throw BookDomainException.basePriceInvalidPrecision()
        }
    }

    companion object {
        fun fromDouble(value: Double): BasePrice {
            val rounded = (value * 100).roundToInt() / 100.0
            return BasePrice(rounded)
        }
    }
}

@JvmInline
value class VatRate(
    val value: Double,
) {
    init {
        require(value in 0.0..1.0) {
            throw BookDomainException.vatRateOutOfRange()
        }
        val rounded = (value * 100).roundToInt() / 100.0
        require((value - rounded).absoluteValue < 0.001) {
            throw BookDomainException.vatRateInvalidPrecision()
        }
    }

    companion object {
        fun default(): VatRate = VatRate(0.04)

        fun fromDouble(value: Double): VatRate {
            val rounded = (value * 100).roundToInt() / 100.0
            return VatRate(rounded)
        }
    }
}

@JvmInline
value class ISBN(
    val value: String,
) {
    init {
        require(value.matches(ISBN_REGEX)) {
            throw BookDomainException.isbnInvalidFormat()
        }
    }

    companion object {
        private val ISBN_REGEX = Regex("^(978|979)\\d{10}$")
    }
}

@JvmInline
value class PublicationDate(
    val value: LocalDate,
) {
    companion object {
        fun fromString(value: String): PublicationDate =
            try {
                PublicationDate(LocalDate.parse(value))
            } catch (_: Exception) {
                throw BookDomainException.publicationDateInvalidFormat()
            }
    }
}

@JvmInline
value class PageCount(
    val value: Int,
) {
    init {
        require(value >= 1) {
            throw BookDomainException.pageCountMustBePositive()
        }
    }
}

@JvmInline
value class CoverImagePath(
    val value: String,
) {
    init {
        require(value.length <= 255) {
            throw BookDomainException.coverImagePathTooLong()
        }
    }
}

@JvmInline
value class Description(
    val value: String,
) {
    init {
        require(value.length <= 2000) {
            throw BookDomainException.descriptionTooLong()
        }
    }
}

@JvmInline
value class SecondaryLanguages(
    val value: List<Language>,
) {
    init {
        require(value.size <= 3) {
            throw BookDomainException.secondaryLanguagesTooMany()
        }
        require(value.size == value.distinct().size) {
            throw BookDomainException.secondaryLanguageDuplicated()
        }
    }

    fun validateNotContainingPrimary(primaryLanguage: Language) {
        require(!value.contains(primaryLanguage)) {
            throw BookDomainException.secondaryLanguageSameAsPrimary()
        }
    }
}

@JvmInline
value class SecondaryGenres(
    val value: List<Genre>,
) {
    init {
        require(value.size <= 3) {
            throw BookDomainException.secondaryGenresTooMany()
        }
        require(value.size == value.distinct().size) {
            throw BookDomainException.secondaryGenreDuplicated()
        }
    }

    fun validateNotContainingPrimary(primaryGenre: Genre) {
        require(!value.contains(primaryGenre)) {
            throw BookDomainException.secondaryGenreSameAsPrimary()
        }
    }
}
