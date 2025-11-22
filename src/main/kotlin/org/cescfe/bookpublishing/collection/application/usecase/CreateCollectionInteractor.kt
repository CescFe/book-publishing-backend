package org.cescfe.bookpublishing.collection.application.usecase

import org.cescfe.bookpublishing.collection.application.port.input.CreateCollectionUseCase
import org.cescfe.bookpublishing.collection.application.port.input.mapper.CreateCollectionUseCaseMapper
import org.cescfe.bookpublishing.collection.domain.model.Collection
import org.cescfe.bookpublishing.collection.domain.port.CollectionRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CreateCollectionInteractor(
    private val collectionRepository: CollectionRepository,
    private val mapper: CreateCollectionUseCaseMapper,
) : CreateCollectionUseCase {
    override fun execute(command: CreateCollectionUseCase.CreateCollectionCommand): Collection {
        val collection = mapper.toDomain(command)
        return collectionRepository.save(collection)
    }
}
