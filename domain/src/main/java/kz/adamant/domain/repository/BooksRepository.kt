package kz.adamant.domain.repository

import kotlinx.coroutines.flow.Flow
import kz.adamant.domain.models.Book

interface BooksRepository {

    suspend fun getAllBooks(): Flow<List<Book>>

}