package org.cescfe.bookpublishing.author.domain.exception

class InvalidAuthorRolesException(
    message: String,
    cause: Throwable? = null
) : AuthorDomainException(message, cause) {

    companion object {
        fun emptyRoles(): InvalidAuthorRolesException {
            return InvalidAuthorRolesException("Author must have at least one role")
        }

        fun missingAuthorRole(): InvalidAuthorRolesException {
            return InvalidAuthorRolesException("Author must have AUTHOR role")
        }
    }
}
