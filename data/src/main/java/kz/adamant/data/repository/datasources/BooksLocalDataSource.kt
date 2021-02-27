package kz.adamant.data.repository.datasources

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kz.adamant.data.repository.BooksDataSource
import kz.adamant.data.local.dao.BooksDao
import kz.adamant.data.local.models.BookEntity
import kz.adamant.data.mappers.toDomain
import kz.adamant.domain.models.Book

class BooksLocalDataSource(
    private val booksDao: BooksDao
): BooksDataSource {
    override suspend fun getAllBooks(): Flow<List<Book>> {
        return booksDao.allBooks().flatMapConcat {
            flowOf(it.map { bookEntity -> bookEntity.toDomain() })
        }
    }

    suspend fun resetBooksTable() {
        booksDao.resetTable()
    }

    suspend fun saveAllBooks(books: List<BookEntity>) {
        booksDao.insertBooks(books)
    }
}