package org.cescfe.bookpublishing

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
class BookPublishingApplication

fun main(args: Array<String>) {
    runApplication<BookPublishingApplication>(*args)
}
