package org.cescfe.bookpublishing.collection.domain.service

interface CollectionDomainService {
    fun ensureNameUniqueness(name: String?)

    fun ensureNameUniquenessForUpdate(
        name: String?,
        collectionId: String,
    )
}
