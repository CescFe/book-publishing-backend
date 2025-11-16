package org.cescfe.bookpublishing.author.domain.model

data class AuthorSummary(
    val id: AuthorId,
    val fullName: FullName,
    val pseudonym: Pseudonym? = null,
    val email: Email? = null,
)
