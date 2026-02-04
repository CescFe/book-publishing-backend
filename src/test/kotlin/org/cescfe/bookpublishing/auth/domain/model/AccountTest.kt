package org.cescfe.bookpublishing.auth.domain.model

import org.cescfe.bookpublishing.auth.domain.exception.AuthDomainException
import org.cescfe.bookpublishing.auth.domain.model.enum.Permission
import org.cescfe.bookpublishing.auth.domain.model.enum.Role
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.UUID

class AccountTest {
    @Test
    fun `should create account with valid data`() {
        val id = UserId.generate()
        val username = Username("user@example.com")
        val passwordHash = PasswordHash("hashed")

        val account =
            Account(
                id = id,
                username = username,
                passwordHash = passwordHash,
                roles = setOf(Role.ADMIN, Role.USER),
                permissions = setOf(Permission.READ, Permission.WRITE),
            )

        assertEquals(id, account.id)
        assertEquals(username, account.username)
        assertEquals(passwordHash, account.passwordHash)
        assertEquals(setOf(Role.ADMIN, Role.USER), account.roles)
        assertEquals(setOf(Permission.READ, Permission.WRITE), account.permissions)
    }

    @Test
    fun `should generate user id`() {
        val id = UserId.generate()

        assertNotNull(id.value)
    }

    @Test
    fun `should parse user id from string`() {
        val uuid = UUID.randomUUID()
        val id = UserId.fromString(uuid.toString())

        assertEquals(uuid, id.value)
    }

    @Test
    fun `should keep user id value from constructor`() {
        val uuid = UUID.randomUUID()
        val id = UserId(uuid)

        assertEquals(uuid, id.value)
    }

    @Test
    fun `should throw for invalid user id format`() {
        val ex =
            assertThrows<AuthDomainException> {
                UserId.fromString("invalid")
            }

        assertEquals("USER_ID_INVALID_FORMAT", ex.subType)
    }

    @Test
    fun `should throw for blank username`() {
        val ex =
            assertThrows<AuthDomainException> {
                Username("  ")
            }

        assertEquals("USERNAME_CANNOT_BE_BLANK", ex.subType)
    }

    @Test
    fun `should throw for long username`() {
        val longUsername = "a".repeat(256)
        val ex =
            assertThrows<AuthDomainException> {
                Username(longUsername)
            }

        assertEquals("USERNAME_TOO_LONG", ex.subType)
    }

    @Test
    fun `should accept min and max username length`() {
        val minUsername = Username("a")
        val maxUsername = Username("a".repeat(255))

        assertEquals("a", minUsername.value)
        assertEquals(255, maxUsername.value.length)
    }

    @Test
    fun `should throw for blank password hash`() {
        val ex =
            assertThrows<AuthDomainException> {
                PasswordHash(" ")
            }

        assertEquals("PASSWORD_HASH_CANNOT_BE_BLANK", ex.subType)
    }
}
