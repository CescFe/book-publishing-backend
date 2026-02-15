package org.cescfe.bookpublishing.collection.application.port.input.usecase

import org.cescfe.bookpublishing.collection.application.port.input.ListAllCollectionsUseCase
import org.cescfe.bookpublishing.collection.application.port.input.mapper.ListCollectionsUseCaseMapper
import org.cescfe.bookpublishing.collection.domain.model.CollectionSummary
import org.cescfe.bookpublishing.collection.domain.port.CollectionRepository
import org.cescfe.bookpublishing.shared.domain.model.NonPaginatedResult
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ListAllCollectionsInteractor(
    private val collectionRepository: CollectionRepository,
    private val mapper: ListCollectionsUseCaseMapper,
) : ListAllCollectionsUseCase {
    override fun execute(): NonPaginatedResult<CollectionSummary> {
        val collections = collectionRepository.findAllSummary()
        val totalCount = collectionRepository.countAll()

        return mapper.toNonPaginatedResult(collections, totalCount)
    }
}
