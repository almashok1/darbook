package kz.adamant.data.repository

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kz.adamant.data.mappers.toEntity
import kz.adamant.data.repository.datasources.BooksLocalDataSource
import kz.adamant.data.utils.networkBoundResource
import kz.adamant.domain.models.Book
import kz.adamant.domain.models.Genre
import kz.adamant.domain.models.Resource
import kz.adamant.domain.repository.BooksRepository

class BooksRepositoryImpl(
    private val localDataSource: BooksDataSource,
    private val remoteDataSource: BooksDataSource,
): BooksRepository {
    override suspend fun getAllBooks(shouldFetchFromNetwork: Boolean): Flow<Resource<List<Book>>> {
        return networkBoundResource(
            query = { localDataSource.getAllBooks() },
            fetch = { remoteDataSource.getAllBooks() },
            saveFetchResult = { items -> items.collect { saveBooksToDatabase(it) } },
            shouldFetch = {items -> shouldFetchFromNetwork}
        )
    }

    override suspend fun getAllGenres(): Flow<Resource<List<Genre>>> {
        return networkBoundResource(
            query = { localDataSource.getAllGenres() },
            fetch = { remoteDataSource.getAllGenres() },
            saveFetchResult = { items -> items.collect { saveGenresToDatabase(it) } },
            shouldFetch = { genres -> genres.isEmpty() }
        )
    }

    override suspend fun getBooksWithQuery(query: String): Flow<Resource<List<Book>>> {
        return (localDataSource as BooksLocalDataSource).getBooksWithQuery(query).flatMapConcat {
            flowOf(Resource.Success(it))
        }
    }

    override suspend fun getBooksWithFilteredGenresId(selectedGenres: List<Int>): Flow<Resource<List<Book>>> {
        return (localDataSource as BooksLocalDataSource).getBooksWithFilteredGenresId(selectedGenres).flatMapConcat {
            flowOf(Resource.Success(it))
        }
    }

    private suspend fun saveBooksToDatabase(books: List<Book>) {
        val local = localDataSource as BooksLocalDataSource
        local.resetBooksTable()
        local.saveAllBooks(books.map {bookDto -> bookDto.toEntity()})
    }

    private suspend fun saveGenresToDatabase(genres: List<Genre>) {
        Log.d("TAG", "saveGenresToDatabase: $genres")
        val local = localDataSource as BooksLocalDataSource
        local.resetGenresTable()
        local.saveAllGenres(genres.map {genresDto -> genresDto.toEntity()})
    }
}