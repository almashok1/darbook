package kz.adamant.bookstore.data.repository.datasources

import kotlinx.coroutines.flow.Flow
import kz.adamant.bookstore.data.repository.BooksDataSource
import kz.adamant.bookstore.data.local.entity.BookEntity

class BooksRemoteDataSource(
    //TODO Inject remote api
): BooksDataSource {
    override suspend fun getAllBooks(): Flow<List<BookEntity>> {
        TODO("Not yet implemented")
    }
}