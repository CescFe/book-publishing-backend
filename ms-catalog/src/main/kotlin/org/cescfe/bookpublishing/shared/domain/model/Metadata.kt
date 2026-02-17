package org.cescfe.bookpublishing.shared.domain.model

import java.time.LocalDateTime

data class Metadata(
    val createdAt: LocalDateTime?,
    val createdBy: String?,
    val updatedAt: LocalDateTime?,
    val updatedBy: String?,
)
