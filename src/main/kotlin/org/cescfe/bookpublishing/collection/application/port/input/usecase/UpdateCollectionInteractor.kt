package org.cescfe.bookpublishing.collection.application.port.input.usecase

import org.cescfe.bookpublishing.collection.application.port.input.UpdateCollectionUseCase
import org.cescfe.bookpublishing.collection.application.port.input.mapper.UpdateCollectionUseCaseMapper
import org.cescfe.bookpublishing.collection.domain.exception.CollectionDomainException
import org.cescfe.bookpublishing.collection.domain.model.Collection
import org.cescfe.bookpublishing.collection.domain.model.CollectionId
import org.cescfe.bookpublishing.collection.domain.port.CollectionRepository

class UpdateCollectionInteractor(
    private val collectionRepository: CollectionRepository,
    private val mapper: UpdateCollectionUseCaseMapper,
) : UpdateCollectionUseCase {
    override fun execute(
        collectionId: String,
        command: UpdateCollectionUseCase.Command,
    ): Collection {
        val collectionIdDomain = CollectionId.fromString(collectionId)
        val existingCollection =
            collectionRepository.findById(collectionIdDomain)
                ?: throw CollectionDomainException.collectionNotFound(collectionId)

        val updatedCollection = mapper.toDomain(command, existingCollection)

        return collectionRepository.save(updatedCollection)
    }
}
