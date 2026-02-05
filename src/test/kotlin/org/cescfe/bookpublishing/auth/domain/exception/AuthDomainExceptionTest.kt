package org.cescfe.bookpublishing.auth.domain.exception

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class AuthDomainExceptionTest {
    @Test
    fun `should build user id invalid format exception`() {
        val ex = AuthDomainException.userIdInvalidFormat("bad-id")

        assertEquals("USER_ID_INVALID_FORMAT", ex.subType)
        assertEquals("User id 'bad-id' has invalid format. Expected a valid UUID", ex.message)
    }

    @Test
    fun `should build username cannot be blank exception`() {
        val ex = AuthDomainException.usernameCannotBeBlank()

        assertEquals("USERNAME_CANNOT_BE_BLANK", ex.subType)
        assertEquals("Username cannot be blank", ex.message)
    }

    @Test
    fun `should build username too long exception`() {
        val ex = AuthDomainException.usernameTooLong()

        assertEquals("USERNAME_TOO_LONG", ex.subType)
        assertEquals("Username must be between 1 and 255 characters", ex.message)
    }

    @Test
    fun `should build password hash cannot be blank exception`() {
        val ex = AuthDomainException.passwordHashCannotBeBlank()

        assertEquals("PASSWORD_HASH_CANNOT_BE_BLANK", ex.subType)
        assertEquals("Password hash cannot be blank", ex.message)
    }

    @Test
    fun `should build invalid credentials exception`() {
        val ex = AuthDomainException.invalidCredentials()

        assertEquals("INVALID_CREDENTIALS", ex.subType)
        assertEquals("Invalid credentials", ex.message)
    }
}
