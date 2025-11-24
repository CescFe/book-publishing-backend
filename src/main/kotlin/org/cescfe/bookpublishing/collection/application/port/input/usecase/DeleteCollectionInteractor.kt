package org.cescfe.bookpublishing.collection.application.port.input.usecase

import org.cescfe.bookpublishing.collection.application.port.input.DeleteCollectionUseCase
import org.cescfe.bookpublishing.collection.domain.exception.CollectionDomainException
import org.cescfe.bookpublishing.collection.domain.model.CollectionId
import org.cescfe.bookpublishing.collection.domain.port.CollectionRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class DeleteCollectionInteractor(
    private val collectionRepository: CollectionRepository,
) : DeleteCollectionUseCase {
    override fun execute(command: DeleteCollectionUseCase.Command) {
        val collectionId = CollectionId.fromString(command.collectionId)

        collectionRepository.findById(collectionId)
            ?: throw CollectionDomainException.collectionNotFound(command.collectionId)

        collectionRepository.deleteById(collectionId)
    }
}
