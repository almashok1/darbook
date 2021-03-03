package kz.adamant.domain.repository

import kotlinx.coroutines.flow.Flow
import kz.adamant.domain.models.Book
import kz.adamant.domain.models.Genre
import kz.adamant.domain.models.Resource

interface BooksRepository {

    suspend fun getAllBooks(shouldFetchFromNetwork: Boolean = true): Flow<Resource<List<Book>>>
    suspend fun getBooksWithQuery(query: String): Flow<Resource<List<Book>>>
    suspend fun getAllGenres(): Flow<Resource<List<Genre>>>
    suspend fun getBooksWithFilteredGenresId(selectedGenres: List<Int>): Flow<Resource<List<Book>>>
}