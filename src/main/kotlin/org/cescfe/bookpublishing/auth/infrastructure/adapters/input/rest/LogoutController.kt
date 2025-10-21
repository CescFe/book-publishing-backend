package org.cescfe.bookpublishing.auth.infrastructure.adapters.input.rest

import io.swagger.v3.oas.annotations.tags.Tag
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.LogoutApi
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "Logout")
class LogoutController : LogoutApi {
    override fun logout(): ResponseEntity<Unit> {
        SecurityContextHolder.clearContext()
        return ResponseEntity.noContent().build()
    }
}
