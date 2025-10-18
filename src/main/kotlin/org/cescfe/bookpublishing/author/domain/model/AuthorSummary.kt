package org.cescfe.bookpublishing.author.domain.model

import org.cescfe.bookpublishing.author.domain.exception.AuthorDomainException

data class AuthorSummary(
    val id: AuthorId,
    val fullName: FullName,
    val roles: Set<AuthorRole>,
    val pseudonym: Pseudonym? = null,
    val email: Email? = null,
) {
    init {
        require(roles.isNotEmpty()) { throw AuthorDomainException.emptyRoles() }
        require(roles.contains(AuthorRole.AUTHOR)) { throw AuthorDomainException.missingAuthorRole() }
    }
}
