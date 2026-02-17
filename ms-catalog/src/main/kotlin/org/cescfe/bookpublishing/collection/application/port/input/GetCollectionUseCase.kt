package org.cescfe.bookpublishing.collection.application.port.input

import org.cescfe.bookpublishing.collection.domain.model.Collection

interface GetCollectionUseCase {
    fun execute(query: Query): Collection

    data class Query(
        val collectionId: String,
    )
}
