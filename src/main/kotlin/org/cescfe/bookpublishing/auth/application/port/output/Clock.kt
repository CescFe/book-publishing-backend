package org.cescfe.bookpublishing.auth.application.port.output

import java.time.Instant

interface Clock {
    fun now(): Instant
}
