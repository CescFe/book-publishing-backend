package org.cescfe.bookpublishing.author.infrastructure.adapters.output.persistence.exception

class RoleNotFoundException(
    roleName: String,
    cause: Throwable? = null,
) : RuntimeException("Role '$roleName' not found in database", cause) {
    companion object {
        fun forRoleName(roleName: String): RoleNotFoundException = RoleNotFoundException(roleName)

        fun forRoleNameWithCause(
            roleName: String,
            cause: Throwable,
        ): RoleNotFoundException = RoleNotFoundException(roleName, cause)
    }
}
