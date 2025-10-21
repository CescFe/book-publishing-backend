package org.cescfe.bookpublishing.auth.application.port.input

interface LoginUseCase {
    fun execute(input: InputValues): OutputValues

    data class InputValues(
        val username: String,
        val password: String,
    )

    data class OutputValues(
        val accessToken: String,
        val expiresIn: Long,
        val scope: String,
        val userId: String,
    )
}
