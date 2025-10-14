package org.cescfe.bookpublishing.author.domain.model

data class SearchCriteria(
    val searchTerm: String? = null,
    val page: Int,
    val limit: Int,
) {
    init {
        require(page > 0) { "Page must be greater than 0" }
        require(limit > 0) { "Limit must be greater than 0" }
        require(limit <= 100) { "Limit cannot exceed 100" }
    }
}
