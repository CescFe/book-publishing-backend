package org.cescfe.bookpublishing.auth.infrastructure.adapters.input.rest

import io.swagger.v3.oas.annotations.tags.Tag
import org.cescfe.bookpublishing.auth.application.port.input.LoginUseCase
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.LoginApi
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.Login200ResponseDTO
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.LoginRequestDTO
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "Login")
class LoginController(
    private val loginUseCase: LoginUseCase,
) : LoginApi {
    override fun login(loginRequestDTO: LoginRequestDTO): ResponseEntity<Login200ResponseDTO> {
        val result =
            loginUseCase.execute(
                LoginUseCase.InputValues(
                    username = loginRequestDTO.username,
                    password = loginRequestDTO.password,
                ),
            )

        val response =
            Login200ResponseDTO(
                accessToken = result.accessToken,
                tokenType = Login200ResponseDTO.TokenType.BEARER,
                expiresIn = result.expiresIn.toInt(),
                scope = result.scope,
                userId = result.userId,
            )

        return ResponseEntity.ok(response)
    }
}
