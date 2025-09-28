package org.cescfe.bookpublishing.shared.infrastructure.adapters.input.exception.model

import java.time.Instant

data class ApiError(
    val status: Int,
    val error: String,
    val message: String,
    val code: String? = null,
    val details: Map<String, Any>? = null,
    val timestamp: String = Instant.now().toString(),
)
