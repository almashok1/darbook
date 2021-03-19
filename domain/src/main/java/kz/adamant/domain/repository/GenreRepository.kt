package kz.adamant.domain.repository

import kotlinx.coroutines.flow.Flow
import kz.adamant.domain.models.Genre
import kz.adamant.domain.models.Resource

interface GenreRepository {
    suspend fun getGenreById(genreId: Int): Flow<Resource<Genre?>>
    suspend fun getAllGenres(shouldFetchFromNetwork: Boolean): Flow<Resource<List<Genre>>>
}