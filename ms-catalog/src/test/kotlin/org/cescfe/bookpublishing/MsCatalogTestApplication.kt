package org.cescfe.bookpublishing

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration

@SpringBootApplication(
    exclude = [
        LiquibaseAutoConfiguration::class,
    ],
)
class MsCatalogTestApplication
