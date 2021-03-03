package kz.adamant.data.repository

import kotlinx.coroutines.flow.Flow
import kz.adamant.domain.models.Book
import kz.adamant.domain.models.Genre

interface BooksDataSource {
    suspend fun getAllBooks(): Flow<List<Book>>
    suspend fun getAllGenres(): Flow<List<Genre>>
}