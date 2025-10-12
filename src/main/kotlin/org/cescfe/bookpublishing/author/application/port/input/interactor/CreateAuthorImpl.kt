package org.cescfe.bookpublishing.author.application.port.input.interactor

import org.cescfe.bookpublishing.author.application.port.input.CreateAuthorUseCase
import org.cescfe.bookpublishing.author.application.port.input.mapper.CreateAuthorUseCaseMapper
import org.cescfe.bookpublishing.author.domain.exception.AuthorDomainException
import org.cescfe.bookpublishing.author.domain.model.Author
import org.cescfe.bookpublishing.author.domain.port.AuthorRepositoryView
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CreateAuthorImpl(
    private val authorRepository: AuthorRepositoryView,
    private val mapper: CreateAuthorUseCaseMapper,
) : CreateAuthorUseCase {
    override fun execute(input: CreateAuthorUseCase.InputValues): Author {
        validateEmailUniqueness(input.email)
        val author = mapper.toDomain(input)
        return authorRepository.save(author)
    }

    private fun validateEmailUniqueness(email: String?) {
        if (email != null) {
            val existingAuthor = authorRepository.findByEmail(email)
            if (existingAuthor != null) {
                throw AuthorDomainException.emailAlreadyExists(email)
            }
        }
    }
}
