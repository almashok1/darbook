package kz.adamant.bookstore.data.repository.datasources

import kotlinx.coroutines.flow.Flow
import kz.adamant.bookstore.data.repository.BooksDataSource
import kz.adamant.bookstore.data.local.dao.BooksDao
import kz.adamant.bookstore.data.local.entity.BookEntity

class BooksLocalDataSource(
    private val booksDao: BooksDao
): BooksDataSource {
    override suspend fun getAllBooks(): Flow<List<BookEntity>> {
        return booksDao.allBooks()
    }
}