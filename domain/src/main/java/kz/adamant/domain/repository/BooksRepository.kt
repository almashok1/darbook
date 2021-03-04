package kz.adamant.domain.repository

import kotlinx.coroutines.flow.Flow
import kz.adamant.domain.models.Book
import kz.adamant.domain.models.Genre
import kz.adamant.domain.models.Resource

interface BooksRepository {

    suspend fun getAllBooks(query: String?, selectedGenres: List<Int>?, shouldFetchFromNetwork: Boolean): Flow<Resource<List<Book>>>
    suspend fun getAllGenres(shouldFetchFromNetwork: Boolean): Flow<Resource<List<Genre>>>
}