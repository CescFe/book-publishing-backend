package org.cescfe.bookpublishing.collection.application.port.input.usecase

import org.cescfe.bookpublishing.collection.application.port.input.GetCollectionUseCase
import org.cescfe.bookpublishing.collection.domain.exception.CollectionDomainException
import org.cescfe.bookpublishing.collection.domain.model.Collection
import org.cescfe.bookpublishing.collection.domain.model.CollectionId
import org.cescfe.bookpublishing.collection.domain.port.CollectionRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class GetCollectionInteractor(
    private val collectionRepository: CollectionRepository,
) : GetCollectionUseCase {
    override fun execute(query: GetCollectionUseCase.Query): Collection {
        val collectionId = CollectionId.fromString(query.collectionId)
        return collectionRepository.findById(collectionId)
            ?: throw CollectionDomainException.collectionNotFound(query.collectionId)
    }
}
