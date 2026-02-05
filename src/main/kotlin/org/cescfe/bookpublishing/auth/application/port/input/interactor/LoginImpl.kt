package org.cescfe.bookpublishing.auth.application.port.input.interactor

import org.cescfe.bookpublishing.auth.application.port.input.LoginUseCase
import org.cescfe.bookpublishing.auth.application.port.output.Clock
import org.cescfe.bookpublishing.auth.application.port.output.PasswordHasher
import org.cescfe.bookpublishing.auth.application.port.output.TokenPayload
import org.cescfe.bookpublishing.auth.application.port.output.TokenService
import org.cescfe.bookpublishing.auth.application.port.output.UserRepository
import org.cescfe.bookpublishing.auth.domain.exception.AuthDomainException
import org.cescfe.bookpublishing.auth.domain.model.Username
import org.cescfe.bookpublishing.auth.domain.policy.ScopePolicy
import org.springframework.stereotype.Service

@Service
class LoginImpl(
    private val userRepository: UserRepository,
    private val passwordHasher: PasswordHasher,
    private val tokenService: TokenService,
    private val clock: Clock,
    private val scopePolicy: ScopePolicy,
) : LoginUseCase {
    override fun execute(input: LoginUseCase.InputValues): LoginUseCase.OutputValues {
        val username = Username(input.username)
        val user =
            userRepository.findByUsername(username)
                ?: throw AuthDomainException.invalidCredentials()

        if (!passwordHasher.matches(input.password, user.passwordHash)) {
            throw AuthDomainException.invalidCredentials()
        }

        val expiresIn = tokenService.getExpirationTime()
        val expiresAt = clock.now().plusSeconds(expiresIn)
        val token =
            tokenService.issueToken(
                TokenPayload(
                    userId = user.id,
                    username = user.username,
                    roles = user.roles,
                    permissions = user.permissions,
                    expiresAt = expiresAt,
                ),
            )
        val scope = scopePolicy.scopeFor(user.roles)

        return LoginUseCase.OutputValues(
            accessToken = token,
            expiresIn = expiresIn,
            scope = scope,
            userId = user.id.value.toString(),
        )
    }
}
