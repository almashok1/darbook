package kz.adamant.domain.usecases

import kotlinx.coroutines.flow.Flow
import kz.adamant.domain.models.Genre
import kz.adamant.domain.models.Resource
import kz.adamant.domain.repository.GenreRepository

class GetGenreUseCase(
    private val genreRepository: GenreRepository
) {
    suspend operator fun invoke(genreId: Int): Flow<Resource<Genre?>> {
        return genreRepository.getGenreById(genreId)
    }
}