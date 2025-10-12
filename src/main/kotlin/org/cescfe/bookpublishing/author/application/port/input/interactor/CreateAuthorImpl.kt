package org.cescfe.bookpublishing.author.application.port.input.interactor

import org.cescfe.bookpublishing.author.application.port.input.CreateAuthorUseCase
import org.cescfe.bookpublishing.author.domain.exception.AuthorDomainException
import org.cescfe.bookpublishing.author.domain.model.Author
import org.cescfe.bookpublishing.author.domain.model.AuthorId
import org.cescfe.bookpublishing.author.domain.model.AuthorRole
import org.cescfe.bookpublishing.author.domain.model.Biography
import org.cescfe.bookpublishing.author.domain.model.Email
import org.cescfe.bookpublishing.author.domain.model.FullName
import org.cescfe.bookpublishing.author.domain.model.Pseudonym
import org.cescfe.bookpublishing.author.domain.model.Website
import org.cescfe.bookpublishing.author.domain.port.AuthorRepositoryView
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CreateAuthorImpl(
    private val authorRepository: AuthorRepositoryView,
) : CreateAuthorUseCase {
    override fun execute(input: CreateAuthorUseCase.InputValues): Author {
        validateEmailUniqueness(input.email)
        val author = createAuthorFromInput(input)
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

    private fun createAuthorFromInput(input: CreateAuthorUseCase.InputValues): Author =
        Author(
            id = AuthorId.generate(),
            fullName = FullName(input.fullName),
            roles = input.roles.map { AuthorRole.fromString(it) }.toSet(),
            pseudonym = input.pseudonym?.let { Pseudonym(it) },
            biography = input.biography?.let { Biography(it) },
            email = input.email?.let { Email(it) },
            website = input.website?.let { Website(it) },
        )
}
