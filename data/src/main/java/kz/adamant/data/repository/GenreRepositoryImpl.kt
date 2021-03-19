package kz.adamant.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kz.adamant.data.mappers.toDomain
import kz.adamant.data.mappers.toEntity
import kz.adamant.data.repository.datasources.BooksLocalDataSource
import kz.adamant.data.utils.networkBoundResource
import kz.adamant.domain.models.Genre
import kz.adamant.domain.models.Resource
import kz.adamant.domain.repository.GenreRepository

class GenreRepositoryImpl(
    private val localDataSource: BooksDataSource,
    private val remoteDataSource: BooksDataSource,
): GenreRepository {
    override suspend fun getGenreById(genreId: Int): Flow<Resource<Genre?>> {
        return networkBoundResource(
            query = { localDataSource.getGenreById(genreId) },
            fetch = { remoteDataSource.getGenreById(genreId) },
            saveFetchResult = { item -> saveGenre(item) },
            shouldFetch = { genre -> genre == null  }
        )
    }

    override suspend fun getAllGenres(shouldFetchFromNetwork: Boolean): Flow<Resource<List<Genre>>> {
        return networkBoundResource(
            query = { localDataSource.getAllGenres() },
            fetch = { remoteDataSource.getAllGenres() },
            saveFetchResult = { items -> saveGenresToDatabase(items) },
            shouldFetch = { genres -> genres.isEmpty() || shouldFetchFromNetwork }
        )
    }

    private suspend fun saveGenresToDatabase(genres: List<Genre>) {
        val local = localDataSource as BooksLocalDataSource
        local.resetGenresTable()
        local.saveAllGenres(genres.map {genresDto -> genresDto.toEntity()})
    }

    private suspend fun saveGenre(genre: Genre?) {
        if (genre == null) return
        val local = localDataSource as BooksLocalDataSource
        local.updateGenre(genre.toEntity())
    }
}