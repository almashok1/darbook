package kz.adamant.data.repository.datasources

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kz.adamant.data.mappers.toDomain
import kz.adamant.data.repository.BooksDataSource
import kz.adamant.data.remote.api.BooksApiService
import kz.adamant.domain.models.Book

class BooksRemoteDataSource(
   private val apiService: BooksApiService
): BooksDataSource {
    override suspend fun getAllBooks(): Flow<List<Book>> {
        apiService.allBooks().body()?.let {
            return flowOf(it.map { bookDto -> bookDto.toDomain() })
        }
        return emptyFlow()
    }
}