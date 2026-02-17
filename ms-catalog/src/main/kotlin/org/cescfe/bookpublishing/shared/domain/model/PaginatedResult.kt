package org.cescfe.bookpublishing.shared.domain.model

data class PaginatedResult<T>(
    val data: List<T>,
    val metadata: PaginationMeta,
)

data class PaginationMeta(
    val total: Long,
    val page: Int,
    val limit: Int,
    val totalPages: Int,
) {
    init {
        require(page > 0) { "Page must be greater than 0" }
        require(limit > 0) { "Limit must be greater than 0" }
        require(total >= 0) { "Total must be non-negative" }
        require(totalPages >= 0) { "Total pages must be non-negative" }
    }
}
