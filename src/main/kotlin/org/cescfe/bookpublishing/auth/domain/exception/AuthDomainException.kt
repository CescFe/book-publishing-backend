package org.cescfe.bookpublishing.auth.domain.exception

import org.cescfe.bookpublishing.shared.domain.exception.DomainException

class AuthDomainException(
    message: String,
    val subType: String,
    cause: Throwable? = null,
) : DomainException(message, cause) {
    companion object {
        fun userIdInvalidFormat(id: String): AuthDomainException =
            AuthDomainException(
                "User id '$id' has invalid format. Expected a valid UUID",
                "USER_ID_INVALID_FORMAT",
            )

        fun usernameCannotBeBlank(): AuthDomainException =
            AuthDomainException("Username cannot be blank", "USERNAME_CANNOT_BE_BLANK")

        fun usernameTooLong(): AuthDomainException =
            AuthDomainException("Username must be between 1 and 255 characters", "USERNAME_TOO_LONG")

        fun passwordHashCannotBeBlank(): AuthDomainException =
            AuthDomainException("Password hash cannot be blank", "PASSWORD_HASH_CANNOT_BE_BLANK")

        fun invalidCredentials(): AuthDomainException =
            AuthDomainException("Invalid credentials", "INVALID_CREDENTIALS")
    }
}
