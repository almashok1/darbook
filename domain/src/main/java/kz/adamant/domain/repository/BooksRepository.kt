package kz.adamant.domain.repository

import kotlinx.coroutines.flow.Flow
import kz.adamant.domain.models.Book
import kz.adamant.domain.models.Genre
import kz.adamant.domain.models.ReadingBook
import kz.adamant.domain.models.Resource

interface BooksRepository {
    suspend fun getAllBooks(query: String?, selectedGenres: List<Int>?, shouldFetchFromNetwork: Boolean): Flow<Resource<List<Book>>>
    suspend fun getBookByIsbn(isbn: String): Flow<List<Book>>
    suspend fun getAllReadingBooks(shouldFetchFromNetwork: Boolean): Flow<Resource<List<ReadingBook>>>
    suspend fun getAllBooksNewlyAdded(shouldFetchFromNetwork: Boolean): Flow<Resource<List<Book>>>
}