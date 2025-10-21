package org.cescfe.bookpublishing.auth.application.port.input.interactor

import org.cescfe.bookpublishing.auth.application.port.input.LoginUseCase
import org.cescfe.bookpublishing.auth.domain.service.UserService
import org.cescfe.bookpublishing.shared.infrastructure.adapters.input.security.JwtUtil
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class LoginImpl(
    private val authenticationManager: AuthenticationManager,
    private val jwtUtil: JwtUtil,
    private val userService: UserService,
) : LoginUseCase {
    override fun execute(input: LoginUseCase.InputValues): LoginUseCase.OutputValues {
        val authentication: Authentication =
            authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(input.username, input.password),
            )

        val userDetails = userService.loadUserByUsername(input.username)
        val token = jwtUtil.generateToken(userDetails)

        return LoginUseCase.OutputValues(
            accessToken = token,
            expiresIn = 86400L,
            scope = "read write delete",
            userId = UUID.randomUUID().toString(),
        )
    }
}
