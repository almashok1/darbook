package kz.adamant.data.repository.datasources

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kz.adamant.data.mappers.toDomain
import kz.adamant.data.remote.api.BooksApiService
import kz.adamant.data.repository.BooksDataSource
import kz.adamant.domain.models.Book
import kz.adamant.domain.models.Genre

class BooksRemoteDataSource(
   private val apiService: BooksApiService
): BooksDataSource {
    override suspend fun getAllBooks(): Flow<List<Book>> {
//        val booksJob = CoroutineScope(Dispatchers.IO).async { apiService.allBooks() }
//        val genresJob = CoroutineScope(Dispatchers.IO).async { apiService.allGenres() }
//
//        val responseBooks = booksJob.await()
//        val responseGenres = genresJob.await()

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
}