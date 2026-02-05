package org.cescfe.bookpublishing.auth.application.port.input.interactor

import org.cescfe.bookpublishing.auth.application.port.input.LoginUseCase
import org.cescfe.bookpublishing.auth.application.port.output.Clock
import org.cescfe.bookpublishing.auth.application.port.output.PasswordHasher
import org.cescfe.bookpublishing.auth.application.port.output.TokenPayload
import org.cescfe.bookpublishing.auth.application.port.output.TokenService
import org.cescfe.bookpublishing.auth.application.port.output.UserRepository
import org.cescfe.bookpublishing.auth.domain.exception.AuthDomainException
import org.cescfe.bookpublishing.auth.domain.model.AuthUser
import org.cescfe.bookpublishing.auth.domain.model.PasswordHash
import org.cescfe.bookpublishing.auth.domain.model.UserId
import org.cescfe.bookpublishing.auth.domain.model.Username
import org.cescfe.bookpublishing.auth.domain.model.enum.Role
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.time.Instant
import java.util.UUID

class LoginImplTest {
    private lateinit var userRepository: UserRepository
    private lateinit var passwordHasher: PasswordHasher
    private lateinit var tokenService: TokenService
    private lateinit var clock: Clock
    private lateinit var loginImpl: LoginImpl

    @BeforeEach
    fun setup() {
        userRepository = mock()
        passwordHasher = mock()
        tokenService = mock()
        clock = mock()
        loginImpl = LoginImpl(userRepository, passwordHasher, tokenService, clock)
    }

    @Test
    fun `should return access token on successful authentication`() {
        val input =
            LoginUseCase.InputValues(
                username = "user@example.com",
                password = "password123",
            )
        val userId = UserId(UUID.randomUUID())
        val authUser =
            AuthUser(
                id = userId,
                username = Username(input.username),
                passwordHash = PasswordHash("hashed"),
                roles = setOf(Role.USER),
            )
        val expectedToken = "jwt.token.here"
        val expectedExpiration = 3600L
        val now = Instant.parse("2030-01-01T00:00:00Z")

        whenever(userRepository.findByUsername(Username(input.username))).thenReturn(authUser)
        whenever(passwordHasher.matches(input.password, authUser.passwordHash)).thenReturn(true)
        whenever(tokenService.getExpirationTime()).thenReturn(expectedExpiration)
        whenever(clock.now()).thenReturn(now)
        whenever(tokenService.issueToken(org.mockito.kotlin.any<TokenPayload>())).thenReturn(expectedToken)

        val result = loginImpl.execute(input)

        assertEquals(expectedToken, result.accessToken)
        assertEquals(expectedExpiration, result.expiresIn)
        assertEquals("read", result.scope)
        assertEquals(userId.value.toString(), result.userId)
        verify(tokenService).issueToken(
            TokenPayload(
                userId = userId,
                username = Username(input.username),
                roles = setOf(Role.USER),
                permissions = emptySet(),
                expiresAt = now.plusSeconds(expectedExpiration),
            ),
        )
    }

    @Test
    fun `should throw exception for invalid credentials`() {
        val input =
            LoginUseCase.InputValues(
                username = "user@example.com",
                password = "wrongPassword",
            )

        whenever(userRepository.findByUsername(Username(input.username))).thenReturn(null)

        val ex =
            assertThrows<AuthDomainException> {
                loginImpl.execute(input)
            }
        assertEquals("INVALID_CREDENTIALS", ex.subType)
    }

    @Test
    fun `should throw exception when password does not match`() {
        val input =
            LoginUseCase.InputValues(
                username = "user@example.com",
                password = "wrongPassword",
            )
        val authUser =
            AuthUser(
                id = UserId(UUID.randomUUID()),
                username = Username(input.username),
                passwordHash = PasswordHash("hashed"),
                roles = setOf(Role.USER),
            )

        whenever(userRepository.findByUsername(Username(input.username))).thenReturn(authUser)
        whenever(passwordHasher.matches(input.password, authUser.passwordHash)).thenReturn(false)

        val ex =
            assertThrows<AuthDomainException> {
                loginImpl.execute(input)
            }
        assertEquals("INVALID_CREDENTIALS", ex.subType)
    }

    @Test
    fun `should include correct scope based on admin role`() {
        val input =
            LoginUseCase.InputValues(
                username = "admin@example.com",
                password = "admin123",
            )
        val authUser =
            AuthUser(
                id = UserId(UUID.randomUUID()),
                username = Username(input.username),
                passwordHash = PasswordHash("hashed"),
                roles = setOf(Role.ADMIN, Role.USER),
            )

        whenever(userRepository.findByUsername(Username(input.username))).thenReturn(authUser)
        whenever(passwordHasher.matches(input.password, authUser.passwordHash)).thenReturn(true)
        whenever(tokenService.getExpirationTime()).thenReturn(3600L)
        whenever(clock.now()).thenReturn(Instant.parse("2030-01-01T00:00:00Z"))
        whenever(tokenService.issueToken(org.mockito.kotlin.any<TokenPayload>())).thenReturn("token")

        val result = loginImpl.execute(input)

        assertEquals("read write delete", result.scope)
    }

    @Test
    fun `should include correct scope based on user role`() {
        val input =
            LoginUseCase.InputValues(
                username = "user@example.com",
                password = "password123",
            )
        val authUser =
            AuthUser(
                id = UserId(UUID.randomUUID()),
                username = Username(input.username),
                passwordHash = PasswordHash("hashed"),
                roles = setOf(Role.USER),
            )

        whenever(userRepository.findByUsername(Username(input.username))).thenReturn(authUser)
        whenever(passwordHasher.matches(input.password, authUser.passwordHash)).thenReturn(true)
        whenever(tokenService.getExpirationTime()).thenReturn(3600L)
        whenever(clock.now()).thenReturn(Instant.parse("2030-01-01T00:00:00Z"))
        whenever(tokenService.issueToken(org.mockito.kotlin.any<TokenPayload>())).thenReturn("token")

        val result = loginImpl.execute(input)

        assertEquals("read", result.scope)
    }
}
