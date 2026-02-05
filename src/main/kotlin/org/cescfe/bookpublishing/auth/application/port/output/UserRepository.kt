package org.cescfe.bookpublishing.auth.application.port.output

import org.cescfe.bookpublishing.auth.domain.model.AuthUser
import org.cescfe.bookpublishing.auth.domain.model.UserId
import org.cescfe.bookpublishing.auth.domain.model.Username

interface UserRepository {
    fun findById(id: UserId): AuthUser?

    fun findByUsername(username: Username): AuthUser?
}
