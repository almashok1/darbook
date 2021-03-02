package kz.adamant.data.repository

import kotlinx.coroutines.flow.Flow
import kz.adamant.domain.models.Book

interface BooksDataSource {
    suspend fun getAllBooks(): Flow<List<Book>>
}