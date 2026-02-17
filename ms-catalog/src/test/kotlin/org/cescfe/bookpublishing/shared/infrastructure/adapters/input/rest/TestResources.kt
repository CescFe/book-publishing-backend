package org.cescfe.bookpublishing.shared.infrastructure.adapters.input.rest

import org.springframework.core.io.ClassPathResource

object TestResources {
    fun json(path: String): String =
        ClassPathResource(path)
            .inputStream
            .bufferedReader()
            .use { it.readText() }
}
