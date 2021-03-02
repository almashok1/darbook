package kz.adamant.domain.repository

import kotlinx.coroutines.flow.Flow
import kz.adamant.domain.models.Book
import kz.adamant.domain.models.Resource

interface BooksRepository {

    suspend fun getAllBooks(): Flow<Resource<List<Book>>>

}