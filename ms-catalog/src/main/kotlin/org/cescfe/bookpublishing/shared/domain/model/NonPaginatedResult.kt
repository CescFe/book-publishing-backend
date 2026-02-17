package org.cescfe.bookpublishing.shared.domain.model

data class NonPaginatedResult<T>(
    val data: List<T>,
    val metadata: NonPaginationMeta,
)

data class NonPaginationMeta(
    val total: Long,
) {
    init {
        require(total >= 0) { "Total must be non-negative" }
    }
}
