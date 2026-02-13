package org.cescfe.bookpublishing.collection.application.port.input

import org.cescfe.bookpublishing.shared.domain.model.NonPaginatedResult
import org.cescfe.bookpublishing.collection.domain.model.CollectionSummary

interface ListCollectionsUseCase {
    fun execute(): NonPaginatedResult<CollectionSummary>
}
