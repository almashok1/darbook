package kz.adamant.data.repository

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kz.adamant.data.mappers.toEntity
import kz.adamant.data.repository.datasources.BooksLocalDataSource
import kz.adamant.data.repository.datasources.BooksRemoteDataSource
import kz.adamant.data.utils.networkBoundResource
import kz.adamant.domain.models.Book
import kz.adamant.domain.models.ReadingBook
import kz.adamant.domain.models.Resource
import kz.adamant.domain.repository.BooksRepository

class BooksRepositoryImpl(
    private val localDataSource: BooksLocalDataSource,
    private val remoteDataSource: BooksRemoteDataSource,
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

    override suspend fun getAllReadingBooks(
        shouldFetchFromNetwork: Boolean,
    ): Flow<Resource<List<ReadingBook>>> {
        return networkBoundResource(
            query = { localDataSource.getAllReadingBooks() },
            fetch = { remoteDataSource.getAllReadingBooks() },
            saveFetchResult = { items -> saveReadingBooksToDatabase(items) },
            shouldFetch = { items -> shouldFetchFromNetwork },
            onFetchFailed = { t -> Log.d("TAG", "getAllReadingBooks: $t") }
        )
    }

    override suspend fun getAllBooksNewlyAdded(
        shouldFetchFromNetwork: Boolean,
    ): Flow<Resource<List<Book>>> {
        return networkBoundResource(
            query = { localDataSource.getAllBooksNewlyAdded() },
            fetch = { remoteDataSource.getAllBooksNewlyAdded() },
            saveFetchResult = { items -> saveBooksToDatabase(items) },
            shouldFetch = { items -> shouldFetchFromNetwork },
            onFetchFailed = { t -> Log.d("TAG", "getAllBooksNewlyAdded: $t") }
        )
    }

    override suspend fun getBookByIsbn(isbn: String): Flow<List<Book>> {
        return localDataSource.getBookByIsbn(isbn)
    }

    private fun getBooksWithQuery(query: String): Flow<Resource<List<Book>>> {
        return localDataSource.getBooksWithQuery(query).flatMapConcat {
            flowOf(Resource.Success(it))
        }
    }

    private fun getBooksWithFilteredGenresId(selectedGenres: List<Int>): Flow<Resource<List<Book>>> {
        return localDataSource.getBooksWithFilteredGenresId(selectedGenres).flatMapConcat {
            flowOf(Resource.Success(it))
        }
    }

    private fun getBooksWithQueryAndFilter(query: String, selectedGenres: List<Int>): Flow<Resource<List<Book>>> {
        return localDataSource.getBooksWithQueryAndFilter(query, selectedGenres).flatMapConcat {
            flowOf(Resource.Success(it))
        }
    }

    private suspend fun saveBooksToDatabase(books: List<Book>) {
        localDataSource.resetBooksTable()
        localDataSource.saveAllBooks(books.map {bookDto -> bookDto.toEntity()})
    }

    private suspend fun saveReadingBooksToDatabase(readingBooks: List<ReadingBook>) {
        localDataSource.resetReadingTable()
        localDataSource.saveAllReadingEntity(
            readingBooks.map {
                it.book?.let { book -> localDataSource.updateOrInsertBook(book.toEntity()) }
                it.toEntity()
            }
        )
    }
}