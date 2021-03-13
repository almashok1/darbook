package kz.adamant.data.repository

import kotlinx.coroutines.flow.Flow
import kz.adamant.domain.models.Book
import kz.adamant.domain.models.Genre
import kz.adamant.domain.models.ReadingBook

interface BooksDataSource {
    suspend fun getAllBooks(): Flow<List<Book>>
    suspend fun getAllGenres(): Flow<List<Genre>>
    suspend fun getAllReadingBooks(fetchTopN: Int? = null): Flow<List<ReadingBook>>
    suspend fun getAllBooksNewlyAdded(fetchTopN: Int? = null): Flow<List<Book>>
}