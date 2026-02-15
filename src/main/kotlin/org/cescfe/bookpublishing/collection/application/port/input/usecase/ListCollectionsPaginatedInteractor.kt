package org.cescfe.bookpublishing.collection.application.port.input.usecase

import org.cescfe.bookpublishing.collection.application.port.input.ListCollectionsPaginatedUseCase
import org.cescfe.bookpublishing.collection.application.port.input.mapper.ListCollectionsUseCaseMapper
import org.cescfe.bookpublishing.collection.domain.model.CollectionSummary
import org.cescfe.bookpublishing.collection.domain.port.CollectionRepository
import org.cescfe.bookpublishing.shared.domain.model.PaginatedResult
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ListCollectionsPaginatedInteractor(
    private val collectionRepository: CollectionRepository,
    private val mapper: ListCollectionsUseCaseMapper,
) : ListCollectionsPaginatedUseCase {
    override fun execute(query: ListCollectionsPaginatedUseCase.Query): PaginatedResult<CollectionSummary> {
        val collections = collectionRepository.findAllSummary(query.page, query.limit)
        val totalCount = collectionRepository.countAll()

        return mapper.toPaginatedResult(collections, totalCount, query.page, query.limit)
    }
}
