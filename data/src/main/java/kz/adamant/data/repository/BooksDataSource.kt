package kz.adamant.data.repository

import kotlinx.coroutines.flow.Flow
import kz.adamant.domain.models.Book
import kz.adamant.domain.models.Genre
import kz.adamant.domain.models.ReadingBook

interface BooksDataSource {
    suspend fun getAllBooks(): Flow<List<Book>>
    suspend fun getAllGenres(): Flow<List<Genre>>
    suspend fun getGenreById(genreId: Int): Flow<Genre?>
    suspend fun getAllReadingBooks(): Flow<List<ReadingBook>>
    suspend fun getAllBooksNewlyAdded(): Flow<List<Book>>
}