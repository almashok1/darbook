package kz.adamant.bookstore.data.repository

import kotlinx.coroutines.flow.Flow
import kz.adamant.bookstore.models.Book

interface BooksRepository {

    suspend fun getAllBooks(): Flow<List<Book>>

}