package org.cescfe.bookpublishing.collection.domain.service

import org.cescfe.bookpublishing.collection.domain.exception.CollectionDomainException
import org.cescfe.bookpublishing.collection.domain.port.CollectionRepository
import org.springframework.stereotype.Component

@Component
class CollectionDomainServiceImpl(
    private val collectionRepository: CollectionRepository,
) : CollectionDomainService {
    override fun ensureNameUniqueness(name: String?) {
        if (name != null && collectionRepository.existsByName(name)) {
            throw CollectionDomainException.nameAlreadyExists(name)
        }
    }

    override fun ensureNameUniquenessForUpdate(
        name: String?,
        collectionId: String,
    ) {
        if (name != null) {
            val existingCollection = collectionRepository.findByName(name)
            if (existingCollection != null && existingCollection.id.value.toString() != collectionId) {
                throw CollectionDomainException.nameAlreadyExists(name)
            }
        }
    }
}
