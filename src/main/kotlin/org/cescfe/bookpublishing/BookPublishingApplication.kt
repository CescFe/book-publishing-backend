package org.cescfe.bookpublishing

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BookPublishingApplication

fun main(args: Array<String>) {
    runApplication<BookPublishingApplication>(*args)
}
