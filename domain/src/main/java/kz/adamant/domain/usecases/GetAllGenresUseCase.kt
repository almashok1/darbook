package kz.adamant.domain.usecases

import kotlinx.coroutines.flow.Flow
import kz.adamant.domain.models.Genre
import kz.adamant.domain.models.Resource
import kz.adamant.domain.repository.BooksRepository

class GetAllGenresUseCase(
    private val booksRepository: BooksRepository
) {
    suspend operator fun invoke(): Flow<Resource<List<Genre>>> {
        return booksRepository.getAllGenres()
    }
}