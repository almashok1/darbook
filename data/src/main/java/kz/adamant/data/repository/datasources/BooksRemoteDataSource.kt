package kz.adamant.data.repository.datasources

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kz.adamant.data.mappers.toDomain
import kz.adamant.data.remote.api.BooksApiService
import kz.adamant.data.repository.BooksDataSource
import kz.adamant.domain.models.Book
import kz.adamant.domain.models.Genre
import kz.adamant.domain.models.ReadingBook

class BooksRemoteDataSource(
   private val apiService: BooksApiService
): BooksDataSource {
    override suspend fun getAllBooks(): Flow<List<Book>> {
        val responseBooks = apiService.allBooks()

        return if (responseBooks.isSuccessful) {
            flowOf(responseBooks.body()!!.map { it.toDomain() })
        }
        else
            emptyFlow()
    }

    override suspend fun getAllGenres(): Flow<List<Genre>> {
        val responseGenres = apiService.allGenres()
        return if (responseGenres.isSuccessful) {
            flowOf(responseGenres.body()!!.map { it.toDomain() })
        } else
            emptyFlow()
    }

    override suspend fun getAllReadingBooks(fetchTopN: Int?): Flow<List<ReadingBook>> {
       val responseReadingBooks = apiService.allReadingBooks()
        return if (responseReadingBooks.isSuccessful) {
            flowOf(responseReadingBooks.body()!!.map { it.toDomain() })
        } else
            emptyFlow()
    }

    override suspend fun getAllBooksNewlyAdded(fetchTopN: Int?): Flow<List<Book>> {
        val responseBooks = apiService.allBooks()

        return if (responseBooks.isSuccessful) {
            flowOf(
                responseBooks.body()!!
                    .map { it.toDomain() }
                    .sortedByDescending { it.publishedDate }
            )
        }
        else
            emptyFlow()
    }
}