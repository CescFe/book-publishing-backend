package org.cescfe.bookpublishing.author.infrastructure.adapters.output.persistence.objectMothers

import org.cescfe.bookpublishing.author.infrastructure.adapters.output.persistence.entity.RoleEntity

object RoleEntityObjectMother {
    fun createAuthorRole(): RoleEntity = RoleEntity(id = 1L, name = "AUTHOR", description = "Book author")

    fun createIllustratorRole(): RoleEntity =
        RoleEntity(id = 2L, name = "ILLUSTRATOR", description = "Book illustrator")

    fun createTranslatorRole(): RoleEntity = RoleEntity(id = 3L, name = "TRANSLATOR", description = "Book translator")

    fun createCuratorRole(): RoleEntity = RoleEntity(id = 4L, name = "CURATOR", description = "Book curator")
}
