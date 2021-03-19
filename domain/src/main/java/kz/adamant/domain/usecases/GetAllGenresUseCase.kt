package kz.adamant.domain.usecases

import kotlinx.coroutines.flow.Flow
import kz.adamant.domain.models.Genre
import kz.adamant.domain.models.Resource
import kz.adamant.domain.repository.BooksRepository
import kz.adamant.domain.repository.GenreRepository

class GetAllGenresUseCase(
    private val genreRepository: GenreRepository
) {
    suspend operator fun invoke(shouldFetchFromNetwork: Boolean): Flow<Resource<List<Genre>>> {
        return genreRepository.getAllGenres(shouldFetchFromNetwork)
    }
}