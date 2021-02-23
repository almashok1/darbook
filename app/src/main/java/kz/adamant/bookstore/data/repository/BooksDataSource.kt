package kz.adamant.bookstore.data.repository

import kotlinx.coroutines.flow.Flow
import kz.adamant.bookstore.data.local.entity.BookEntity

interface BooksDataSource {
    suspend fun getAllBooks(): Flow<List<BookEntity>>
}