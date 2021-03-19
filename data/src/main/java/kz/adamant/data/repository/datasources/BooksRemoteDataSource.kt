package kz.adamant.data.repository.datasources

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kz.adamant.data.mappers.toDomain
import kz.adamant.data.remote.api.BooksApiService
import kz.adamant.data.remote.models.RentInfoDto
import kz.adamant.data.repository.BooksDataSource
import kz.adamant.domain.models.Book
import kz.adamant.domain.models.Genre
import kz.adamant.domain.models.ReadingBook
import kz.adamant.domain.models.Resource

class BooksRemoteDataSource(
   private val apiService: BooksApiService
): BooksDataSource{
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

    override suspend fun getGenreById(genreId: Int): Flow<Genre?> {
        val responseGenre = apiService.genreById(genreId)
        return if (responseGenre.isSuccessful) {
            flowOf(responseGenre.body()!!.toDomain())
        } else
            emptyFlow()
    }

    override suspend fun getAllReadingBooks(): Flow<List<ReadingBook>> {
       val responseReadingBooks = apiService.allReadingBooks()
        return if (responseReadingBooks.isSuccessful) {
            flowOf(responseReadingBooks.body()!!.map { it.toDomain() })
        } else
            emptyFlow()
    }

    override suspend fun getAllBooksNewlyAdded(): Flow<List<Book>> {
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

    suspend fun rentBook(rentInfoDto: RentInfoDto): ReadingBook? {
        return try {
            val response = apiService.rentBook(rentInfoDto)
            if (response.isSuccessful) {
                response.body()!!.toDomain()
            } else null
        } catch (e: Throwable) {
            null
        }
    }
}