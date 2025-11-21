package org.cescfe.bookpublishing.collection.application.port.input.interactor

import org.cescfe.bookpublishing.collection.application.port.input.CreateCollectionUseCase
import org.cescfe.bookpublishing.collection.application.port.input.mapper.CreateCollectionUseCaseMapper
import org.cescfe.bookpublishing.collection.domain.model.Collection
import org.cescfe.bookpublishing.collection.domain.port.CollectionRepositoryView
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CreateCollectionImpl(
    private val collectionRepository: CollectionRepositoryView,
    private val mapper: CreateCollectionUseCaseMapper,
) : CreateCollectionUseCase {
    override fun execute(input: CreateCollectionUseCase.InputValues): Collection {
        val collection = mapper.toDomain(input)
        return collectionRepository.save(collection)
    }
}
