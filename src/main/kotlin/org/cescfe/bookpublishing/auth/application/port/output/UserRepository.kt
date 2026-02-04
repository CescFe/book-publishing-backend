package org.cescfe.bookpublishing.auth.application.port.output

import org.cescfe.bookpublishing.auth.domain.model.Account
import org.cescfe.bookpublishing.auth.domain.model.UserId
import org.cescfe.bookpublishing.auth.domain.model.Username

interface UserRepository {
    fun findById(id: UserId): Account?

    fun findByUsername(username: Username): Account?
}
