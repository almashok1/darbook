package kz.adamant.data.repository

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kz.adamant.data.mappers.toDomain
import kz.adamant.data.mappers.toEntity
import kz.adamant.data.repository.datasources.BooksLocalDataSource
import kz.adamant.data.utils.networkBoundResource
import kz.adamant.domain.models.Book
import kz.adamant.domain.models.Genre
import kz.adamant.domain.models.ReadingBook
import kz.adamant.domain.models.Resource
import kz.adamant.domain.repository.BooksRepository

class BooksRepositoryImpl(
    private val localDataSource: BooksDataSource,
    private val remoteDataSource: BooksDataSource,
): BooksRepository {
    override suspend fun getAllBooks(query: String?, selectedGenres: List<Int>?, shouldFetchFromNetwork: Boolean): Flow<Resource<List<Book>>> {
        return if (query.isNullOrEmpty() || query.isNullOrBlank()) {
            if (selectedGenres.isNullOrEmpty())
                networkBoundResource(
                    query = { localDataSource.getAllBooks() },
                    fetch = { remoteDataSource.getAllBooks() },
                    saveFetchResult = { items -> saveBooksToDatabase(items) },
                    shouldFetch = {items -> items.isEmpty() || shouldFetchFromNetwork}
                )
            else
                getBooksWithFilteredGenresId(selectedGenres)
        } else {
            if (selectedGenres.isNullOrEmpty()) getBooksWithQuery(query)
            else getBooksWithQueryAndFilter(query, selectedGenres)
        }
    }

    override suspend fun getAllGenres(shouldFetchFromNetwork: Boolean): Flow<Resource<List<Genre>>> {
        return networkBoundResource(
            query = { localDataSource.getAllGenres() },
            fetch = { remoteDataSource.getAllGenres() },
            saveFetchResult = { items -> saveGenresToDatabase(items) },
            shouldFetch = { genres -> genres.isEmpty() || shouldFetchFromNetwork }
        )
    }

    override suspend fun getAllReadingBooks(
        shouldFetchFromNetwork: Boolean,
        fetchTopN: Int?
    ): Flow<Resource<List<ReadingBook>>> {
        return networkBoundResource(
            query = { localDataSource.getAllReadingBooks(fetchTopN) },
            fetch = { remoteDataSource.getAllReadingBooks() },
            saveFetchResult = { items -> saveReadingBooksToDatabase(items) },
            shouldFetch = { items -> shouldFetchFromNetwork },
            onFetchFailed = { t -> Log.d("TAG", "getAllReadingBooks: $t") }
        )
    }

    override suspend fun getAllBooksNewlyAdded(
        shouldFetchFromNetwork: Boolean,
        fetchTopN: Int?
    ): Flow<Resource<List<Book>>> {
        return networkBoundResource(
            query = { localDataSource.getAllBooksNewlyAdded(fetchTopN) },
            fetch = { remoteDataSource.getAllBooksNewlyAdded() },
            saveFetchResult = { items -> saveBooksToDatabase(items) },
            shouldFetch = { items -> shouldFetchFromNetwork },
            onFetchFailed = { t -> Log.d("TAG", "getAllBooksNewlyAdded: $t") }
        )
    }

    private fun getBooksWithQuery(query: String): Flow<Resource<List<Book>>> {
        return (localDataSource as BooksLocalDataSource).getBooksWithQuery(query).flatMapConcat {
            flowOf(Resource.Success(it))
        }
    }

    private fun getBooksWithFilteredGenresId(selectedGenres: List<Int>): Flow<Resource<List<Book>>> {
        return (localDataSource as BooksLocalDataSource).getBooksWithFilteredGenresId(selectedGenres).flatMapConcat {
            flowOf(Resource.Success(it))
        }
    }

    private fun getBooksWithQueryAndFilter(query: String, selectedGenres: List<Int>): Flow<Resource<List<Book>>> {
        return (localDataSource as BooksLocalDataSource).getBooksWithQueryAndFilter(query, selectedGenres).flatMapConcat {
            flowOf(Resource.Success(it))
        }
    }

    private suspend fun saveBooksToDatabase(books: List<Book>) {
        val local = localDataSource as BooksLocalDataSource
        local.resetBooksTable()
        local.saveAllBooks(books.map {bookDto -> bookDto.toEntity()})
    }

    private suspend fun saveGenresToDatabase(genres: List<Genre>) {
        val local = localDataSource as BooksLocalDataSource
        local.resetGenresTable()
        local.saveAllGenres(genres.map {genresDto -> genresDto.toEntity()})
    }

    private suspend fun saveReadingBooksToDatabase(readingBooks: List<ReadingBook>) {
        val local = localDataSource as BooksLocalDataSource
        local.resetReadingTable()
        local.saveAllReadingEntity(
            readingBooks.map {
                it.book?.let { book -> local.updateOrInsertBook(book.toEntity()) }
                it.toDomain()
            }
        )
    }
}